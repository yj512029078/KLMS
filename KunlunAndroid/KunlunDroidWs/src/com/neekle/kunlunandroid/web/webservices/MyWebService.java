package com.neekle.kunlunandroid.web.webservices;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.common.TypeReturn;
import com.neekle.kunlunandroid.web.data.friend.TypeFriend;

class MyWebService {

	private int mSoapVersion = SoapEnvelope.VER12;

	private String mNameSpace;
	private String mMethodName;
	private String mEndPoint;
	private String mSoapAction;

	private boolean mIsDebug;
	private boolean mIsDotnet;

	private LinkedHashMap<String, Object> mHashMap;

	public MyWebService(String nameSpace, String methodName, String endPoint,
			String soapAction) {
		mNameSpace = nameSpace;
		mMethodName = methodName;
		mEndPoint = endPoint;
		mSoapAction = soapAction;
	}

	public void isDebug(boolean isDebug) {
		mIsDebug = isDebug;
	}

	public boolean getIsDebug() {
		return mIsDebug;
	}

	public void setSoapVresion(int version) {
		mSoapVersion = version;
	}

	public int getSoapVersion() {
		return mSoapVersion;
	}

	public void isWebServerDotnet(boolean isDotnet) {
		mIsDotnet = isDotnet;
	}

	public boolean getIsWebServerDotnet() {
		return mIsDotnet;
	}

	public void addProperty(LinkedHashMap<String, Object> hashMap) {
		mHashMap = hashMap;
	}

	public void clearHashMap() {
		if (mHashMap != null) {
			mHashMap.clear();
			mHashMap = null;
		}
	}

	public Object startWebService() {
		SoapObject rpc = new SoapObject(mNameSpace, mMethodName);

		addArgumetnsToSoap(rpc, mHashMap);
		clearHashMap();

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				mSoapVersion);
		envelope.bodyOut = rpc;
		envelope.dotNet = mIsDotnet;
		envelope.setOutputSoapObject(rpc);
		MarshalFloat marshalFloat = new MarshalFloat();
		marshalFloat.register(envelope);
		MarshalDate marshalDate = new MarshalDate();
		marshalDate.register(envelope);

		HttpTransportSE transport = new HttpTransportSE(mEndPoint);

		transport.debug = true;
		Object[] newObject = new Object[2];
		newObject[0] = null;
		newObject[1] = mMethodName;
		try {
			// FakeX509TrustManager.allowAllSSL();
			transport.call(mSoapAction, envelope);
			String requestDump = transport.requestDump;
			String responseDump = transport.responseDump;

			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			// Object soapObject2 = (Object) envelope.getResponse();
			// Object dataObject = wrapAsObject(soapObject, mMethodName);
			// newObject[0] = dataObject;
			newObject[0] = soapObject;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return newObject;
	}

	private void addArgumetnsToSoap(SoapObject rpc,
			LinkedHashMap<String, Object> hashMap) {
		if (hashMap != null) {
			Set<Map.Entry<String, Object>> set = hashMap.entrySet();
			Iterator<Map.Entry<String, Object>> iterator = set.iterator();

			while (iterator.hasNext()) {
				Map.Entry<String, Object> maps = iterator.next();
				String key = maps.getKey();
				Object value = maps.getValue();

				addtest(rpc, key, value);
			}
		}

		hashMap = null;
	}

	private void addtest(SoapObject rpc, String key, Object value) {
		if (value instanceof TypeFriend[]) {
			TypeFriend[] typeFriends = (TypeFriend[]) value;
			int length = typeFriends.length;

			for (int i = 0; i < length; i++) {
				TypeFriend typeFriend = typeFriends[i];
				constructSerialObject(typeFriend, rpc, key, value);
			}
		} else if (value instanceof TypeFriend) {
			TypeFriend typeFriend = (TypeFriend) value;
			constructSerialObject(typeFriend, rpc, key, value);
		} else {
			rpc.addProperty(key, value);
		}
	}

	// later we will modify it
	private void constructSerialObject(TypeFriend typeFriend, SoapObject rpc,
			String key, Object value) {
		// the namespace should be watched, perhaps empty string
		String spaceString = mNameSpace;
		spaceString = "http://type.ws.kunlun.sj.com/xsd";
		SoapObject soapObject = new SoapObject(spaceString, "TypeFriend");

		String displayName = typeFriend.getDisplayName();
		String email = typeFriend.getEmail();
		String friendJid = typeFriend.getFriendJid();
		String group = typeFriend.getGroup();
		String mobile = typeFriend.getMobile();
		String phone = typeFriend.getPhone();
		String type = typeFriend.getType();

		soapObject.addProperty("displayName", displayName);
		soapObject.addProperty("email", email);
		soapObject.addProperty("friendJid", friendJid);
		soapObject.addProperty("group", group);
		soapObject.addProperty("mobile", mobile);
		soapObject.addProperty("phone", phone);
		soapObject.addProperty("type", type);

		rpc.addProperty(key, soapObject);
	}

}
