package com.neekle.kunlunandroid.webservices.test;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;

import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.common.TypeReturn;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;
import com.neekle.kunlunandroid.web.util.Logger;

import android.test.AndroidTestCase;
import android.util.Log;

public class GeneralUnitTest extends AndroidTestCase {

	private UnitTestPresenter mPresenter;

	private static final String TAG = "GeneralUnitTest";

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	@Override
	public void testAndroidTestCaseSetupProperly() {
		// TODO Auto-generated method stub
		super.testAndroidTestCaseSetupProperly();
	}

	public GeneralUnitTest() {
		mPresenter = new UnitTestPresenter();
	}

	public void testLoginWebservice() {
		Logger.i(TAG, "<<<testLoginWebservice>>> start");

		String nameSpace = WebserviceConstants.NAME_SPACE;
		String methodName = WebserviceConstants.GLOCAL_METHOD_LOGIN;
		String endPoint = WebserviceConstants.END_POINT_GLOCAL;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;
		String soapAction = prefix + methodName;

		String jid = "test001@server08.com";
		LinkedHashMap<String, Object> hashMap = getArgus(jid);
		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		Object object = mPresenter.action(webserviceParamsObject);
		Object resolvedObject = mPresenter.resolveObject(object);
		outputInfo(resolvedObject);

		Object dataObject = mPresenter.getDataObject(resolvedObject);
		assertNotNull(dataObject);

		Logger.i(TAG, "<<<testLoginWebservice>>> end");
	}

	private void outputInfo(Object object) {
		Object dataObject = Array.get(object, 0);
		Object methodObject = Array.get(object, 1);
		String method = (String) methodObject;

		String msg = null;

		if (dataObject instanceof TypeReturn) {
			TypeReturn typeReturn = (TypeReturn) dataObject;

			String codeString = typeReturn.getCode();
			String descriString = typeReturn.getDescription();
			msg = "codeString:" + codeString + "  descriString:" + descriString
					+ "   method:" + method;

			Log.i(TAG, msg);
		} else if (dataObject instanceof String) {
			String string = (String) dataObject;
			msg = "string:" + string + "   method:" + method;
			Log.i(TAG, msg);
		}

	}

	private void assertRightCode(Object object) {
		Object dataObject = Array.get(object, 0);
		Object methodObject = Array.get(object, 1);
		String method = (String) methodObject;

		if (dataObject instanceof TypeReturn) {
			TypeReturn typeReturn = (TypeReturn) dataObject;

			String codeString = typeReturn.getCode();
			String descriString = typeReturn.getDescription();
			String msg = "codeString:" + codeString + "  descriString:"
					+ descriString + "   method:" + method;

			int code = Integer.valueOf(codeString);
			int expectedCode = WebserviceConstants.WERBSERVICE_RESULT_SUCCESS_CODE;
			assertEquals(msg, expectedCode, code);
		}
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
}
