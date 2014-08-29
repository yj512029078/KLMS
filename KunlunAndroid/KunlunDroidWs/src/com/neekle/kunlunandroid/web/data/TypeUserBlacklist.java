package com.neekle.kunlunandroid.web.data;

public class TypeUserBlacklist {
	private String addTime;
	private String contactJid;

	public TypeUserBlacklist() {

	}

	public TypeUserBlacklist(String addTime, String contactJid) {
		super();
		this.addTime = addTime;
		this.contactJid = contactJid;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	public String getContactJid() {
		return contactJid;
	}

	public void setContactJid(String contactJid) {
		this.contactJid = contactJid;
	}

}
