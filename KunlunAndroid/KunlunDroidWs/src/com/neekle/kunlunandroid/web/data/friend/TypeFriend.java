package com.neekle.kunlunandroid.web.data.friend;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class TypeFriend implements Serializable, Cloneable, Parcelable {

	private static final long serialVersionUID = 0x00000010;

	private String displayName;
	private String email;
	private String friendJid;
	private String group;
	private String mobile;
	private String phone;
	private String type;

	public TypeFriend() {

	}

	public TypeFriend(String displayName, String email, String friendJid,
			String group, String mobile, String phone, String type) {
		super();
		this.displayName = displayName;
		this.email = email;
		this.friendJid = friendJid;
		this.group = group;
		this.mobile = mobile;
		this.phone = phone;
		this.type = type;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFriendJid() {
		return friendJid;
	}

	public void setFriendJid(String friendJid) {
		this.friendJid = friendJid;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static final Parcelable.Creator<TypeFriend> CREATOR = new Creator<TypeFriend>() {
		public TypeFriend createFromParcel(Parcel source) {
			TypeFriend typeFriend = new TypeFriend();
			typeFriend.displayName = source.readString();
			typeFriend.email = source.readString();
			typeFriend.friendJid = source.readString();
			typeFriend.group = source.readString();
			typeFriend.mobile = source.readString();
			typeFriend.phone = source.readString();
			typeFriend.type = source.readString();

			return typeFriend;
		}

		public TypeFriend[] newArray(int size) {
			return new TypeFriend[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(displayName);
		dest.writeString(email);
		dest.writeString(friendJid);
		dest.writeString(group);
		dest.writeString(mobile);
		dest.writeString(phone);
		dest.writeString(type);
	}

	// 序列化实现深拷贝
	public Object deepCopy() {
		Object object = null;

		// 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(this);

			// 将流序列化成对象
			ByteArrayInputStream bis = new ByteArrayInputStream(
					bos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bis);
			object = ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return object;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

}
