package com.neekle.kunlunandroid.web.webservices;

import java.util.LinkedHashMap;

import org.ksoap2.SoapEnvelope;

import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;

class WebserviceBlockTask {

	public Object executeBlockTask(WebserviceParamsObject webserviceParamsObject) {
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
