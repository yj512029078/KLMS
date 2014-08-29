package com.neekle.kunlunandroid.xmpp.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.neekle.kunlunandroid.data.ContactInfo;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class XmppFriend implements Serializable, Cloneable, Parcelable {

	private static final long serialVersionUID = 0x00000003;

	// 登陆者jid
	private String myJid;
	// 朋友jid
	private String friendJid;
	// 朋友名字
	private String name;
	// 朋友名字拼音
	private ArrayList<String> namePinyinList;
	// 朋友备注名字
	private String remarkName;
	// 朋友备注名字拼音
	private ArrayList<String> remarkNamePinyinList;
	// 显示名字
	private String showName;
	// 显示名字拼音
	private String showNamePinyin;
	// 朋友签名
	private String signature;
	private volatile String sex;
	// 朋友手机号
	private String mobilePhone;

	private String email;
	private String phone;
	private volatile String headPortrait;
	private volatile String backgroundPic;

	// 邀请状态（订阅状态）
	private int inviteState;
	// 是否星标
	// 基于子线程和主线程可能同时会操作它，实际上，根据我们现有的业务需求，并不存在这个情况，这里相当于是提前做周全考虑
	private volatile boolean isStarSign;

	private boolean isIgnoreMicroBlog;
	private boolean isInBlacklist;

	// 是否在线
	private boolean isOnline;
	// 所在组
	private ArrayList<String> groupList;

	// 是否是群成员
	private boolean isMemFlag;// 是否已经是群成员 标志
	private int selfPhoto;// 头像

	// 朋友在线与否状态等信息
	private XmppFriendPresenExtra friendPresenExtra;

	public XmppFriend() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMyJid() {
		return myJid;
	}

	public void setMyJid(String myJid) {
		this.myJid = myJid;
	}

	public String getFriendJid() {
		return friendJid;
	}

	public void setFriendJid(String friendJid) {
		this.friendJid = friendJid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getNamePinyinList() {
		return namePinyinList;
	}

	public void setNamePinyinList(ArrayList<String> namePinyinList) {
		this.namePinyinList = namePinyinList;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	public ArrayList<String> getRemarkNamePinyinList() {
		return remarkNamePinyinList;
	}

	public void setRemarkNamePinyinList(ArrayList<String> remarkNamePinyinList) {
		this.remarkNamePinyinList = remarkNamePinyinList;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getShowNamePinyin() {
		return showNamePinyin;
	}

	public void setShowNamePinyin(String showNamePinyin) {
		this.showNamePinyin = showNamePinyin;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public String getBackgroundPic() {
		return backgroundPic;
	}

	public void setBackgroundPic(String backgroundPic) {
		this.backgroundPic = backgroundPic;
	}

	public int getInviteState() {
		return inviteState;
	}

	public void setInviteState(int inviteState) {
		this.inviteState = inviteState;
	}

	public boolean isStarSign() {
		return isStarSign;
	}

	public void setStarSign(boolean isStarSign) {
		this.isStarSign = isStarSign;
	}

	public boolean isIgnoreMicroBlog() {
		return isIgnoreMicroBlog;
	}

	public void setIgnoreMicroBlog(boolean isIgnoreMicroBlog) {
		this.isIgnoreMicroBlog = isIgnoreMicroBlog;
	}

	public synchronized boolean isInBlacklist() {
		return isInBlacklist;
	}

	public synchronized void setInBlacklist(boolean isInBlacklist) {
		this.isInBlacklist = isInBlacklist;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public ArrayList<String> getGroupList() {
		return groupList;
	}

	public void setGroupList(ArrayList<String> groupList) {
		this.groupList = groupList;
	}

	public XmppFriendPresenExtra getFriendPresenExtra() {
		return friendPresenExtra;
	}

	public void setFriendPresenExtra(XmppFriendPresenExtra friendPresenExtra) {
		this.friendPresenExtra = friendPresenExtra;
	}

	public boolean isMember() {
		return isMemFlag;
	}

	public void setMemFlag(boolean isMemFlag) {
		this.isMemFlag = isMemFlag;
	}

	public int getSelfPhoto() {
		return selfPhoto;
	}

	public void setSelfPhoto(int selfPhoto) {
		this.selfPhoto = selfPhoto;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<XmppFriend> CREATOR = new Creator<XmppFriend>() {
		public XmppFriend createFromParcel(Parcel source) {
			XmppFriend data = new XmppFriend();
			data.myJid = source.readString();
			data.friendJid = source.readString();
			data.name = source.readString();
			data.namePinyinList = new ArrayList<String>();
			source.readList(data.namePinyinList, String.class.getClassLoader());
			data.remarkName = source.readString();
			data.remarkNamePinyinList = new ArrayList<String>();
			source.readList(data.remarkNamePinyinList,
					String.class.getClassLoader());
			data.showName = source.readString();
			data.showNamePinyin = source.readString();
			data.signature = source.readString();
			data.sex = source.readString();
			data.mobilePhone = source.readString();
			data.email = source.readString();
			data.phone = source.readString();
			data.headPortrait = source.readString();
			data.backgroundPic = source.readString();
			data.inviteState = source.readInt();

			int value = source.readInt();
			if (value == 1) {
				data.isStarSign = true;
			} else {
				data.isStarSign = false;
			}

			value = source.readInt();
			if (value == 1) {
				data.isIgnoreMicroBlog = true;
			} else {
				data.isIgnoreMicroBlog = false;
			}

			value = source.readInt();
			if (value == 1) {
				data.isInBlacklist = true;
			} else {
				data.isInBlacklist = false;
			}

			value = source.readInt();
			if (value == 1) {
				data.isOnline = true;
			} else {
				data.isOnline = false;
			}

			data.groupList = new ArrayList<String>();
			source.readList(data.groupList, String.class.getClassLoader());
			data.friendPresenExtra = source
					.readParcelable(XmppFriendPresenExtra.class
							.getClassLoader());

			return data;
		}

		public XmppFriend[] newArray(int size) {
			return new XmppFriend[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(myJid);
		dest.writeString(friendJid);
		dest.writeString(name);
		dest.writeList(namePinyinList);
		dest.writeString(remarkName);
		dest.writeList(remarkNamePinyinList);
		dest.writeString(showName);
		dest.writeString(showNamePinyin);
		dest.writeString(signature);
		dest.writeString(sex);
		dest.writeString(mobilePhone);
		dest.writeString(email);
		dest.writeString(phone);
		dest.writeString(headPortrait);
		dest.writeString(backgroundPic);
		dest.writeInt(inviteState);

		if (isStarSign) {
			dest.writeInt(1);
		} else {
			dest.writeInt(0);
		}

		if (isIgnoreMicroBlog) {
			dest.writeInt(1);
		} else {
			dest.writeInt(0);
		}

		if (isInBlacklist) {
			dest.writeInt(1);
		} else {
			dest.writeInt(0);
		}

		if (isOnline) {
			dest.writeInt(1);
		} else {
			dest.writeInt(0);
		}

		dest.writeList(groupList);
		dest.writeParcelable(friendPresenExtra, flags);

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
