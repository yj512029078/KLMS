package com.neekle.kunlunandroid.data;

public class CellPhoneContactData {
	private int userState; // ...
	private String name; // ...
	private boolean isStar;
	private boolean isPhoneWifi;
	private int phoneState;
	private String phoneNumber;
	private String time;
	private String myJid;
	private String fromFullJid;
	private String toBareJid;
	private String partnerJid;
	private long dbId;

	public CellPhoneContactData() {

	}

	public int getUserState() {
		return userState;
	}

	public void setUserState(int userState) {
		this.userState = userState;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStar() {
		return isStar;
	}

	public void setStar(boolean isStar) {
		this.isStar = isStar;
	}

	public boolean isPhoneWifi() {
		return isPhoneWifi;
	}

	public void setPhoneWifi(boolean isPhoneWifi) {
		this.isPhoneWifi = isPhoneWifi;
	}

	public int getPhoneState() {
		return phoneState;
	}

	public void setPhoneState(int phoneState) {
		this.phoneState = phoneState;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	public String getPartnerJid() {
		return partnerJid;
	}

	public void setPartnerJid(String parrtnerJid) {
		this.partnerJid = parrtnerJid;
	}

	public long getDbId() {
		return dbId;
	}

	public void setDbId(long dbId) {
		this.dbId = dbId;
	}

}
