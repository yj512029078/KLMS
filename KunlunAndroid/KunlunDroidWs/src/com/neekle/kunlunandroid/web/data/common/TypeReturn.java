package com.neekle.kunlunandroid.web.data.common;

import java.util.ArrayList;

import com.neekle.kunlunandroid.web.data.TypeCircle;
import com.neekle.kunlunandroid.web.data.friend.FriendInfo;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TypeReturn implements Parcelable {

	private String code;
	private String description;

	public TypeReturn() {

	}

	public TypeReturn(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static final Parcelable.Creator<TypeReturn> CREATOR = new Creator<TypeReturn>() {
		public TypeReturn createFromParcel(Parcel source) {
			TypeReturn data = new TypeReturn();
			data.code = source.readString();
			data.description = source.readString();

			return data;
		}

		public TypeReturn[] newArray(int size) {
			return new TypeReturn[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(code);
		dest.writeString(description);
	}

}
