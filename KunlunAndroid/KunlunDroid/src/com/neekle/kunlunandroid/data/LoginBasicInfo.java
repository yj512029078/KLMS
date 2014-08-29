package com.neekle.kunlunandroid.data;

public class LoginBasicInfo {

	private String bareJid;
	private String serverAddress;
	private String recentLoginTime;
	private String encriedPasswd;
	private boolean isRememberPasswd;
	private boolean isAutoLogin;
	private boolean isStartToMain;

	public LoginBasicInfo() {

	}

	public String getBareJid() {
		return bareJid;
	}

	public void setBareJid(String bareJid) {
		this.bareJid = bareJid;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getRecentLoginTime() {
		return recentLoginTime;
	}

	public void setRecentLoginTime(String recentLoginTime) {
		this.recentLoginTime = recentLoginTime;
	}

	public String getEncriedPasswd() {
		return encriedPasswd;
	}

	public void setEncriedPasswd(String encriedPasswd) {
		this.encriedPasswd = encriedPasswd;
	}

	public boolean isRememberPasswd() {
		return isRememberPasswd;
	}

	public void setRememberPasswd(boolean isRememberPasswd) {
		this.isRememberPasswd = isRememberPasswd;
	}

	public boolean isAutoLogin() {
		return isAutoLogin;
	}

	public void setAutoLogin(boolean isAutoLogin) {
		this.isAutoLogin = isAutoLogin;
	}

	public boolean isStartToMain() {
		return isStartToMain;
	}

	public void setStartToMain(boolean isStartToMain) {
		this.isStartToMain = isStartToMain;
	}

}
