package com.neekle.kunlunandroid.data;

public class SinglePhoneData {
	private String myJid;
	private String fromFullJid;
	private String toBareJid;
	private String sendOrRcvTag;
	private boolean isConnect;
	private String phoneNumber;
	private String dbTime;
	private String timeStamp;
	private boolean isAV;
	private String partnerJid;
	private boolean isStar;
	private boolean isSelfWifi;
	private long dbId;

	public SinglePhoneData() {

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

	public void setFromFullJid(String fromFullJid) {
		this.fromFullJid = fromFullJid;
	}

	public String getToBareJid() {
		return toBareJid;
	}

	public void setToBareJid(String toBareJid) {
		this.toBareJid = toBareJid;
	}

	public String getSendOrRcvTag() {
		return sendOrRcvTag;
	}

	public void setSendOrRcvTag(String sendOrRcvTag) {
		this.sendOrRcvTag = sendOrRcvTag;
	}

	public boolean isConnect() {
		return isConnect;
	}

	public void setConnect(boolean isConnect) {
		this.isConnect = isConnect;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDbTime() {
		return dbTime;
	}

	public void setDbTime(String dbTime) {
		this.dbTime = dbTime;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isAV() {
		return isAV;
	}

	public void setAV(boolean isAV) {
		this.isAV = isAV;
	}

	public String getPartnerJid() {
		return partnerJid;
	}

	public void setPartnerJid(String parrtnerJid) {
		this.partnerJid = parrtnerJid;
	}

	public boolean isStar() {
		return isStar;
	}

	public void setStar(boolean isStar) {
		this.isStar = isStar;
	}

	public boolean isSelfWifi() {
		return isSelfWifi;
	}

	public void setSelfWifi(boolean isSelfWifi) {
		this.isSelfWifi = isSelfWifi;
	}

	public long getDbId() {
		return dbId;
	}

	public void setDbId(long dbId) {
		this.dbId = dbId;
	}

}
