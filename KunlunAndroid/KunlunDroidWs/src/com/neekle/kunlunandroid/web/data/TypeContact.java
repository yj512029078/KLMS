package com.neekle.kunlunandroid.web.data;

public class TypeContact {

	private String jid;
	private String points;

	public TypeContact() {
		super();
	}

	public TypeContact(String jid, String points) {
		super();
		this.jid = jid;
		this.points = points;
	}

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

}
