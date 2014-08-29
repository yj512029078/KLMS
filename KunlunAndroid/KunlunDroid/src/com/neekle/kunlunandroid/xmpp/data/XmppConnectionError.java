package com.neekle.kunlunandroid.xmpp.data;

public class XmppConnectionError {

	private int swigValue;
	private String error;

	public XmppConnectionError() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XmppConnectionError(int swigValue, String error) {
		super();
		this.swigValue = swigValue;
		this.error = error;
	}

	public int getSwigValue() {
		return swigValue;
	}

	public void setSwigValue(int swigValue) {
		this.swigValue = swigValue;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
