package com.neekle.kunlunandroid.web.data.friend;

import java.security.PrivateKey;
import java.util.ArrayList;

import com.neekle.kunlunandroid.web.data.TypeCircle;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class FriendInfo implements Parcelable {

	private String friendJID;
	private String displayName;
	private String phone;
	private String mobile;
	private String email;
	private String addTime;
	private String autoEnterRoom;
	private ArrayList<String> groupList;
	private ArrayList<TypeCircle> circleList;

	public FriendInfo() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAutoEnterRoom() {
		return autoEnterRoom;
	}

	public void setAutoEnterRoom(String autoEnterRoom) {
		this.autoEnterRoom = autoEnterRoom;
	}

	public String getFriendJID() {
		return friendJID;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setFriendJID(String friendJID) {
		this.friendJID = friendJID;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public ArrayList<String> getGroupList() {
		return groupList;
	}

	public void setGroupList(ArrayList<String> groupList) {
		this.groupList = groupList;
	}

	public ArrayList<TypeCircle> getCircleList() {
		return circleList;
	}

	public void setCircleList(ArrayList<TypeCircle> circleList) {
		this.circleList = circleList;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static final Parcelable.Creator<FriendInfo> CREATOR = new Creator<FriendInfo>() {
		public FriendInfo createFromParcel(Parcel source) {
			FriendInfo friendInfo = new FriendInfo();
			friendInfo.friendJID = source.readString();
			friendInfo.displayName = source.readString();
			friendInfo.phone = source.readString();
			friendInfo.mobile = source.readString();
			friendInfo.email = source.readString();
			friendInfo.addTime = source.readString();
			friendInfo.autoEnterRoom = source.readString();
			friendInfo.groupList = new ArrayList<String>();
			source.readList(friendInfo.groupList, String.class.getClassLoader());
			friendInfo.circleList = new ArrayList<TypeCircle>();
			source.readList(friendInfo.circleList,
					TypeCircle.class.getClassLoader());

			return friendInfo;
		}

		public FriendInfo[] newArray(int size) {
			return new FriendInfo[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(friendJID);
		dest.writeString(displayName);
		dest.writeString(phone);
		dest.writeString(mobile);
		dest.writeString(email);
		dest.writeString(addTime);
		dest.writeString(autoEnterRoom);
		dest.writeList(groupList);
		dest.writeList(circleList);
	}

}
