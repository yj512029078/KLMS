package com.neekle.kunlunandroid.screens;

import org.doubango.ngn.NgnApplication;
import org.doubango.ngn.NgnEngine;
import org.doubango.ngn.events.NgnRegistrationEventTypes;
import org.doubango.ngn.media.NgnMediaType;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.data.UserGlobalPartInfo;
import com.neekle.kunlunandroid.presenter.common.FriendOperation;
import com.neekle.kunlunandroid.presenter.common.NotificationOperation;
import com.neekle.kunlunandroid.presenter.common.PreInitInfoOperation;
import com.neekle.kunlunandroid.presenter.common.ServerAddressOperation;
import com.neekle.kunlunandroid.sip.SipService;
import com.neekle.kunlunandroid.sip.SipSessionManager;
import com.neekle.kunlunandroid.sip.common.SipConstants;
import com.neekle.kunlunandroid.util.StringOperator;
import com.neekle.kunlunandroid.view.controls.cellOneMessage.util.imagezooming.IPhotoView;
import com.neekle.kunlunandroid.web.data.TypeServerAddress;
import com.neekle.kunlunandroid.xmpp.MyXmppCallback;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;

public class KunlunApplication extends NgnApplication {

	private static KunlunApplication sInstance;

	private static final String EXTERNAL_INSTALL_PATH = "/data";
	private static final String LIBS_FOLDER_PATH = String.format("%s/%s",
			EXTERNAL_INSTALL_PATH, "KunlunLibs");

	private static boolean mIsLibsPathDef = true;
	// 目前不是100%肯定，这种判定的做法是否有问题，比如在加载SO库的情况下，application会重启,。。。
	private static volatile boolean mIsPreinitFinish;

	private String mUserJid;

	static {
		// if (false) {
		if (mIsLibsPathDef) {
			System.loadLibrary("msc");
			System.loadLibrary("crypto.so.1.0.0");
			System.loadLibrary("ssl.so.1.0.0");
			System.loadLibrary("gloox");
		} else {
			// String DATA_FOLDER = String.format("/data/data/%s",
			// "com.neekle.kunlunandroid");
			// String LIBS_DATA_FOLDER = String.format("%s/lib",
			// DATA_FOLDER);
			// libFolderDir = LIBS_DATA_FOLDER;

			String libCryptoPath = String.format("%s/%s", LIBS_FOLDER_PATH,
					"libcrypto.so.1.0.0.so");
			String libSslPath = String.format("%s/%s", LIBS_FOLDER_PATH,
					"libssl.so.1.0.0.so");
			String libGlooxPath = String.format("%s/%s", LIBS_FOLDER_PATH,
					"libgloox.so");

			System.load(libCryptoPath);
			System.load(libSslPath);
			System.load(libGlooxPath);
		}
		// }
	}

	public static KunlunApplication getInstance() {
		return sInstance;
	}

	public KunlunApplication() {
		super();

		sInstance = this;
	}

	/**
	 * Retrieve application's context
	 * 
	 * @return Android context
	 */
	public static Context getContext() {
		return getInstance();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		MyXmppCallback myXmppCallback = MyXmppCallback.getSingleton();
		myXmppCallback.setAppContext(this);

		// initSipService();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	public void setGlobalUserJid(String jid) {
		mUserJid = jid;
	}

	public String getGlobalUserJid() {
		return mUserJid;
	}

	private static void doInitSipService() {
		initSipService();
	}

	private static void initSipService() {
		UserGlobalPartInfo data = UserGlobalPartInfo.getSingleton();
		TypeServerAddress typeSipServerAddress = data.getTypeSipServerAddress();
		String hostIp = ServerAddressOperation
				.getProperHostIp(typeSipServerAddress);
		int port = ServerAddressOperation
				.getProperHostPort(typeSipServerAddress);

		String sipUsername = null;
		String sipPasswd = null;
		XmppData xmppData = XmppData.getSingleton();
		XmppJid xmppJid = xmppData.getJid();
		if (xmppJid != null) {
			sipUsername = xmppJid.getUserName();
		}
		sipPasswd = xmppData.getPasswdString();
		// 这个后面服务器XMPP和SIP同步后改掉,注意下
		sipPasswd = SipConstants.SIP_PASSWD_MINE;

		sipUsername = StringOperator.getNotNullString(sipUsername);
		sipPasswd = StringOperator.getNotNullString(sipPasswd);

		SipSessionManager manager = SipSessionManager.getSingleton();
		manager.setOnSipSessionEventListener(mOnSipSessionEventListener);

		SipService sipService = SipService.getSingleton();
		sipService.configure(sipUsername, sipPasswd, hostIp, port);
		sipService.startSipEngine();
	}

	public static SipSessionManager.OnSipSessionEventListener mOnSipSessionEventListener = new SipSessionManager.OnSipSessionEventListener() {

		@Override
		public void onSipRegistrationState(int state) {
			if (state == NgnRegistrationEventTypes.REGISTRATION_OK.ordinal()) {
				Log.i("sipInfo", "onSipRegistrationState REGISTRATION_OK");
			}
		}

		@Override
		public void onSipInitOk() {
			Log.i("sipInfo", "onSipInitOk");

			SipService sipService = SipService.getSingleton();
			sipService.signIn();
		}
	};

	public synchronized static void onRevXmppLoginSuccess() {
		if (!mIsPreinitFinish) {
			PreInitInfoOperation operation = new PreInitInfoOperation();
			operation.doLoadBlacklistInfo();
			operation.doLoadMeSettingPartInfo();

			mIsPreinitFinish = true;
		}

		Handler handler = new Handler(Looper.getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				doInitSipService();
			}

		};

		Message message = Message.obtain();
		handler.sendMessage(message);
	}

	public static void onRevMsgToNotify() {
		Handler handler = new Handler(Looper.getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				NotificationOperation operation = new NotificationOperation();
				operation.doShowNotification();
			}

		};

		Message message = Message.obtain();
		handler.sendMessage(message);
	}

	public static void onRevFriendInviteRequest(final String partnerBareJid) {
		Handler handler = new Handler(Looper.getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				FriendOperation operation = new FriendOperation();
				operation.doHandleFriendInvite(partnerBareJid);
			}

		};

		Message message = Message.obtain();
		handler.sendMessage(message);
	}

	public static void onRevAvatarChanged(final String bareJid) {
		Handler handler = new Handler(Looper.getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				FriendOperation operation = new FriendOperation();
				operation.doLoadUserinfoToUpdateXmppFriendAndDb(bareJid);
			}

		};

		Message message = Message.obtain();
		handler.sendMessage(message);
	}
}
