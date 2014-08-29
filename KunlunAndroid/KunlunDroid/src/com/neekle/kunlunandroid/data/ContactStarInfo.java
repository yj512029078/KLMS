package com.neekle.kunlunandroid.data;

public class ContactStarInfo {
	// 登陆者jid
	private String myJid;
	// 朋友jid
	private String friendJid;
	// 是否星标
	private boolean isStarSign;

	public ContactStarInfo() {

	}

	public ContactStarInfo(String myJid, String friendJid, boolean isStarSign) {
		super();
		this.myJid = myJid;
		this.friendJid = friendJid;
		this.isStarSign = isStarSign;
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

	public boolean isStarSign() {
		return isStarSign;
	}

	public void setStarSign(boolean isStarSign) {
		this.isStarSign = isStarSign;
	}

}
