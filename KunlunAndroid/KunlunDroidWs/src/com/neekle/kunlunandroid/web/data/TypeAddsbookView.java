package com.neekle.kunlunandroid.web.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TypeAddsbookView implements Parcelable {

	private String addbookId;
	private String displayName;
	private String friendJid;
	private float index;
	private int isPublic;

	public TypeAddsbookView() {
		super();
	}

	public TypeAddsbookView(String addbookId, String displayName,
			String friendJid, float index, int isPublic) {
		super();
		this.addbookId = addbookId;
		this.displayName = displayName;
		this.friendJid = friendJid;
		this.index = index;
		this.isPublic = isPublic;
	}

	public String getAddbookId() {
		return addbookId;
	}

	public void setAddbookId(String addbookId) {
		this.addbookId = addbookId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFriendJid() {
		return friendJid;
	}

	public void setFriendJid(String friendJid) {
		this.friendJid = friendJid;
	}

	public float getIndex() {
		return index;
	}

	public void setIndex(float index) {
		this.index = index;
	}

	public int getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(int isPublic) {
		this.isPublic = isPublic;
	}

	public static final Parcelable.Creator<TypeAddsbookView> CREATOR = new Creator<TypeAddsbookView>() {
		public TypeAddsbookView createFromParcel(Parcel source) {
			TypeAddsbookView data = new TypeAddsbookView();
			data.addbookId = source.readString();
			data.displayName = source.readString();
			data.friendJid = source.readString();
			data.index = source.readFloat();
			data.isPublic = source.readInt();

			return data;
		}

		public TypeAddsbookView[] newArray(int size) {
			return new TypeAddsbookView[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(addbookId);
		dest.writeString(displayName);
		dest.writeString(friendJid);
		dest.writeFloat(index);
		dest.writeInt(isPublic);
	}

}
