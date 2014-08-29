package com.neekle.kunlunandroid.sip;

import org.doubango.ngn.events.NgnEventArgs;
import org.doubango.ngn.events.NgnRegistrationEventArgs;
import org.doubango.ngn.events.NgnRegistrationEventTypes;
import org.doubango.ngn.media.NgnMediaType;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.screens.KunlunApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class SipSessionManager {

	private static String TAG = SipSessionManager.class.getCanonicalName();

	private static SipSessionManager mSingleton;

	private Context mContext;
	private BroadcastReceiver mStateBroadCastRecv;
	private BroadcastReceiver mSipBroadCastRecv;
	private OnSipSessionEventListener mListener;

	private boolean mIsStateBroadCastRecvRegistered;
	private boolean mIsSipBroadCastRecvRegistered;

	public interface OnSipSessionEventListener {
		public void onSipInitOk();

		public void onSipRegistrationState(int state);
	}

	public void setOnSipSessionEventListener(OnSipSessionEventListener listener) {
		mListener = listener;
	}

	private SipSessionManager() {
		mContext = KunlunApplication.getContext();
	}

	public static SipSessionManager getSingleton() {
		if (mSingleton == null) {
			synchronized (SipSessionManager.class) {
				if (mSingleton == null) {
					mSingleton = new SipSessionManager();
				}
			}
		}

		return mSingleton;
	}

	public void registerStateBroascastRev() {
		mStateBroadCastRecv = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				final String action = intent.getAction();
				Log.d(TAG, "onReceive()");

				if (NativeService.ACTION_STATE_EVENT.equals(action)) {
					if (intent.getBooleanExtra("started", false)) {
						// registerSipBroadCastRecv();
						// signIn();
						if (mListener != null) {
							Log.i("sipInfo", "onReceive onSipInitOk");
							mListener.onSipInitOk();
						}

					}
				}
			}
		};

		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(NativeService.ACTION_STATE_EVENT);
		mContext.registerReceiver(mStateBroadCastRecv, intentFilter);
		mIsStateBroadCastRecvRegistered = true;
	}

	public void registerSipBroadCastRecv() {
		mSipBroadCastRecv = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				final String action = intent.getAction();

				// Registration Event
				if (NgnRegistrationEventArgs.ACTION_REGISTRATION_EVENT
						.equals(action)) {
					NgnRegistrationEventArgs args = intent
							.getParcelableExtra(NgnEventArgs.EXTRA_EMBEDDED);

					int state = Constants.DEF_INT_VALUE;

					if (args == null) {
						Log.e(TAG, "Invalid event args");

						// added by yj
						if (mListener != null) {
							mListener.onSipRegistrationState(state);
						}

						return;
					}

					Log.i("sipInfo",
							"mSipBroadCastRecv: " + args.getEventType());

					switch (args.getEventType()) {

					case REGISTRATION_NOK: {
						state = NgnRegistrationEventTypes.REGISTRATION_NOK
								.ordinal();
						break;
					}
					case UNREGISTRATION_OK: {
						state = NgnRegistrationEventTypes.UNREGISTRATION_OK
								.ordinal();
						break;
					}
					case REGISTRATION_OK: {
						// String remoteUri = "weixiao";
						// NgnMediaType mediaType = NgnMediaType.Audio;
						// VoiceTelActivity.makeCall(remoteUri, mediaType);

						state = NgnRegistrationEventTypes.REGISTRATION_OK
								.ordinal();
						break;
					}
					case REGISTRATION_INPROGRESS: {
						state = NgnRegistrationEventTypes.REGISTRATION_INPROGRESS
								.ordinal();
						break;
					}
					case UNREGISTRATION_INPROGRESS: {
						state = NgnRegistrationEventTypes.UNREGISTRATION_INPROGRESS
								.ordinal();
						break;
					}
					case UNREGISTRATION_NOK: {
						state = NgnRegistrationEventTypes.UNREGISTRATION_NOK
								.ordinal();
						break;
					}
					default: {
						break;
					}

					}

					if (mListener != null) {
						mListener.onSipRegistrationState(state);
					}

				}
			}
		};

		final IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(NgnRegistrationEventArgs.ACTION_REGISTRATION_EVENT);
		mContext.registerReceiver(mSipBroadCastRecv, intentFilter);
		mIsSipBroadCastRecvRegistered = true;
	}

	public void unRegisterAllReceiver() {

		if ((mStateBroadCastRecv != null)) {
			mContext.unregisterReceiver(mStateBroadCastRecv);
			mIsStateBroadCastRecvRegistered = false;
			mStateBroadCastRecv = null;

			Log.i("sipinittest", "mStateBroadCastRecv unregister ");
		}

		if ((mSipBroadCastRecv != null)) {
			mContext.unregisterReceiver(mSipBroadCastRecv);
			mIsSipBroadCastRecvRegistered = false;
			mSipBroadCastRecv = null;

			Log.i("sipinittest", "mSipBroadCastRecv unregister ");
		}

	}
}
