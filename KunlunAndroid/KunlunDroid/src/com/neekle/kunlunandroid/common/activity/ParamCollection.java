package com.neekle.kunlunandroid.common.activity;

import java.util.ArrayList;

public class ParamCollection {

	private final ArrayList<NameValue> mNameValueArray = new ArrayList<NameValue>();

	public static class NameValue {
		public final String name;
		public final Object value;

		public NameValue(String name, Object value) {
			this.name = name;
			this.value = value;
		}
	}

	public ArrayList<NameValue> getParamsList() {
		return mNameValueArray;
	}

	public ParamCollection put(String name, String value) {
		appendToParamsArray(name, value);
		return this;
	}

	public ParamCollection put(String name, int value) {
		appendToParamsArray(name, value);
		return this;
	}

	public ParamCollection put(String name, boolean value) {
		appendToParamsArray(name, value);
		return this;
	}

	public ParamCollection put(String name, float value) {
		appendToParamsArray(name, value);
		return this;
	}

	public ParamCollection put(String name, long value) {
		appendToParamsArray(name, value);
		return this;
	}

	public ParamCollection put(String name, double value) {
		appendToParamsArray(name, value);
		return this;
	}

	private ParamCollection appendToParamsArray(String name, Object value) {
		if (value != null) {
			NameValue nameValue = new NameValue(name, value);
			mNameValueArray.add(nameValue);
		}
		return this;
	}

	public static ParamCollection build() {
		return new ParamCollection();
	};
}
