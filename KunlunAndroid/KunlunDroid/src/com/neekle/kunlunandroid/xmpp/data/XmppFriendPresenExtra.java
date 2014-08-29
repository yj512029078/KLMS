package com.neekle.kunlunandroid.xmpp.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.xmpp.myWRAP.Presence.PresenceType;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class XmppFriendPresenExtra implements Serializable, Cloneable,
		Parcelable {

	private static final long serialVersionUID = 0x00000004;

	private String jid;
	// init it with unavailable
	private int presenceState = PresenceType.Unavailable.swigValue();
	private String presenceResource;
	private String presenceMsg;

	public XmppFriendPresenExtra() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XmppFriendPresenExtra(String jid, int presenceState,
			String presenceResource, String presenceMsg) {
		super();
		this.jid = jid;
		this.presenceState = presenceState;
		this.presenceResource = presenceResource;
		this.presenceMsg = presenceMsg;
	}

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public int getPresenceState() {
		return presenceState;
	}

	public void setPresenceState(int presenceState) {
		this.presenceState = presenceState;
	}

	public String getPresenceResource() {
		return presenceResource;
	}

	public void setPresenceResource(String presenceResource) {
		this.presenceResource = presenceResource;
	}

	public String getPresenceMsg() {
		return presenceMsg;
	}

	public void setPresenceMsg(String presenceMsg) {
		this.presenceMsg = presenceMsg;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<XmppFriendPresenExtra> CREATOR = new Creator<XmppFriendPresenExtra>() {
		public XmppFriendPresenExtra createFromParcel(Parcel source) {
			XmppFriendPresenExtra data = new XmppFriendPresenExtra();
			data.jid = source.readString();
			data.presenceState = source.readInt();
			data.presenceResource = source.readString();
			data.presenceMsg = source.readString();

			return data;
		}

		public XmppFriendPresenExtra[] newArray(int size) {
			return new XmppFriendPresenExtra[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(jid);
		dest.writeInt(presenceState);
		dest.writeString(presenceResource);
		dest.writeString(presenceMsg);
	}

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
