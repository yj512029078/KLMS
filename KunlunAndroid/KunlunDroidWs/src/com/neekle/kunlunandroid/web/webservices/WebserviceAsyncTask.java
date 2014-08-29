package com.neekle.kunlunandroid.web.webservices;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.ksoap2.SoapEnvelope;

import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;

import android.os.AsyncTask;

 class WebserviceAsyncTask extends
		AsyncTask<WebserviceParamsObject, Object, Object> {

	private ISoapReceivedCallback mISoapReceivedCallback;

	public WebserviceAsyncTask() {

	}

	public void setCallback(ISoapReceivedCallback iSoapReceivedCallback) {
		mISoapReceivedCallback = iSoapReceivedCallback;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);

		if (mISoapReceivedCallback != null) {
			mISoapReceivedCallback.onWsResultNotify(result);
			mISoapReceivedCallback = null;
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Object... values) {
		super.onProgressUpdate(values);

	}

	@Override
	protected Object doInBackground(WebserviceParamsObject... params) {
		Serializable serializable = params[0];
		WebserviceParamsObject webserviceParamsObject = (WebserviceParamsObject) serializable;
		Object object = startWebservice(webserviceParamsObject);

		return object;
	}

	private Object startWebservice(WebserviceParamsObject webserviceParamsObject) {
		String nameSpace = webserviceParamsObject.getNameSpace();
		String methodName = webserviceParamsObject.getMethodName();
		String endPoint = webserviceParamsObject.getEndPoint();
		String soapAction = webserviceParamsObject.getSoapAction();
		LinkedHashMap<String, Object> hashMap = webserviceParamsObject
				.getHashMap();

		MyWebService myWebService = new MyWebService(nameSpace, methodName,
				endPoint, soapAction);
		Object object = startService(myWebService, hashMap);
		return object;
	}

	private Object startService(MyWebService myWebService,
			LinkedHashMap<String, Object> hashMap) {
		myWebService.setSoapVresion(SoapEnvelope.VER12);
		myWebService.isDebug(true);
		myWebService.isWebServerDotnet(false);
		myWebService.addProperty(hashMap);

		Object object = myWebService.startWebService();
		return object;
	}

}