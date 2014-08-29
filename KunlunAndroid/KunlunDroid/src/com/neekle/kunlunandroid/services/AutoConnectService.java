package com.neekle.kunlunandroid.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.data.LoginBasicInfo;
import com.neekle.kunlunandroid.data.UserGlobalPartInfo;
import com.neekle.kunlunandroid.db.TbAccountUserInfoController;
import com.neekle.kunlunandroid.presenter.common.ServerAddressOperation;
import com.neekle.kunlunandroid.presenter.common.WebservicePresenter;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.security.AESUtils;
import com.neekle.kunlunandroid.security.MD5Utils;
import com.neekle.kunlunandroid.util.ApplicationInfoUtil;
import com.neekle.kunlunandroid.util.DeviceInfo;
import com.neekle.kunlunandroid.util.NetworkState;
import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.TypeServerAddress;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.XmppService;
import com.neekle.kunlunandroid.xmpp.data.XmppConnectionError;

//测试发现，进程终止后，service 终止，依附于进程
public class AutoConnectService extends Service {

	private volatile static boolean mIsTcpConOk = true;
	private volatile static boolean mIsNetConOk = true;
	private volatile static boolean mIsThreadRun = true;

	private static Thread mThread;
	private static XmppService mXmppService;
	private static LinkedHashMap<String, IConnServiceCallback> mIConnServiceCbHashMap = new LinkedHashMap<String, AutoConnectService.IConnServiceCallback>();

	private static final int UNIT_TIME_INTERVAL = 1000;
	// private static final int MINUTE_TIME = 60 * UNIT_TIME_INTERVAL;
	private static final int MINUTE_TIME = 1 * UNIT_TIME_INTERVAL;
	private static final int TIME_INTERVAL_FOR_APP_FG = 6 * MINUTE_TIME;
	private static final int TIME_INTERVAL_FOR_APP_BG_WIFI = 6 * MINUTE_TIME;
	private static final int TIME_INTERVAL_FOR_APP_BG_3G = 15 * MINUTE_TIME;
	private static final int MSG_CONN_FAILED = 11000;
	private static final int MSG_CONN_OK = 11001;

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = connectivityManager.getActiveNetworkInfo();

				if ((info != null) && (info.isConnected())) {
					onNetConnChanged(true);
				} else {
					onNetConnChanged(false);
				}

			}
		}
	};

	private static Handler mHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			int what = msg.what;
			switch (what) {
			case MSG_CONN_FAILED: {
				handleConnFail();
				break;
			}
			case MSG_CONN_OK: {
				handleConnOk();
				break;
			}
			default:
				break;
			}
		}

	};

	private static void handleConnOk() {
		mIsTcpConOk = true;

		Set<Entry<String, IConnServiceCallback>> entrySet = mIConnServiceCbHashMap
				.entrySet();
		Iterator<Entry<String, IConnServiceCallback>> iterator = entrySet
				.iterator();
		while (iterator.hasNext()) {
			java.util.Map.Entry<String, IConnServiceCallback> entry = (java.util.Map.Entry<String, IConnServiceCallback>) iterator
					.next();
			IConnServiceCallback cb = entry.getValue();
			if (cb != null) {
				cb.onConnState(true);
			}
		}
	}

	private static void handleConnFail() {
		mIsTcpConOk = false;

		Set<Entry<String, IConnServiceCallback>> entrySet = mIConnServiceCbHashMap
				.entrySet();
		Iterator<Entry<String, IConnServiceCallback>> iterator = entrySet
				.iterator();
		while (iterator.hasNext()) {
			java.util.Map.Entry<String, IConnServiceCallback> entry = (java.util.Map.Entry<String, IConnServiceCallback>) iterator
					.next();
			IConnServiceCallback cb = entry.getValue();
			if (cb != null) {
				cb.onConnState(false);
			}
		}
	}

	public static void startAutoConnectService(Context context) {

		String serviceClsName = AutoConnectService.class.getName();
		boolean isServiceRunning = ApplicationInfoUtil.getIsServiceRunning(
				context, serviceClsName);

		if (!isServiceRunning) {
			Intent intent = new Intent();
			intent.setClass(context, AutoConnectService.class);
			context.startService(intent);
		}
	}

	public static void stopAutoConnectService(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, AutoConnectService.class);
		boolean result = context.stopService(intent);

		Log.i("onConnState", "service stop result: " + result);
	}

	public interface IConnServiceCallback {
		public void onConnState(boolean isConnOk);
	}

	public synchronized static void registerIConnServiceCallback(
			IConnServiceCallback callback) {
		// 获得实际的调用的类名
		String name = callback.getClass().getName();
		mIConnServiceCbHashMap.put(name, callback);

		boolean isConnOk = getIsConnOk();
		if (callback != null) {
			callback.onConnState(isConnOk);
		}
	}

	public synchronized static void unRegisterIConnServiceCallback(
			IConnServiceCallback callback) {
		// 获得实际的调用的类名
		String name = callback.getClass().getName();
		mIConnServiceCbHashMap.remove(name);
	}

	public static void onNetConnChanged(boolean isConnected) {
		mIsNetConOk = isConnected;
	}

	private static boolean getIsConnOk() {
		boolean isOk = mIsNetConOk && mIsTcpConOk;
		return isOk;
	}

	public static boolean getNetConnState() {
		return mIsNetConOk;
	}

	public static boolean getTcpConnState() {
		return mIsTcpConOk;
	}

	public static void onTcpConnFailed(XmppConnectionError error) {
		mHandler.sendEmptyMessage(MSG_CONN_FAILED);
	}

	public static void onLoginSuccess() {
		mHandler.sendEmptyMessage(MSG_CONN_OK);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		unregisterReceiver(mReceiver);

		// In fact, it is usually no meaning at all
		mIsThreadRun = false;
		if ((mThread != null) && mThread.isAlive()) {
			mThread.interrupt();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// initHandler();
		startThread();
		// Later the system will try to re-create the service
		return START_STICKY;
		// return super.onStartCommand(intent, flags, startId);
	}

	/* 注意：这里关于 mHandler的初始化，需要注意依附于的主线程和子线程的问题 */
	private void initHandler() {
		// 或者可以指定在Handler依附于Main Thread
		// mHandler = new Handler(Looper.getMainLooper()) {}
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				int what = msg.what;
				switch (what) {
				case MSG_CONN_FAILED: {
					handleConnFail();
					break;
				}
				case MSG_CONN_OK: {
					handleConnOk();
					break;
				}
				default:
					break;
				}
			}

		};
	}

	private void startThread() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				LoginBasicInfo info = readLoginBasicInfoFromDb();
				String bareJid = info.getBareJid();
				String fullJid = getFullJid(bareJid);
				String encriedPasswd = info.getEncriedPasswd();
				String pwd = decrypt(AutoConnectService.this, encriedPasswd);

				String serverAddress = ServerAddressOperation
						.readServerAddressFromSysConfigFile(AutoConnectService.this);
				ArrayList<TypeServerAddress> list = doGetProperAccessAddress(serverAddress);
				TypeServerAddress typeSipServerAddress = ServerAddressOperation
						.getTypeSipServerAddress(list);
				TypeServerAddress typeXmppServerAddress = ServerAddressOperation
						.getTypeXmppServerAddress(list);
				UserGlobalPartInfo data = UserGlobalPartInfo.getSingleton();
				data.setTypeSipServerAddress(typeSipServerAddress);
				data.setTypeXmppServerAddress(typeXmppServerAddress);

				String hostIp = ServerAddressOperation
						.getProperHostIp(typeXmppServerAddress);
				int port = ServerAddressOperation
						.getProperHostPort(typeXmppServerAddress);

				/* 准备相关联系人数据，从数据库中读取 */
				// judgeToReadXmppFriendFromDbAndSetData();

				while (mIsThreadRun) {
					doWork(fullJid, pwd, hostIp, port);
					doSleep();
				}
			}
		};

		if (mThread != null) {
			if (mThread.isAlive()) {
				mThread.interrupt();
				mThread = null;
			}
		}

		mThread = new Thread(runnable);
		mThread.setDaemon(true);
		mThread.start();
	}

	private String getFullJid(String bareJid) {
		String deviceId = DeviceInfo.getDeviceId(this);
		String string = bareJid + "/" + deviceId;
		return string;
	}

	private String decrypt(Context context, String toDecryptString) {
		String digest = getDigest(context);
		String originalStr = AESUtils.decrypt(digest, toDecryptString);

		return originalStr;
	}

	private String getDigest(Context context) {
		String deviceId = getDeviceId(context);
		String digest = MD5Utils.getMD5(deviceId);

		return digest;
	}

	private String getDeviceId(Context context) {
		String deviceId = DeviceInfo.getDeviceId(context);
		return deviceId;
	}

	private LoginBasicInfo readLoginBasicInfoFromDb() {
		LoginBasicInfo data = null;
		Context context = KunlunApplication.getContext();
		TbAccountUserInfoController controller = new TbAccountUserInfoController(
				context);

		try {
			controller.open();
			data = controller.getRecentLoginBasicInfo();
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return data;
	}

	private void doWork(String jidString, String pwd, String hostIp, int port) {
		if (mXmppService == null) {
			mXmppService = XmppService.getSingleton();
		}

		boolean isNetOk = NetworkState.isNetworkAvailable(this);
		boolean isLogined = mXmppService.getIsLogined();

		Log.i("onConnState", "doWork: " + "isNetOk:" + isNetOk + "  isLogined:"
				+ isLogined);

		// 移到此处，确保总是可以进行必要性的数据库读取
		XmppData xmpData = XmppData.getSingleton();
		xmpData.setAccountString(jidString, pwd);
		XmppOperation.judgeToReadXmppFriendFromDbAndSetData(jidString);

		if (isNetOk && !isLogined) {
			jidString = getProperString(jidString);
			pwd = getProperString(pwd);
			hostIp = getProperString(hostIp);

			mXmppService.login(jidString, pwd, hostIp, port);
		}
	}

	private String getProperString(String string) {
		if (string == null) {
			string = Constants.EMPTY_STRING;
		}

		return string;
	}

	private void doSleep() {
		int timeInterval = getDetectIntervalTime();
		int hasSleptTime = 0;

		while ((hasSleptTime <= timeInterval) && mIsThreadRun) {
			try {
				Thread.sleep(UNIT_TIME_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			hasSleptTime += UNIT_TIME_INTERVAL;
		}
	}

	private int getDetectIntervalTime() {
		return MINUTE_TIME;
		// boolean isAppBg =
		// ApplicationInfoUtil.getIsApplicationBackground(this);
		// if (isAppBg) {
		// return TIME_INTERVAL_FOR_APP_FG;
		// }
		//
		// boolean isWifiOk = NetworkState.isWifiActive(this);
		// if (isWifiOk) {
		// return TIME_INTERVAL_FOR_APP_BG_WIFI;
		// } else {
		// return TIME_INTERVAL_FOR_APP_BG_3G;
		// }
	}

	private ArrayList<TypeServerAddress> doGetProperAccessAddress(String userJid) {
		ArrayList<TypeServerAddress> list = null;

		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		TypeServerAddress serverAddress = info.getTypeXmppServerAddress();

		if (serverAddress == null) {
			Object object = doWsRequestManageGetServersAddress(userJid);

			Context context = KunlunApplication.getContext();
			WebservicePresenter webservicePresenter = new WebservicePresenter(
					context);
			Object resolvedObject = webservicePresenter.resolveObject(object);
			String method = webservicePresenter.getMethod(resolvedObject);
			Object dataObject = webservicePresenter
					.getDataObject(resolvedObject);
			list = (ArrayList<TypeServerAddress>) dataObject;
		}

		return list;
	}

	private Object doWsRequestManageGetServersAddress(String userJid) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_MANAGE;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.BLOCK_TASK_TYPE;
		Object object = wsRequestManageGetServersAddress(nameSpace, endPoint,
				prefix, userJid, handleTypeSync);
		return object;
	}

	private Object wsRequestManageGetServersAddress(String nameSpace,
			String endPoint, String prefix, String userJid, int handleType) {
		Context context = KunlunApplication.getContext();
		WebservicePresenter webservicePresenter = new WebservicePresenter(
				context);

		String methodName = WebserviceConstants.MANAGE_METHOD_MANAGE_GET_SERVERS_ADDRESS;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = webservicePresenter
				.getArgus(userJid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		return webservicePresenter.action(webserviceParamsObject, handleType);
	}

}
