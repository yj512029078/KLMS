package com.neekle.kunlunandroid.presenter.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.widget.Toast;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.XmlParser;
import com.neekle.kunlunandroid.common.Constants.XmlType;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.TypeAddsbookView;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;
import com.neekle.kunlunandroid.web.webservices.ISoapReceivedCallback;
import com.neekle.kunlunandroid.xmpp.XmppOperation;

public class AddsbookOperation implements ISoapReceivedCallback {

	private static final int MSG_WS_lOGIN_COMPLETED = 1050;

	private static final String MESSAGE_TO_PASS = "message_to_pass";

	private String mSessionId;
	private Context mContext;
	private WebservicePresenter mWebsPresenter;

	public AddsbookOperation() {
		mContext = KunlunApplication.getContext();
		mWebsPresenter = new WebservicePresenter(mContext);
		mWebsPresenter.setCallback(this);
	}

	public void doWsRequestLogin() {
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

	private void doWsRequestGetAddsbookListview(String sessionid) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_ADDSBOOK;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestGetAddsbookListview(nameSpace, endPoint, prefix, sessionid,
				handleTypeSync);
	}

	private void wsRequestGetAddsbookListview(String nameSpace,
			String endPoint, String prefix, String sessionid, int handleType) {
		String methodName = WebserviceConstants.ADDSBOOK_METHOD_GET_ADDSBOOK_LISTVIEW;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter
				.getArgus(sessionid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	private void doWsRequestAddsbookGet(String sessionid, String addsbookId) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_ADDSBOOK;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestAddsbookGet(nameSpace, endPoint, prefix, sessionid,
				addsbookId, handleTypeSync);
	}

	private void wsRequestAddsbookGet(String nameSpace, String endPoint,
			String prefix, String sessionid, String addsbookId, int handleType) {
		String methodName = WebserviceConstants.ADDSBOOK_METHOD_GET;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter.getArgus(
				sessionid, addsbookId);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	private void sendMsgToHandler(int what, String string) {
		Message message = Message.obtain();
		message.what = what;
		if (string != null) {
			Bundle bundle = new Bundle();
			bundle.putString(MESSAGE_TO_PASS, string);
			message.setData(bundle);
		}
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

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Bundle bundle = msg.getData();
			int what = msg.what;
			switch (what) {
			case MSG_WS_lOGIN_COMPLETED: {
				handleMsgWsLogin(bundle);
				break;
			}
			default: {
				break;
			}

			}
		}

	};

	private void handleMsgWsLogin(Bundle bundle) {
		String sessionId = bundle.getString(MESSAGE_TO_PASS);

		if (sessionId == null) {
			String hint = mContext.getString(R.string.ws_login_fail);
			// mIView.showHint(hint, Toast.LENGTH_SHORT);
			return;
		}

		mSessionId = sessionId;
		// 临时
		doWsRequestGetAddsbookListview(sessionId);
	}

	private void resolveWsResult(Object object) {
		Object resolvedObject = mWebsPresenter.resolveObject(object);
		String method = mWebsPresenter.getMethod(resolvedObject);
		Object dataObject = mWebsPresenter.getDataObject(resolvedObject);

		if (method.equals(WebserviceConstants.GLOCAL_METHOD_LOGIN)) {
			String sessionId = null;
			if (dataObject != null) {
				sessionId = (String) dataObject;
			}

			sendMsgToHandler(MSG_WS_lOGIN_COMPLETED, sessionId);
		} else if (method
				.equals(WebserviceConstants.ADDSBOOK_METHOD_GET_ADDSBOOK_LISTVIEW)) {
			if (dataObject != null) {
				ArrayList<TypeAddsbookView> list = (ArrayList<TypeAddsbookView>) dataObject;
				TypeAddsbookView addsbookView = list.get(0);
				String addsbookId = addsbookView.getAddbookId();

				doWsRequestAddsbookGet(mSessionId, addsbookId);
			}
		} else if (method.equals(WebserviceConstants.ADDSBOOK_METHOD_GET)) {
			String msg = (String) dataObject;
			List<?> list = null;
			try {
				String rootIdValue = "aeb600d4-b6d5-4595-aa2c-9b0cf9a90df3";
				list = AddsbookXmlLevelResolver.parse(msg, XmlType.ADDSBOOK,
						"ID", rootIdValue);
				rootIdValue = "ca74377a-a9a2-4239-a203-d42147039e61";
				list = AddsbookXmlLevelResolver.parse(msg, XmlType.ADDSBOOK,
						"ID", rootIdValue);
				rootIdValue = "e3b1b19e-b341-4d23-b25e-30b58b89e07b";
				list = AddsbookXmlLevelResolver.parse(msg, XmlType.ADDSBOOK,
						"ID", rootIdValue);
				rootIdValue = "27ee9c86-8d20-4b19-9132-349d11735cb6";
				list = AddsbookXmlLevelResolver.parse(msg, XmlType.ADDSBOOK,
						"ID", rootIdValue);
				rootIdValue = "39f2399b-592e-4ea0-ba9f-300a1c7c69d5";
				list = AddsbookXmlLevelResolver.parse(msg, XmlType.ADDSBOOK,
						"ID", rootIdValue);
				rootIdValue = "83e095bf-319a-4a47-bffa-7897bb919ec2";
				list = AddsbookXmlLevelResolver.parse(msg, XmlType.ADDSBOOK,
						"ID", rootIdValue);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onWsResultNotify(Object object) {
		resolveWsResult(object);
	}
}
