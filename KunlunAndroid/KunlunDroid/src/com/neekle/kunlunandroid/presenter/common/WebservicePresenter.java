package com.neekle.kunlunandroid.presenter.common;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;

import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;
import com.neekle.kunlunandroid.web.resolver.SoapObjectResolver;
import com.neekle.kunlunandroid.web.webservices.ISoapReceivedCallback;
import com.neekle.kunlunandroid.web.webservices.WebServiceManager;

public class WebservicePresenter implements ISoapReceivedCallback {

	private WebServiceManager mWebServiceManager;
	private ISoapReceivedCallback mISoapReceivedCallback;

	public WebservicePresenter(Context context) {
		mWebServiceManager = new WebServiceManager(context);
	}

	public void setCallback(ISoapReceivedCallback iSoapReceivedCallback) {
		mISoapReceivedCallback = iSoapReceivedCallback;
	}

	public Object action(WebserviceParamsObject webserviceParamsObject,
			int handleType) {
		mWebServiceManager.setCallback(this);
		Object object = mWebServiceManager.submitWebServiceRequest(
				webserviceParamsObject, handleType);
		return object;
	}

	public LinkedHashMap<String, Object> getArgus(Object... args) {
		LinkedHashMap<String, Object> hashMap = new LinkedHashMap<String, Object>();

		int length = args.length;
		for (int i = 0; i < length; i++) {
			Object value = args[i];
			hashMap.put("arg" + i, value);
		}

		return hashMap;
	}

	public Object resolveObject(Object object) {
		Object[] resultObject = new Object[2];

		Object msgObject = Array.get(object, 0);
		Object methodObject = Array.get(object, 1);
		String methodString = (String) methodObject;

		if (msgObject != null) {
			SoapObject msgSoapObject = (SoapObject) msgObject;
			resultObject[0] = SoapObjectResolver.resolve(msgSoapObject,
					methodString);
		}

		resultObject[1] = methodString;

		return resultObject;
	}

	public Object getDataObject(Object resultObject) {
		Object msgObject = Array.get(resultObject, 0);
		return msgObject;
	}

	public String getMethod(Object resultObject) {
		Object methodObject = Array.get(resultObject, 1);
		String method = (String) methodObject;

		return method;
	}

	public boolean judgeIfSuccess(int code) {
		boolean flag = true;

		int expectedCode = WebserviceConstants.WERBSERVICE_RESULT_SUCCESS_CODE;
		if (code != expectedCode) {
			flag = false;
		}

		return flag;
	}

	public boolean judgeIfExist(int code) {
		boolean isExist = false;

		switch (code) {
		case WebserviceConstants.WERBSERVICE_RESULT_FRIEND_EXIST_FRIENDLIST_CODE: {
			isExist = true;
			break;
		}
		case WebserviceConstants.WERBSERVICE_RESULT_FRIEND_EXIST_GROUP_CODE: {
			isExist = true;
			break;
		}
		case WebserviceConstants.WERBSERVICE_RESULT_CIRCLE_FRIEND_EXIST_CODE: {
			isExist = true;
			break;
		}
		case WebserviceConstants.WERBSERVICE_RESULT_ADDRESSBOOK_EXIST_CODE: {
			isExist = true;
			break;
		}
		case WebserviceConstants.WERBSERVICE_RESULT_ADDRESSBOOK_NAME_EXIST_CODE: {
			isExist = true;
			break;
		}
		default: {
			break;
		}

		}

		return isExist;
	}

	@Override
	public void onWsResultNotify(Object object) {
		if (mISoapReceivedCallback != null) {
			mISoapReceivedCallback.onWsResultNotify(object);
		}
	}

}
