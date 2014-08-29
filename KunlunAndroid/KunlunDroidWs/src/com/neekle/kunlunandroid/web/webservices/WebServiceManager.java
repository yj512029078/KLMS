package com.neekle.kunlunandroid.web.webservices;

import java.util.LinkedHashMap;

import org.ksoap2.SoapEnvelope;

import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class WebServiceManager implements ISoapReceivedCallback {

	private Context mContext;
	private ISoapReceivedCallback mISoapReceivedCallback;
	private WebserviceAsyncTask mAsyncTask;

	private static final String INTENT_KEY_WEBSERVICE_PARAMS_OBJECT = "intent_key_webservice_params_object";

	public WebServiceManager(Context context) {
		mContext = context;
	}

	public void setCallback(ISoapReceivedCallback iSoapReceivedCallback) {
		mISoapReceivedCallback = iSoapReceivedCallback;
	}

	public Object submitWebServiceRequest(
			WebserviceParamsObject webserviceParamsObject, int handleType) {
		Object returnObject = null;

		final int blockTaskType = WebserviceConstants.TaskThreadType.BLOCK_TASK_TYPE;
		final int asyncTaskType = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		final int webserviceIntentType = WebserviceConstants.TaskThreadType.WEBSERVICE_INTENT_SERVICE_TYPE;

		switch (handleType) {
		case asyncTaskType: {
			submitAsyncTask(webserviceParamsObject);
			break;
		}
		case webserviceIntentType: {
			submitIntentService(webserviceParamsObject);
			break;
		}
		case blockTaskType: {
			returnObject = submitBlockTask(webserviceParamsObject);
			break;
		}
		default: {
			break;
		}

		}

		return returnObject;
	}

	private void submitAsyncTask(WebserviceParamsObject webserviceParamsObject) {
		mAsyncTask = new WebserviceAsyncTask();
		mAsyncTask.setCallback(this);
		mAsyncTask.execute(webserviceParamsObject);
	}

	private Object submitBlockTask(WebserviceParamsObject webserviceParamsObject) {
		WebserviceBlockTask webserviceBlockTask = new WebserviceBlockTask();
		Object object = webserviceBlockTask
				.executeBlockTask(webserviceParamsObject);

		return object;
	}

	private void submitIntentService(
			WebserviceParamsObject webserviceParamsObject) {
		WebserviceIntentService webserviceIntentService = new WebserviceIntentService();
		webserviceIntentService.setCallback(this);
		Class<?> intentClass = WebserviceIntentService.class;
		startIntentService(intentClass, webserviceParamsObject);
	}

	private void startIntentService(Class<?> intentClass,
			WebserviceParamsObject webserviceParamsObject) {
		Intent intent = new Intent();
		intent.setClass(mContext, intentClass);
		intent.putExtra(INTENT_KEY_WEBSERVICE_PARAMS_OBJECT,
				webserviceParamsObject);
		ComponentName componentName = mContext.startService(intent);
		if (componentName == null) {

		} else {

		}
	}

	/**
	 * temprorary only pause AsyncTask
	 */
	public void stopServiceIfEmergency() {
		if (mAsyncTask != null) {
			mAsyncTask.cancel(true);
		}
	}

	@Override
	public void onWsResultNotify(Object object) {
		if (mISoapReceivedCallback != null) {
			mISoapReceivedCallback.onWsResultNotify(object);
		}
	}
}
