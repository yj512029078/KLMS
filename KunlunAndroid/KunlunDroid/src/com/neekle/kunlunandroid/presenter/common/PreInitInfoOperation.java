package com.neekle.kunlunandroid.presenter.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import com.neekle.kunlunandroid.data.LoginExtenedInfo;
import com.neekle.kunlunandroid.data.UserGlobalPartInfo;
import com.neekle.kunlunandroid.db.TbAccountUserInfoController;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.TypeUserBlacklist;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;
import com.neekle.kunlunandroid.web.webservices.ISoapReceivedCallback;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;

public class PreInitInfoOperation implements ISoapReceivedCallback {

	private WebservicePresenter mWebsPresenter;

	public PreInitInfoOperation() {
		Context context = KunlunApplication.getContext();
		mWebsPresenter = new WebservicePresenter(context);
		mWebsPresenter.setCallback(this);
	}

	public void doLoadMeSettingPartInfo() {
		UserGlobalPartInfo data = UserGlobalPartInfo.getSingleton();
		LoginExtenedInfo loginExtenedInfo = getRecentLoginExtenedInfo();

		if (loginExtenedInfo != null) {
			assignToMeSettingPartInfo(data, loginExtenedInfo);
		}
	}

	private LoginExtenedInfo getRecentLoginExtenedInfo() {
		Context context = KunlunApplication.getContext();
		TbAccountUserInfoController controller = new TbAccountUserInfoController(
				context);

		LoginExtenedInfo info = null;
		try {
			controller.open();
			info = controller.getRecentLoginExtenedInfo();
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}

		return info;
	}

	private void assignToMeSettingPartInfo(UserGlobalPartInfo data,
			LoginExtenedInfo loginExtenedInfo) {
		boolean isMessageNotify = loginExtenedInfo.isMessageNotify();
		boolean isMessageNotifyBeep = loginExtenedInfo.isMessageNotifyBeep();
		boolean isMessageNotifyVibrate = loginExtenedInfo
				.isMessageNotifyVibrate();
		boolean isAutoAcceptInvite = loginExtenedInfo.isAutoAcceptInvite();

		data.setRevNewMsgRemind(isMessageNotify);
		data.setRevNewMsgSound(isMessageNotifyBeep);
		data.setRevNewMsgVibrate(isMessageNotifyVibrate);
		data.setNeedInviteToFriendWithMe(!isAutoAcceptInvite);
	}

	public void doLoadBlacklistInfo() {
		doWsRequestLogin();
	}

	private void doWsRequestLogin() {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_GLOCAL;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		String myJid = XmppOperation.getMyBareJid();

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestLogin(nameSpace, endPoint, prefix, myJid, handleTypeSync);
	}

	private void wsRequestLogin(String nameSpace, String endPoint,
			String prefix, String myJid, int handleType) {
		String methodName = WebserviceConstants.GLOCAL_METHOD_LOGIN;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter.getArgus(myJid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	private void doWsRequestUserGetBlackList(String sessionid) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_USER;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestUserGetBlackList(nameSpace, endPoint, prefix, sessionid,
				handleTypeSync);
	}

	private void wsRequestUserGetBlackList(String nameSpace, String endPoint,
			String prefix, String sessionid, int handleType) {
		String methodName = WebserviceConstants.USER_METHOD_USER_GET_BLACKLIST;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter
				.getArgus(sessionid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	private void doOnWsResult(Object object) {
		Object resolvedObject = mWebsPresenter.resolveObject(object);
		String method = mWebsPresenter.getMethod(resolvedObject);
		Object dataObject = mWebsPresenter.getDataObject(resolvedObject);

		if (method.equals(WebserviceConstants.GLOCAL_METHOD_LOGIN)) {
			String sessionId = null;
			if (dataObject != null) {
				sessionId = (String) dataObject;
			}

			if (sessionId != null) {
				doWsRequestUserGetBlackList(sessionId);
			}
		} else if (method
				.equals(WebserviceConstants.USER_METHOD_USER_GET_BLACKLIST)) {
			ArrayList<TypeUserBlacklist> list = (ArrayList<TypeUserBlacklist>) dataObject;

			doUpdateXmppFriendAndWriteDb(list);
		}
	}

	private void doUpdateXmppFriendAndWriteDb(ArrayList<TypeUserBlacklist> list) {
		if (list == null) {
			return;
		}

		XmppOperation xmppOperation = new XmppOperation();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			TypeUserBlacklist data = list.get(i);
			String contactJid = data.getContactJid();

			XmppData xmppData = XmppData.getSingleton();
			XmppFriend xmppFriend = xmppData.getFriend(contactJid);

			if (xmppFriend != null) {
				xmppFriend.setInBlacklist(true);
				xmppOperation.writeXmpFriendToDb(xmppFriend);
			}
		}
	}

	@Override
	public void onWsResultNotify(Object object) {
		doOnWsResult(object);
	}

}
