package com.neekle.kunlunandroid.web.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author kevin
 * 
 */
public class TypeValidateCode implements Parcelable {

	private String validateCodeIDString;
	private byte[] picture;

	public TypeValidateCode() {

	}

	public TypeValidateCode(String validateCodeIDString, byte[] picture) {

		this.validateCodeIDString = validateCodeIDString;
		this.picture = picture;
	}

	public String getValidateCodeIDString() {
		return validateCodeIDString;
	}

	public void setValidateCodeIDString(String validateCodeIDString) {

		this.validateCodeIDString = validateCodeIDString;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(validateCodeIDString);
		dest.writeByteArray(picture);

	}

}
