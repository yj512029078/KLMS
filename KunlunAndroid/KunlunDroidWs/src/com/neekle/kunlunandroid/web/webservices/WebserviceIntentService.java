package com.neekle.kunlunandroid.web.webservices;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.ksoap2.SoapEnvelope;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;

public class WebserviceIntentService extends IntentService {

	private static ISoapReceivedCallback mISoapReceivedCallback;

	private static final String INTENT_KEY_WEBSERVICE_PARAMS_OBJECT = "intent_key_webservice_params_object";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onBind(intent);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public void setCallback(ISoapReceivedCallback iSoapReceivedCallback) {
		mISoapReceivedCallback = iSoapReceivedCallback;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void setIntentRedelivery(boolean enabled) {
		// TODO Auto-generated method stub
		super.setIntentRedelivery(false);
	}

	public WebserviceIntentService(String name) {
		super(name);
	}

	public WebserviceIntentService() {
		super("WebserviceIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		WebserviceParamsObject webserviceParamsObject = getWebserviceParamsObject(intent);
		Object object = startWebservice(webserviceParamsObject);
		mISoapReceivedCallback.onWsResultNotify(object);
	}

	private WebserviceParamsObject getWebserviceParamsObject(Intent intent) {
		Serializable serializable = intent
				.getSerializableExtra(INTENT_KEY_WEBSERVICE_PARAMS_OBJECT);
		WebserviceParamsObject webserviceParamsObject = (WebserviceParamsObject) serializable;

		return webserviceParamsObject;
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
