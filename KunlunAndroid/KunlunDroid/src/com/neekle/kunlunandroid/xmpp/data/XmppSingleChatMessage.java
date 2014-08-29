package com.neekle.kunlunandroid.xmpp.data;

import java.util.ArrayList;

import org.xmpp.myWRAP.Message;
import org.xmpp.myWRAP.Message.MessageType;

import com.neekle.kunlunandroid.xmpp.common.XmppConstants;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class XmppSingleChatMessage implements Parcelable {
	private String myJid;
	private String fromFullJid;
	private String fromName;
	private String toBareJid;
	private String toName;
	private String sendOrRcvTag;
	private String messageId;
	private int messageType;
	private String threadId;
	private String timeStamp;
	private String geoloc;
	private String dbTime;
	private String bodyType;
	private String msgBody;
	private int infoType;

	private String msgText;// 纯文本

	private String fromPhotoPath;
	private String toPhotoPath;
	private String lang;
	private String subject;
	private String partnerJid;

	public XmppSingleChatMessage() {

	}

	public String getMyJid() {
		return myJid;
	}

	public void setMyJid(String myJid) {
		this.myJid = myJid;
	}

	public String getFromFullJid() {
		return fromFullJid;
	}

	public void setFromFullJid(String fromJid) {
		this.fromFullJid = fromJid;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getToBareJid() {
		return toBareJid;
	}

	public void setToBareJid(String toJid) {
		this.toBareJid = toJid;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getSendOrRcvTag() {
		return sendOrRcvTag;
	}

	public void setSendOrRcvTag(String sendOrRcvTag) {
		this.sendOrRcvTag = sendOrRcvTag;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getGeoloc() {
		return geoloc;
	}

	public void setGeoloc(String geoloc) {
		this.geoloc = geoloc;
	}

	public String getDbTime() {
		return dbTime;
	}

	public void setDbTime(String dbTime) {
		this.dbTime = dbTime;
	}

	public String getBodyType() {
		return bodyType;
	}

	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}

	public int getInfoType() {
		return infoType;
	}

	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}

	public String getFromPhotoPath() {
		return fromPhotoPath;
	}

	public void setFromPhotoPath(String fromPhotoPath) {
		this.fromPhotoPath = fromPhotoPath;
	}

	public String getToPhotoPath() {
		return toPhotoPath;
	}

	public void setToPhotoPath(String toPhotoPath) {
		this.toPhotoPath = toPhotoPath;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPartnerJid() {
		return partnerJid;
	}

	public void setPartnerJid(String partnerJid) {
		this.partnerJid = partnerJid;
	}

	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	public static Parcelable.Creator<XmppSingleChatMessage> getCreator() {
		return CREATOR;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<XmppSingleChatMessage> CREATOR = new Creator<XmppSingleChatMessage>() {
		public XmppSingleChatMessage createFromParcel(Parcel source) {
			XmppSingleChatMessage data = new XmppSingleChatMessage();
			data.myJid = source.readString();
			data.fromFullJid = source.readString();
			data.fromName = source.readString();
			data.toBareJid = source.readString();
			data.toName = source.readString();
			data.sendOrRcvTag = source.readString();
			data.messageId = source.readString();
			data.messageType = source.readInt();
			data.threadId = source.readString();
			data.timeStamp = source.readString();
			data.geoloc = source.readString();
			data.dbTime = source.readString();
			data.bodyType = source.readString();
			data.msgBody = source.readString();
			data.infoType = source.readInt();
			data.fromPhotoPath = source.readString();
			data.toPhotoPath = source.readString();
			data.lang = source.readString();
			data.subject = source.readString();
			data.partnerJid = source.readString();
			data.msgText = source.readString();

			return data;
		}

		public XmppSingleChatMessage[] newArray(int size) {
			return new XmppSingleChatMessage[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(myJid);
		dest.writeString(fromFullJid);
		dest.writeString(fromName);
		dest.writeString(toBareJid);
		dest.writeString(toName);
		dest.writeString(sendOrRcvTag);
		dest.writeString(messageId);
		dest.writeInt(messageType);
		dest.writeString(threadId);
		dest.writeString(timeStamp);
		dest.writeString(geoloc);
		dest.writeString(dbTime);
		dest.writeString(bodyType);
		dest.writeString(msgBody);
		dest.writeInt(infoType);
		dest.writeString(fromPhotoPath);
		dest.writeString(toPhotoPath);
		dest.writeString(lang);
		dest.writeString(subject);
		dest.writeString(partnerJid);
		dest.writeString(msgText);
	}

}
