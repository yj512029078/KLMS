package com.neekle.kunlunandroid.presenter.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.presenter.common.WebservicePresenter;
import com.neekle.kunlunandroid.presenter.interf.IMeActivity;
import com.neekle.kunlunandroid.screens.MainScreenActivity;
import com.neekle.kunlunandroid.screens.MeActivity;
import com.neekle.kunlunandroid.screens.MePersonalInfoActivity;
import com.neekle.kunlunandroid.screens.MeSettingActivity;
import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;
import com.neekle.kunlunandroid.web.data.friend.TypeUser;
import com.neekle.kunlunandroid.web.webservices.ISoapReceivedCallback;
import com.neekle.kunlunandroid.xmpp.XmppData;

public class MeActivityPresenter implements ISoapReceivedCallback {

	private Context context;
	private IMeActivity mView;

	private XmppData mXmppData;
	private WebservicePresenter mWebsPresenter;

	private String sessionid = null;
	private TypeUser mTypeUser = null;

	public TypeUser getmTypeUser() {
		return mTypeUser;
	}

	public String getSessionid() {
		return sessionid;
	}

	public MeActivityPresenter(MeActivity activity, Context context) {
		this.mView = activity;
		this.context = context;

		mTypeUser = new TypeUser();
		mWebsPresenter = new WebservicePresenter(context);
		mWebsPresenter.setCallback(this);
		mXmppData = XmppData.getSingleton();

		doWsRequestLogin();

	}

	private void initViewAndData() {
		actionSelectUserProfile();
	}

	private void doWsRequestLogin() {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_GLOCAL;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		String myJID = mXmppData.getJid().getBare();
		wsRequestLogin(nameSpace, endPoint, prefix, myJID, handleTypeSync);
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

	public void actionSelectUserProfile() {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String method = WebserviceConstants.USERPROFILE_METHOD_SELECT;
		String endPoint = WebserviceConstants.END_POINT_USERPROFILE;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;
		String soapAction = prefix + method;

		LinkedHashMap<String, Object> hashMap = getArgus(sessionid);

		WebserviceParamsObject wsParamsObj = new WebserviceParamsObject(
				nameSpace, method, endPoint, soapAction, hashMap);
		int handleType = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		mWebsPresenter.action(wsParamsObj, handleType);

	}

	private LinkedHashMap<String, Object> getArgus(Object... args) {

		LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();

		int length = args.length;
		for (int i = 0; i < length; i++) {
			Object value = args[i];
			hashMap.put("arg" + i, value);
		}

		return hashMap;
	}

	public void doSwitchMainScreenActivity() {
		switchActivity(context, MainScreenActivity.class, null);

	}

	public void doSwitchMePersonalInfoActivity() {
		switchActivity(context, MePersonalInfoActivity.class, null);

	}

	public void doSwitchMeSettingActivity() {
		switchActivity(context, MeSettingActivity.class, null);

	}

	private void switchActivity(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle) {
		switchActivityWithNoFinish(context, targetActivity, bundle);
		Activity activity = (Activity) context;
		activity.finish();
	}

	private void switchActivityWithNoFinish(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle) {
		Activity activity = (Activity) context;
		Intent intent = new Intent();
		intent.setClass(activity, targetActivity);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		ActivityUtil.switchTo(activity, intent);
	}

	@Override
	public void onWsResultNotify(Object object) {
		resolveWsObject(object);

	}

	@SuppressWarnings("unchecked")
	private void resolveWsObject(Object object) {
		Object resolvedObject = mWebsPresenter.resolveObject(object);
		String method = mWebsPresenter.getMethod(resolvedObject);
		Object dataObj = mWebsPresenter.getDataObject(resolvedObject);

		if (method.equals(WebserviceConstants.USERPROFILE_METHOD_SELECT)) {
			List<TypeUser> list = new ArrayList<TypeUser>();
			list = (List<TypeUser>) dataObj;
			TypeUser userProfileTypeUser = new TypeUser();
			if (list != null && list.size() != 0) {
				userProfileTypeUser = list.get(0);
				handleUserProfileSelect(userProfileTypeUser);
			}
		} else if (method.equals(WebserviceConstants.GLOCAL_METHOD_LOGIN)) {
			String tempSession = null;
			tempSession = (String) dataObj;
			if (tempSession != null)
				handleSessionID(tempSession);
			else {
				ActivityUtil.show((Activity) mView, R.string.login_error);
			}
		}

	}

	private void handleUserProfileSelect(TypeUser userProfileTypeUser) {
		Bundle bundle = new Bundle();

		bundle.putString("sessionid", this.sessionid);

		this.mTypeUser = userProfileTypeUser;
		bundle.putSerializable("TypeUser", userProfileTypeUser);

		mView.updateMeDataAndView(bundle);

	}

	private void handleSessionID(String sessioinid) {
		this.sessionid = sessioinid;
		initViewAndData();
	}
}
