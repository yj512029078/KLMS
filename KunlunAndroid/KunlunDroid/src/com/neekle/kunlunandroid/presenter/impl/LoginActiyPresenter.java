package com.neekle.kunlunandroid.presenter.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.XmlParser;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.data.LoginBasicInfo;
import com.neekle.kunlunandroid.data.SysConfigInfo;
import com.neekle.kunlunandroid.data.UserGlobalPartInfo;
import com.neekle.kunlunandroid.db.TbAccountUserInfoController;
import com.neekle.kunlunandroid.presenter.common.ServerAddressOperation;
import com.neekle.kunlunandroid.presenter.common.WebservicePresenter;
import com.neekle.kunlunandroid.presenter.interf.ILoginActivity;
import com.neekle.kunlunandroid.presenter.interf.ILoginActiyPresenterCb;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.screens.MainScreenActivity;
import com.neekle.kunlunandroid.security.AESUtils;
import com.neekle.kunlunandroid.security.MD5Utils;
import com.neekle.kunlunandroid.services.AutoConnectService;
import com.neekle.kunlunandroid.util.DeviceInfo;
import com.neekle.kunlunandroid.util.LogUtil;
import com.neekle.kunlunandroid.util.StringOperator;
import com.neekle.kunlunandroid.util.TimeOperater;
import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.TypeServerAddress;
import com.neekle.kunlunandroid.web.data.common.TypeReturn;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;
import com.neekle.kunlunandroid.web.data.friend.FriendInfo;
import com.neekle.kunlunandroid.web.webservices.ISoapReceivedCallback;
import com.neekle.kunlunandroid.xmpp.IXmppCallback;
import com.neekle.kunlunandroid.xmpp.MyXmppCallback;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.XmppService;
import com.neekle.kunlunandroid.xmpp.data.XmppConnectionError;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;

/**
 * 登录业务类
 * 
 * @author yj
 */
public class LoginActiyPresenter implements IXmppCallback,
		ILoginActiyPresenterCb, ISoapReceivedCallback {

	private static final int MSG_LOGIN_SUCCESS = 10;
	private static final int MSG_WS_MANAGE_GET_SERVERS_ADDRESS_COMPLETED = 11;

	private static final String MESSAGE_TO_PASS = "message_to_pass";
	private static final String DEF_JID_RESOURCE = "gloox";
	private static final String SERVER_ADDRESS_SPLIT_IDENTIFIER = ":";
	private static final String FILE_SYS_CONFIG_INFO_NAME = "sys_config_info.xml";
	private static final String INTENT_USER_JID = "user_jid";
	private static final String LOGIN_TAG = "login_tag";

	private volatile String mJid;
	private volatile String mPasswd;
	private volatile String mServerAddress;
	private ArrayList<String> mHisAccountList;

	private Context mContext;
	private MyXmppCallback mMyXmppCallback;
	private XmppService mXmppService;
	private ILoginActivity mIView;
	private WebservicePresenter mWebsPresenter;

	public LoginActiyPresenter(ILoginActivity view, Context context) {
		mIView = view;
		mContext = context;

		mXmppService = XmppService.getSingleton();
		mMyXmppCallback = MyXmppCallback.getSingleton();

		mWebsPresenter = new WebservicePresenter(context);
		mWebsPresenter.setCallback(this);
	}

	// 主要目的，更新UI，显示用户上次登录的信息在登录界面上，比如上次登录的用户名密码等
	public void initData() {
		mHisAccountList = doQueryHisAccoutFromDb();

		if ((mHisAccountList != null) && (mHisAccountList.size() != 0)) {
			String recentLoginAccount = mHisAccountList.get(0);
//			String fullJid = getFullJid(recentLoginAccount);
			mIView.showLoginAccount(recentLoginAccount);
		}

		readServerAddressFromSysConfigFile();
	}

	public LoginBasicInfo readLoginBasicInfoFromDb() {
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

	public void setXmppData(LoginBasicInfo data) {
		if (data != null) {
			String bareJid = data.getBareJid();
			String fullJid = getFullJid(bareJid);
			String encriedPasswd = data.getEncriedPasswd();
			String pwd = decrypt(mContext, encriedPasswd);

			XmppData xmppData = XmppData.getSingleton();
			xmppData.setAccountString(fullJid, pwd);
			XmppOperation.judgeToReadXmppFriendFromDbAndSetData(fullJid);
		}
	}

	private String decrypt(Context context, String toDecryptString) {
		String digest = getDigest(context);
		String originalStr = AESUtils.decrypt(digest, toDecryptString);

		return originalStr;
	}

	public boolean judgeIfToMain(LoginBasicInfo data) {
		boolean isStartToMain = false;
		if (data != null) {
			isStartToMain = data.isStartToMain();
		}

		return isStartToMain;
	}

	private String getFullJid(String bareJid) {
		String deviceId = DeviceInfo.getDeviceId(mContext);
		String string = bareJid + "/" + deviceId;
		return string;
	}

	private ArrayList<String> doQueryHisAccoutFromDb() {
		ArrayList<String> list = null;

		Context context = KunlunApplication.getContext();
		TbAccountUserInfoController controller = new TbAccountUserInfoController(
				context);

		try {
			controller.open();
			list = controller.getTimeDescOrderedHisAccount();
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return list;
	}

	private void readServerAddressFromSysConfigFile() {
		SysConfigInfo sysConfigInfo = null;

		File file = mContext.getFileStreamPath(FILE_SYS_CONFIG_INFO_NAME);
		if (!file.exists()) {
			return;
		}

		FileInputStream fileInputStream = null;
		String xmlString = null;
		try {
			fileInputStream = new FileInputStream(file);
			int length = (int) file.length();
			byte[] bytes = new byte[length];

			fileInputStream.read(bytes);
			xmlString = new String(bytes);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (xmlString == null) {
			return;
		}

		try {
			List<?> list = XmlParser.parse(xmlString,
					Constants.XmlType.SYS_CONFIG_INFO);
			if ((list != null) && (list.size() != 0)) {
				Object object = list.get(0);
				sysConfigInfo = (SysConfigInfo) object;
				mServerAddress = sysConfigInfo.getWebServiceURL();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void killProcess() {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	public ArrayList<String> getHisAccounts() {
		return mHisAccountList;
	}

	public void login(String jidString, String pwd) {
		if ((mServerAddress == null)
				|| (mServerAddress.equals(Constants.EMPTY_STRING))) {
			String msg = mContext.getString(R.string.please_set_server_address);
			mIView.showErrorToast(msg, false);
			return;
		}

		mJid = getNotNullString(jidString);
		mPasswd = getNotNullString(pwd);

		if ((mJid.equals(Constants.EMPTY_STRING))
				|| (mPasswd.equals(Constants.EMPTY_STRING))) {
			String msg = mContext
					.getString(R.string.main_invalid_jid_or_passwd);
			mIView.showErrorToast(msg, false);
			return;
		}

		notifyUIShowProgressDialog();
		doWsRequestManageGetServersAddress(mServerAddress);
	}

	private String getNotNullString(String string) {
		if (string == null) {
			string = Constants.EMPTY_STRING;
		}

		return string;
	}

	public void logout() {
		mXmppService.logout();
	}

	public void clearResource() {
		mHandler.removeMessages(MSG_LOGIN_SUCCESS);
	}

	private void closeProgressDialog() {
		mIView.shutProgressDialog();
	}

	private void sendMsgToHandler(int what) {
		Message message = Message.obtain();
		message.what = what;

		mHandler.sendMessage(message);
	}

	private void sendMsgToHandler(int what, Parcelable parcelable) {
		Message message = Message.obtain();
		message.what = what;
		if (parcelable != null) {
			Bundle bundle = new Bundle();
			bundle.putParcelable(MESSAGE_TO_PASS, parcelable);
			message.setData(bundle);
		}
		mHandler.sendMessage(message);
	}

	private void sendMsgToHandler(int what, ArrayList<? extends Parcelable> list) {
		Message message = Message.obtain();
		message.what = what;
		if (list != null) {
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(MESSAGE_TO_PASS, list);
			message.setData(bundle);
		}
		mHandler.sendMessage(message);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Bundle bundle = msg.getData();
			if (msg.what == MSG_LOGIN_SUCCESS) {
				handleRevLoginSuccess(bundle);
			} else if (msg.what == MSG_WS_MANAGE_GET_SERVERS_ADDRESS_COMPLETED) {
				handleRevGetServerAddressCompleted(bundle);
			}
		}

	};

	private void handleRevLoginSuccess(Bundle bundle) {
		Context appContext = mContext.getApplicationContext();
		// tmp keep it, later rm it
		KunlunApplication kunlunApplication = (KunlunApplication) appContext;
		kunlunApplication.setGlobalUserJid(mJid);

		notifyUIIfFinish(true, null, null);

		Class<? extends Activity> intentClass = MainScreenActivity.class;
		doSwitchMainScreenActivity(mContext, intentClass);
		AutoConnectService.startAutoConnectService(mContext);
	}

	private void handleRevGetServerAddressCompleted(Bundle bundle) {
		ArrayList<TypeServerAddress> list = bundle
				.getParcelableArrayList(MESSAGE_TO_PASS);

		TypeServerAddress typeSipServerAddress = ServerAddressOperation
				.getTypeSipServerAddress(list);
		TypeServerAddress typeXmppServerAddress = ServerAddressOperation
				.getTypeXmppServerAddress(list);
		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		info.setTypeSipServerAddress(typeSipServerAddress);
		info.setTypeXmppServerAddress(typeXmppServerAddress);

		if (typeXmppServerAddress != null) {
			String hostIp = ServerAddressOperation
					.getProperHostIp(typeXmppServerAddress);
			int port = ServerAddressOperation
					.getProperHostPort(typeXmppServerAddress);

			XmppData xmpData = XmppData.getSingleton();
			xmpData.setAccountString(mJid, mPasswd);
			XmppOperation.judgeToReadXmppFriendFromDbAndSetData(mJid);
			mMyXmppCallback.setLoginActiyCallback(this);
			mXmppService.login(mJid, mPasswd, hostIp, port);
		} else {
			String hint = mContext
					.getString(R.string.unable_to_get_valid_service);
			notifyUIIfFinish(false, hint, true);
		}
	}

	private void constructInfoAndInsertToDb() {
		LoginBasicInfo info = constructLoginBasicInfo();
		doInsertLoginInfoToDb(info);
	}

	private LoginBasicInfo constructLoginBasicInfo() {
		LoginBasicInfo loginBasicInfo = new LoginBasicInfo();
		loginBasicInfo.setAutoLogin(true);
		loginBasicInfo.setRememberPasswd(true);
		loginBasicInfo.setStartToMain(true);

		XmppJid xmppJid = new XmppJid(mJid);
		String bareJid = xmppJid.getBare();
		loginBasicInfo.setBareJid(bareJid);

		String encriedPasswd = encrypt(mContext, mPasswd);
		loginBasicInfo.setEncriedPasswd(encriedPasswd);

		String recentLoginTime = TimeOperater.getCurrentTime();
		loginBasicInfo.setRecentLoginTime(recentLoginTime);

		loginBasicInfo.setServerAddress(mServerAddress);

		return loginBasicInfo;
	}

	private void doInsertLoginInfoToDb(LoginBasicInfo loginBasicInfo) {
		Context context = KunlunApplication.getContext();
		TbAccountUserInfoController controller = new TbAccountUserInfoController(
				context);

		try {
			controller.open();
			controller.insertOrNeededUpdate(loginBasicInfo);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	// temprorary not invoked, because no result to return
	// For general, result has been sent to callback in native code
	@Override
	public void onXmppNotify(Object object) {
		if (object != null) {
			if (object instanceof Integer) {
				Integer value = (Integer) object;
				LogUtil.logInfo(LOGIN_TAG, "onXmppNotify: " + value + "");
			}
		}
	}

	@Override
	public void onLoginSuccess() {
		constructInfoAndInsertToDb();
		sendMsgToHandler(MSG_LOGIN_SUCCESS);
	}

	private String encrypt(Context context, String toEncryString) {
		String digest = getDigest(context);
		String encryptionString = AESUtils.encrypt(digest, toEncryString);

		return encryptionString;
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

	public void doSwitchMainScreenActivity(Context context,
			Class<? extends Activity> targetActivity) {
		mMyXmppCallback.setLoginActiyCallback(null);

		// Bundle bundle = new Bundle();
		// bundle.putString(INTENT_USER_JID, mJid);

		switchActivity(context, targetActivity, null);
	}

	public void doSwitchLoginSettingActivity(Context context,
			Class<? extends Activity> targetActivity) {
		mMyXmppCallback.setLoginActiyCallback(null);
		switchActivity(context, targetActivity, null);
	}

	private void switchActivity(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle) {
		Activity activity = (Activity) context;
		Intent intent = new Intent();
		intent.setClass(activity, targetActivity);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		ActivityUtil.switchTo(activity, intent);
		activity.finish();
	}

	@Override
	public void onInvaildJID() {
		int resId = R.string.main_invalid_jid;
		String hint = mContext.getString(resId);
		notifyUIIfFinish(false, hint, false);
	}

	@Override
	public void onInvaildPassword() {
		int resId = R.string.main_passwd_should_not_null;
		String hint = mContext.getString(resId);
		notifyUIIfFinish(false, hint, false);
	}

	// when login for the second time, it will be invoked for some reason of
	// error
	@Override
	public void onTcpConnFailed(XmppConnectionError errorInfo) {
		String errorString = errorInfo.getError();

		int resId = R.string.main_login_failed;
		String hint = mContext.getString(resId);
		notifyUIIfFinish(false, hint, true);
	}

	@Override
	public void onAuthFailed() {
		int resId = R.string.main_login_failed;
		String hint = mContext.getString(resId);
		notifyUIIfFinish(false, hint, true);
	}

	private void notifyUIShowProgressDialog() {
		showProgressDialog();
	}

	private void showProgressDialog() {
		String tittle = mContext.getString(R.string.main_is_loging);
		String msg = mContext.getString(R.string.please_wait);
		mIView.showProgressDialog(tittle, msg);
	}

	private void notifyUIIfFinish(Boolean isSuccess, String hint, Boolean isLong) {
		if (!isSuccess) {
			mIView.showErrorToast(hint, isLong);
		}

		closeProgressDialog();
	}

	private void doWsRequestManageGetServersAddress(String userJid) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_MANAGE;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestManageGetServersAddress(nameSpace, endPoint, prefix, userJid,
				handleTypeSync);
	}

	private void wsRequestManageGetServersAddress(String nameSpace,
			String endPoint, String prefix, String userJid, int handleType) {
		String methodName = WebserviceConstants.MANAGE_METHOD_MANAGE_GET_SERVERS_ADDRESS;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter
				.getArgus(userJid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	private void resolveWsResult(Object object) {
		Object resolvedObject = mWebsPresenter.resolveObject(object);
		String method = mWebsPresenter.getMethod(resolvedObject);
		Object dataObject = mWebsPresenter.getDataObject(resolvedObject);

		if (method
				.equals(WebserviceConstants.MANAGE_METHOD_MANAGE_GET_SERVERS_ADDRESS)) {
			ArrayList<TypeServerAddress> list = null;
			if (dataObject != null) {
				list = (ArrayList<TypeServerAddress>) dataObject;
			}

			sendMsgToHandler(MSG_WS_MANAGE_GET_SERVERS_ADDRESS_COMPLETED, list);
		}
	}

	@Override
	public void onWsResultNotify(Object object) {
		resolveWsResult(object);
	}

}
