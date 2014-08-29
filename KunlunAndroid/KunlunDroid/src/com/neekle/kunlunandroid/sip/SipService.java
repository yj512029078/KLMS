package com.neekle.kunlunandroid.sip;

import org.doubango.ngn.media.NgnMediaType;
import org.doubango.ngn.services.INgnConfigurationService;
import org.doubango.ngn.services.INgnSipService;
import org.doubango.ngn.sip.NgnAVSession;
import org.doubango.ngn.sip.NgnSipSession.ConnectionState;
import org.doubango.ngn.sip.NgnSipStack;
import org.doubango.ngn.utils.NgnConfigurationEntry;
import org.doubango.ngn.utils.NgnUriUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.screens.TelVideoActivity;
import com.neekle.kunlunandroid.screens.TelVoiceActivity;
import com.neekle.kunlunandroid.sip.common.SipConstants;

public class SipService {

	private static String TAG = SipService.class.getCanonicalName();
	// private static final String PROXY_CSCF_HOST = "192.168.1.81";
	// private static final String REALM = PROXY_CSCF_HOST;

	private static SipService mSingleton;

	private Context mContext;
	private Engine mEngine;
	private INgnSipService mSipService;
	private INgnConfigurationService mConfigurationService;
	private SipSessionManager mSipBroadcastManager;

	private SipService() {
		Log.i("sipinittest", "SipService");

		mContext = KunlunApplication.getContext();

		mEngine = (Engine) Engine.getInstance();
		mSipService = getEngine().getSipService();
		mConfigurationService = getEngine().getConfigurationService();

		mSipBroadcastManager = SipSessionManager.getSingleton();
		mSipBroadcastManager.registerStateBroascastRev();
		mSipBroadcastManager.registerSipBroadCastRecv();
	}

	public static SipService getSingleton() {
		if (mSingleton == null) {
			synchronized (SipService.class) {
				if (mSingleton == null) {
					mSingleton = new SipService();
				}
			}
		}

		return mSingleton;
	}

	public void configure(String sipUsername, String sipPasswd, String host,
			int port) {
		String realm = host;
		setIdentity(sipUsername, sipPasswd, realm);
		setNetwork(host, port);
		setCodecs();
	}

	private void setIdentity(String displayName, String passwd, String realm) {
		String privateIdentity = displayName;
		String publicIdentityAccount = displayName;
		String publicIdentity = NgnUriUtils
				.makeValidSipUri(publicIdentityAccount);

		mConfigurationService.putString(
				NgnConfigurationEntry.IDENTITY_DISPLAY_NAME, displayName);
		mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPU,
				publicIdentity);
		mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPI,
				privateIdentity);
		mConfigurationService.putString(
				NgnConfigurationEntry.IDENTITY_PASSWORD, passwd);
		mConfigurationService.putString(NgnConfigurationEntry.NETWORK_REALM,
				realm);
		mConfigurationService.putBoolean(
				NgnConfigurationEntry.NETWORK_USE_EARLY_IMS, false);

		// Compute
		if (!mConfigurationService.commit()) {
			Log.e(TAG, "Failed to Commit() configuration");
		}
	}

	private void setNetwork(String host, int port) {
		mConfigurationService.putString(
				NgnConfigurationEntry.NETWORK_PCSCF_HOST, host);
		mConfigurationService.putInt(NgnConfigurationEntry.NETWORK_PCSCF_PORT,
				port);
		mConfigurationService.putString(
				NgnConfigurationEntry.NETWORK_TRANSPORT,
				NgnConfigurationEntry.DEFAULT_NETWORK_TRANSPORT);
		mConfigurationService.putString(
				NgnConfigurationEntry.NETWORK_PCSCF_DISCOVERY,
				NgnConfigurationEntry.DEFAULT_NETWORK_PCSCF_DISCOVERY);
		// 需要确认是否要开启
		mConfigurationService.putBoolean(
				NgnConfigurationEntry.NETWORK_USE_SIGCOMP, false);
		mConfigurationService.putBoolean(
				NgnConfigurationEntry.NETWORK_USE_WIFI, true);
		mConfigurationService.putBoolean(NgnConfigurationEntry.NETWORK_USE_3G,
				true);
		mConfigurationService.putString(
				NgnConfigurationEntry.NETWORK_IP_VERSION,
				NgnConfigurationEntry.DEFAULT_NETWORK_IP_VERSION);

		// Compute
		if (!mConfigurationService.commit()) {
			Log.e(TAG, "Failed to commit() configuration");
		}
	}

	private void setCodecs() {

	}

	public void startSipEngine() {
		if (!Engine.getInstance().isStarted()) {
			startEngine();
		}
	}

	private void startEngine() {
		final Engine engine = getEngine();

		final Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				if (!engine.isStarted()) {
					Log.d(TAG, "Starts the engine from the splash screen");
					engine.start();
				}
			}
		});
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	private Engine getEngine() {
		return (Engine) Engine.getInstance();
	}

	public void signIn() {
		if (mSipService.getRegistrationState() == ConnectionState.CONNECTING
				|| mSipService.getRegistrationState() == ConnectionState.TERMINATING) {
			mSipService.stopStack();
		}

		if (!mSipService.isRegistered()) {
			// 注意：这里如果在断网的情况下，去注册；在高版本android系统上可能会崩溃。网络请求的原因。。。 这个问题需要进一步注意下
			// 可以考虑移到线程里面或者其它方式
			mSipService.register(mContext);
		}
	}

	public void signOut() {
		if (mSipService.getRegistrationState() == ConnectionState.CONNECTING
				|| mSipService.getRegistrationState() == ConnectionState.TERMINATING) {
			mSipService.stopStack();
		}

		if (mSipService.isRegistered()) {
			mSipService.unRegister();
		}
	}

	public boolean getIsRegistered() {
		boolean isRegistered = false;
		if (mSipService.isRegistered()) {
			isRegistered = true;
		}

		return isRegistered;
	}

	public void exit() {
		// added by yj
		// 在这里直接反注册，会导致无法收到取消注册的回调，但是木有关系
		if (mSipBroadcastManager != null) {
			Log.i("sipinittest", "before unRegisterAllReceiver");
			mSipBroadcastManager.unRegisterAllReceiver();
		}

		// 暂时就放在主线程中做，如果有问题后面再看
		if (!Engine.getInstance().stop()) {
			Log.e(TAG, "Failed to stop engine");
		}

		// Handler mHanler = new Handler();
		//
		// mHanler.post(new Runnable() {
		// public void run() {
		//
		// Log.i("sipinittest", "before Engine.getInstance().stop()");
		//
		// if (!Engine.getInstance().stop()) {
		// Log.e(TAG, "Failed to stop engine");
		// }
		// // finish();
		// }
		// });
	}

	public NgnAVSession createOutgoingSession(String remoteUri,
			NgnMediaType mediaType) {
		final Engine engine = (Engine) Engine.getInstance();
		final INgnSipService sipService = engine.getSipService();
		final INgnConfigurationService configurationService = engine
				.getConfigurationService();
		// final IScreenService screenService = engine.getScreenService();
		final String validUri = NgnUriUtils.makeValidSipUri(remoteUri);
		if (validUri == null) {
			Log.e(TAG, "failed to normalize sip uri '" + remoteUri + "'");
			// modified by yj
			return null;
		} else {
			remoteUri = validUri;
			if (remoteUri.startsWith("tel:")) {
				// E.164 number => use ENUM protocol
				final NgnSipStack sipStack = sipService.getSipStack();
				if (sipStack != null) {
					String phoneNumber = NgnUriUtils
							.getValidPhoneNumber(remoteUri);
					if (phoneNumber != null) {
						String enumDomain = configurationService
								.getString(
										NgnConfigurationEntry.GENERAL_ENUM_DOMAIN,
										NgnConfigurationEntry.DEFAULT_GENERAL_ENUM_DOMAIN);
						String sipUri = sipStack.dnsENUM("E2U+SIP",
								phoneNumber, enumDomain);
						if (sipUri != null) {
							remoteUri = sipUri;
						}
					}
				}
			}
		}

		final NgnAVSession avSession = NgnAVSession.createOutgoingSession(
				sipService.getSipStack(), mediaType);
		avSession.setRemotePartyUri(remoteUri); // HACK

		return avSession;
	}

	public boolean makeCall(NgnAVSession avSession) {
		final NgnAVSession activeCall = NgnAVSession
				.getFirstActiveCallAndNot(avSession.getId());
		if (activeCall != null) {
			activeCall.holdCall();
		}

		// added by yj
		String remoteUri = avSession.getRemotePartyUri();
		boolean flag = avSession.makeCall(remoteUri);

		return flag;
	}

}
