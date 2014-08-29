package com.neekle.kunlunandroid.webservices.test;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;

import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;
import com.neekle.kunlunandroid.web.resolver.SoapObjectResolver;
import com.neekle.kunlunandroid.web.views.IView;
import com.neekle.kunlunandroid.web.webservices.ISoapReceivedCallback;
import com.neekle.kunlunandroid.web.webservices.WebServiceManager;

public class UnitTestPresenter {

	private WebServiceManager mWebServiceManager;
	private int mHandleType = WebserviceConstants.TaskThreadType.BLOCK_TASK_TYPE;

	public UnitTestPresenter() {
		mWebServiceManager = new WebServiceManager(null);
	}

	public Object action(WebserviceParamsObject webserviceParamsObject) {
		mWebServiceManager.setCallback(null);
		Object object = mWebServiceManager.submitWebServiceRequest(
				webserviceParamsObject, mHandleType);

		return object;
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

	public Object getDataObject(Object object) {
		Object msgObject = Array.get(object, 0);
		return msgObject;
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

}
