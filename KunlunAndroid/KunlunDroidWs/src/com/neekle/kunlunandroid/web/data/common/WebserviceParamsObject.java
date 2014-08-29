package com.neekle.kunlunandroid.web.data.common;

import java.io.Serializable;
import java.util.LinkedHashMap;

//Later modify Serializable interface to Parcelable
public class WebserviceParamsObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mNameSpace;
	private String mMethodName;
	private String mEndPoint;
	private String mSoapAction;
	// For Map, it has implemented Serializable interface
	private LinkedHashMap<String, Object> mHashMap;

	public WebserviceParamsObject(String mNameSpace, String mMethodName,
			String mEndPoint, String mSoapAction,
			LinkedHashMap<String, Object> mHashMap) {
		super();

		this.mNameSpace = mNameSpace;
		this.mMethodName = mMethodName;
		this.mEndPoint = mEndPoint;
		this.mSoapAction = mSoapAction;
		this.mHashMap = mHashMap;
	}

	public String getNameSpace() {
		return mNameSpace;
	}

	public void setNameSpace(String mNameSpace) {
		this.mNameSpace = mNameSpace;
	}

	public String getMethodName() {
		return mMethodName;
	}

	public void setMethodName(String mMethodName) {
		this.mMethodName = mMethodName;
	}

	public String getEndPoint() {
		return mEndPoint;
	}

	public void setEndPoint(String mEndPoint) {
		this.mEndPoint = mEndPoint;
	}

	public String getSoapAction() {
		return mSoapAction;
	}

	public void setSoapAction(String mSoapAction) {
		this.mSoapAction = mSoapAction;
	}

	public LinkedHashMap<String, Object> getHashMap() {
		return mHashMap;
	}

	public void setHashMap(LinkedHashMap<String, Object> mHashMap) {
		this.mHashMap = mHashMap;
	}

}
