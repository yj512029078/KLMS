package com.neekle.kunlunandroid.web.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TypeCircle implements Parcelable {

	private String id;
	private String name;
	private String templetId;
	private String comment;

	public TypeCircle() {

	}

	public TypeCircle(String id, String name, String templetId, String comment) {
		super();
		this.id = id;
		this.name = name;
		this.templetId = templetId;
		this.comment = comment;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTempletId() {
		return templetId;
	}

	public void setTempletId(String templetId) {
		this.templetId = templetId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public static final Parcelable.Creator<TypeCircle> CREATOR = new Creator<TypeCircle>() {
		public TypeCircle createFromParcel(Parcel source) {
			TypeCircle typeCircle = new TypeCircle();
			typeCircle.id = source.readString();
			typeCircle.name = source.readString();
			typeCircle.templetId = source.readString();
			typeCircle.comment = source.readString();

			return typeCircle;
		}

		public TypeCircle[] newArray(int size) {
			return new TypeCircle[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(templetId);
		dest.writeString(comment);
	}

}
