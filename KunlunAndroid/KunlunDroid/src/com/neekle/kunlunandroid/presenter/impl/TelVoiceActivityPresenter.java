package com.neekle.kunlunandroid.presenter.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.doubango.ngn.events.NgnInviteEventArgs;
import org.doubango.ngn.events.NgnInviteEventTypes;
import org.doubango.ngn.events.NgnMediaPluginEventArgs;
import org.doubango.ngn.media.NgnMediaType;
import org.doubango.ngn.sip.NgnAVSession;
import org.doubango.ngn.sip.NgnInviteSession.InviteState;
import org.doubango.ngn.utils.NgnStringUtils;
import org.doubango.ngn.utils.NgnTimer;
import org.doubango.ngn.utils.NgnUriUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.util.Log;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.presenter.interf.ITelVoiceActivity;
import com.neekle.kunlunandroid.screens.TelVoiceActivity.ViewType;
import com.neekle.kunlunandroid.sip.Engine;
import com.neekle.kunlunandroid.sip.SipOperation;
import com.neekle.kunlunandroid.sip.SipService;
import com.neekle.kunlunandroid.sip.common.SipConstants;
import com.neekle.kunlunandroid.util.NetworkState;

public class TelVoiceActivityPresenter {

	private static final String TAG = TelVoiceActivityPresenter.class
			.getCanonicalName();

	private Context mContext;
	private Activity mActivity;
	private ITelVoiceActivity mIView;
	private OnDataUpdateListener mOnDataUpdateListener;

	private String mSessionId;
	private NgnAVSession mAVSession;
	private BroadcastReceiver mSipBroadCastRecv;
	// private final NgnTimer mTimerSuicide;
	private final NgnTimer mTimerInCall;
	// 不确定音频部分是否需要，视频部分是需要的
	private final NgnTimer mTimerBlankPacket;
	private static int mCountBlankPacket;
	private boolean mIsVideoCall;

	private ViewType mCurrentViewType;
	private boolean mIsKeyboardShow;
	private boolean mIsPassiveCall;

	private long mDbIdForUpdateDb = Constants.DEF_INT_VALUE;

	public TelVoiceActivityPresenter(ITelVoiceActivity view, Context context) {
		mIView = view;
		mContext = context;
		mActivity = (Activity) context;

		mCurrentViewType = ViewType.ViewDef;

		// mTimerSuicide = new NgnTimer();
		mTimerInCall = new NgnTimer();
		mTimerBlankPacket = new NgnTimer();
	}

	public void init(Intent intent) {
		NgnMediaType mediaType = NgnMediaType.Audio;
		mSessionId = getSessionId(intent, mediaType);
		if (mSessionId == null) {
			String string = mContext
					.getString(R.string.session_failed_as_invalid_session);
			mIView.showErrorHint(string);
			// ... to finish
			finishActivity();
			return;
		}

		initSession();
		loadView();
		setSetting();

		if (!mIsPassiveCall) {
			doMakeAudioCall();
		}
	}

	private void doMakeAudioCall() {
		boolean isSuccess = makeAudioCall();
		if (!isSuccess) {
			String string = mContext.getString(R.string.call_failed);
			mIView.showErrorHint(string);
			// ..., do sth to finish
			finishActivity();
		}
	}

	private String getSessionId(Intent intent, NgnMediaType mediaType) {
		String sessionId = null;

		String telAction = intent
				.getStringExtra(SipConstants.INTENT_TEL_ACTION_KEY);
		if (telAction.equals(SipConstants.TelAction.OUTGOING)) {
			String partnerSipUsername = intent
					.getStringExtra(SipConstants.INTENT_SIP_USERNAME_PARTNER_KEY);

			NgnAVSession avSession = createOutgoingSession(partnerSipUsername,
					mediaType);
			if (avSession != null) {
				long id = avSession.getId();
				sessionId = id + Constants.EMPTY_STRING;
			}

			mIsPassiveCall = false;
		} else {
			sessionId = intent
					.getStringExtra(SipConstants.INTENT_AV_SESSION_ID_KEY);
			mIsPassiveCall = true;
		}

		return sessionId;
	}

	private void initSession() {
		if (NgnStringUtils.isNullOrEmpty(mSessionId)) {
			Log.e(TAG, "Invalid audio/video session");
			String string = mContext
					.getString(R.string.session_failed_as_invalid_session);
			mIView.showErrorHint(string);
			// ..., 考虑修改
			finishActivity();
			return;
		}

		mAVSession = NgnAVSession.getSession(NgnStringUtils.parseLong(
				mSessionId, -1));
		if (mAVSession == null) {
			Log.e(TAG, String.format(
					"Cannot find audio/video session with id=%s", mSessionId));
			String string = mContext
					.getString(R.string.session_failed_as_invalid_session);
			mIView.showErrorHint(string);
			// ...,考虑修改
			finishActivity();
			return;
		}

		mAVSession.incRef();
		mAVSession.setContext(mContext);

		// getRemoteParty info
		// ...，可能我们不能照抄代码的，要另外处理。用我们自己的逻辑显示相关信息。因为联系人是在我们自己的XMPP中

		mIsVideoCall = mAVSession.getMediaType() == NgnMediaType.AudioVideo
				|| mAVSession.getMediaType() == NgnMediaType.Video;
		mCountBlankPacket = 0;

		registerSipBroadcastReceiver();
	}

	private void registerSipBroadcastReceiver() {
		mSipBroadCastRecv = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (NgnInviteEventArgs.ACTION_INVITE_EVENT.equals(intent
						.getAction())) {
					handleSipEvent(intent);
				} else if (NgnMediaPluginEventArgs.ACTION_MEDIA_PLUGIN_EVENT
						.equals(intent.getAction())) {
					handleMediaEvent(intent);
				}
			}
		};

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(NgnInviteEventArgs.ACTION_INVITE_EVENT);
		intentFilter
				.addAction(NgnMediaPluginEventArgs.ACTION_MEDIA_PLUGIN_EVENT);
		mContext.registerReceiver(mSipBroadCastRecv, intentFilter);
	}

	private void setSetting() {
		mActivity.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
	}

	private boolean makeAudioCall() {
		boolean isSuccess = false;

		NgnAVSession avSession = NgnAVSession.getSession(NgnStringUtils
				.parseLong(mSessionId, -1));
		if (avSession != null) {
			isSuccess = makeAudioCall(avSession);
		}

		return isSuccess;
	}

	private void loadView() {
		ViewType viewType = getShouldLoadViewByState();
		// 如果后面策略修改，这个相应改掉
		if (mIsKeyboardShow) {
			viewType = ViewType.ViewInCallShowKeyboard;
		}

		if (viewType != mCurrentViewType) {
			mIView.loadView(viewType);
		}

		writeDb(viewType);
	}

	private void writeDb(ViewType viewType) {
		if (viewType == ViewType.ViewInComing) {
			writeDbIfIncomingState();
		} else if (viewType == ViewType.ViewInCalling) {
			writeDbIfIncallingState();
		} else if (viewType == ViewType.ViewInCall) {
			writeDbIfIncallState();
		}
	}

	private ViewType getShouldLoadViewByState() {
		ViewType viewType = mCurrentViewType;

		switch (mAVSession.getState()) {
		case INCOMING:
			viewType = ViewType.ViewInComing;
			break;

		case INPROGRESS:
		case REMOTE_RINGING: // 这个状态不太确定
			viewType = ViewType.ViewInCalling;
			break;

		// added by yj
		// 根据测试结果来看，EARLY_MEDIA 应该是拨打了，对方也收到了，但是对方现在还没有回应的状态
		case EARLY_MEDIA:
			break;

		case INCALL:
			viewType = ViewType.ViewInCall;
			break;

		// 如此三项，会话都是不属于活动状态
		// NgnInviteSession 构造即初始化, state 初始值为 NONE

		// 可能需要处理，暂不处理
		case NONE:
			break;

		case TERMINATING:
		case TERMINATED:
		default:
			break;
		}

		return viewType;
	}

	public void setKeyboardShow(boolean isShow) {
		mIsKeyboardShow = isShow;
	}

	private void handleMediaEvent(Intent intent) {
		final String action = intent.getAction();

		if (NgnMediaPluginEventArgs.ACTION_MEDIA_PLUGIN_EVENT.equals(action)) {
			NgnMediaPluginEventArgs args = intent
					.getParcelableExtra(NgnMediaPluginEventArgs.EXTRA_EMBEDDED);
			if (args == null) {
				Log.e(TAG, "Invalid event args");
				return;
			}

			switch (args.getEventType()) {
			case STARTED_OK: // started or restarted (e.g. reINVITE)
			{
				mIsVideoCall = (mAVSession.getMediaType() == NgnMediaType.AudioVideo || mAVSession
						.getMediaType() == NgnMediaType.Video);
				loadView();

				break;
			}
			case PREPARED_OK:
			case PREPARED_NOK:
			case STARTED_NOK:
			case STOPPED_OK:
			case STOPPED_NOK:
			case PAUSED_OK:
			case PAUSED_NOK: {
				break;
			}
			}
		}
	}

	private void handleSipEvent(Intent intent) {
		@SuppressWarnings("unused")
		InviteState state;
		if (mAVSession == null) {
			Log.e(TAG, "Invalid session object");
			return;
		}

		final String action = intent.getAction();
		if (NgnInviteEventArgs.ACTION_INVITE_EVENT.equals(action)) {
			NgnInviteEventArgs args = intent
					.getParcelableExtra(NgnInviteEventArgs.EXTRA_EMBEDDED);
			if (args == null) {
				Log.e(TAG, "Invalid event args");
				return;
			}

			if (args.getSessionId() != mAVSession.getId()) {
				if (args.getEventType() == NgnInviteEventTypes.REMOTE_TRANSFER_INPROGESS) {
					// Native code created new session handle to be used to
					// replace the current one (event = "tsip_i_ect_newcall").

					// commented by yj, perhaps no need
					// mAVTransfSession = NgnAVSession.getSession(args
					// .getSessionId());
				}
				return;
			}

			switch ((state = mAVSession.getState())) {
			case NONE:
			default:
				break;

			case INCOMING:
				// added by yj
				actionIfIncomingState();
				break;

			case INPROGRESS:
			case REMOTE_RINGING:
				actionIfIncallingState();
				break;

			// added by yj, 先临时这样，目前已知的是主动拨打对方电话会有这种情况，不知道对方拨打自己，是否会存在这种状态
			// 后面可能要确认
			case EARLY_MEDIA:
				break;

			case INCALL:
				actionIfIncallState(args);
				break;

			case TERMINATING:
			case TERMINATED:
				actionIfTerminateState();
				break;

			}

		}
	}

	private void actionIfIncomingState() {
		mIView.loadView(ViewType.ViewInComing);
		writeDbIfIncomingState();
	}

	private void writeDbIfIncomingState() {
		if (mDbIdForUpdateDb == Constants.DEF_INT_VALUE) {
			SipOperation sipOperation = new SipOperation();
			String partUri = mAVSession.getRemotePartyUri();
			String partnerUsername = NgnUriUtils.getUserName(partUri);
			mDbIdForUpdateDb = sipOperation.writeIncomePhone(partnerUsername,
					false);
		}
		Log.e("updatedbtest", "writeDbIfIncomingState writeDbIfIncallingState");
	}

	private void actionIfIncallingState() {
		mIView.loadView(ViewType.ViewInCalling);
		writeDbIfIncallingState();
	}

	private void writeDbIfIncallingState() {
		if (mDbIdForUpdateDb == Constants.DEF_INT_VALUE) {
			SipOperation sipOperation = new SipOperation();
			String partUri = mAVSession.getRemotePartyUri();
			String partnerUsername = NgnUriUtils.getUserName(partUri);
			mDbIdForUpdateDb = sipOperation.writeOutgoPhone(partnerUsername,
					false);
		}
		Log.i("dbvalue", "mDbIdForUpdateDb: " + mDbIdForUpdateDb);

		Log.e("updatedbtest", "writeDbIfIncallState writeDbIfIncallingState");
	}

	private void actionIfTerminateState() {
		// if (mTransferDialog != null) {
		// mTransferDialog.cancel();
		// mTransferDialog = null;
		// }

		// mTimerSuicide.schedule(mTimerTaskSuicide,
		// new Date(new Date().getTime() + 1500));
		// mTimerTaskInCall.cancel();
		mTimerBlankPacket.cancel();
		// 考虑是否要修改。。。
		finishActivity();

		// release power lock
		// if (mWakeLock != null && mWakeLock.isHeld()) {
		// mWakeLock.release();
		// }
	}

	private void actionIfIncallState(NgnInviteEventArgs args) {

		Log.e("updatedbtest", "writeDbIfIncallState actionIfIncallState");

		Engine engine = (Engine) Engine.getInstance();
		engine.getSoundService().stopRingTone();
		mAVSession.setSpeakerphoneOn(false);

		mIView.loadView(ViewType.ViewInCall);

		// Send blank packets to open NAT pinhole
		if (mAVSession != null) {
			// 推测，应该运用于视频
			// applyCamRotation(mAVSession.compensCamRotation(true));
			mTimerBlankPacket.schedule(mTimerTaskBlankPacket, 0, 250);
			// 暂时也注释，后面放开
			if (!mIsVideoCall) {
				mTimerInCall.schedule(mTimerTaskInCall, 0, 1000);
			}
		}

		// release power lock if not video call
		// if (!mIsVideoCall && mWakeLock != null && mWakeLock.isHeld())
		// {
		// mWakeLock.release();
		// }

		switch (args.getEventType()) {
		case REMOTE_DEVICE_INFO_CHANGED: {
			// 注释掉相关代码，音频部分不需要，视频部分需要
			break;
		}
		case MEDIA_UPDATED: {
			// 注释掉了相关处理，可能需要使用，目前不确定
			break;
		}

		// 这里去掉了ImsDroid工程中大量的和 transfer 相关的代码
		}

		writeDbIfIncallState();
	}

	private void writeDbIfIncallState() {
		Log.e("updatedbtest", "writeDbIfIncallState top");

		if (mDbIdForUpdateDb == Constants.DEF_INT_VALUE) {
			return;
		}

		SipOperation sipOperation = new SipOperation();
		sipOperation.updateConnect(mDbIdForUpdateDb, true);

		Log.e("updatedbtest", "writeDbIfIncallState bottom");
	}

	private final TimerTask mTimerTaskInCall = new TimerTask() {
		@Override
		public void run() {
			if (mAVSession != null) {
				final Date date = new Date(new Date().getTime()
						- mAVSession.getStartTime());
				final SimpleDateFormat durationTimerFormat = new SimpleDateFormat(
						"mm:ss");

				mActivity.runOnUiThread(new Runnable() {
					public void run() {
						try {
							String time = durationTimerFormat.format(date);

							if (mOnDataUpdateListener != null) {
								mOnDataUpdateListener.onIncallTimeUpdate(time);
							}
						} catch (Exception e) {

						}

					}
				});

			}
		}
	};

	private final TimerTask mTimerTaskBlankPacket = new TimerTask() {
		@Override
		public void run() {
			Log.d(TAG,
					"Resending Blank Packet "
							+ String.valueOf(mCountBlankPacket));
			if (mCountBlankPacket < 3) {
				if (mAVSession != null) {
					mAVSession.pushBlankPacket();
				}
				mCountBlankPacket++;
			} else {
				cancel();
				mCountBlankPacket = 0;
			}
		}
	};

	private void cancelBlankPacket() {
		if (mTimerBlankPacket != null) {
			mTimerBlankPacket.cancel();
			mCountBlankPacket = 0;
		}
	}

	// 暂时可以不管，后面需要处理音量
	public boolean onVolumeChanged(boolean bDown) {
		if (mAVSession != null) {
			return mAVSession.onVolumeChanged(bDown);
		}
		return false;
	}

	public NgnAVSession createOutgoingSession(String name,
			NgnMediaType mediaType) {
		NgnAVSession avSession = null;
		SipService sipService = SipService.getSingleton();
		boolean isRegistered = sipService.getIsRegistered();

		if (isRegistered && !NgnStringUtils.isNullOrEmpty(name)) {
			String remoteUri = name;
			avSession = sipService.createOutgoingSession(remoteUri, mediaType);
		}

		return avSession;
	}

	public boolean makeAudioCall(NgnAVSession avSession) {
		boolean isSuccess = false;

		if (avSession != null) {
			SipService sipService = SipService.getSingleton();
			isSuccess = sipService.makeCall(avSession);
		}

		return isSuccess;
	}

	public void toggleSpeakerphone() {
		if (mAVSession != null) {
			mAVSession.toggleSpeakerphone();
		}
	}

	public void toggleMicrophoneMute() {
		if (mAVSession != null) {
			boolean isMute = mAVSession.isMicrophoneMute();
			isMute = !isMute;
			mAVSession.setMicrophoneMute(isMute);
		}
	}

	public boolean hangUpCall() {
		if (mAVSession != null) {
			return mAVSession.hangUpCall();
		}
		return false;
	}

	public boolean acceptCall() {
		if (mAVSession != null) {
			return mAVSession.acceptCall();
		}
		return false;
	}

	public boolean getIsWifiUsed() {
		boolean isWifi = NetworkState.isWifiActive(mContext);
		return isWifi;
	}

	public void doClearResource() {
		Log.d(TAG, "onDestroy()");

		if (mSipBroadCastRecv != null) {
			mContext.unregisterReceiver(mSipBroadCastRecv);
			mSipBroadCastRecv = null;
		}

		mTimerInCall.cancel();
		// mTimerSuicide.cancel();
		cancelBlankPacket();

		// if (mWakeLock != null && mWakeLock.isHeld()) {
		// mWakeLock.release();
		// }
		// mWakeLock = null;

		destroyAVSession();
	}

	private void finishActivity() {
		mDbIdForUpdateDb = Constants.DEF_INT_VALUE;
		doClearResource();

		if (mActivity != null) {
			mActivity.finish();
			mActivity = null;
		}
	}

	private void destroyAVSession() {
		if (mAVSession != null) {
			mAVSession.setContext(null);
			mAVSession.decRef();
		}
	}

	public void setOnDataUpdateListener(OnDataUpdateListener listener) {
		mOnDataUpdateListener = listener;
	}

	public interface OnDataUpdateListener {
		public void onNetworkUpdate(boolean isWifi);

		public void onPhotoUpdate(Bitmap bitmap);

		public void onIncallTimeUpdate(String time);

		public void onNameUpdate(String name);

		public void onLocationUpdate(String location);
	}

}
