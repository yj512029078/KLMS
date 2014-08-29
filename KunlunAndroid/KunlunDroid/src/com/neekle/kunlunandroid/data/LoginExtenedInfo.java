package com.neekle.kunlunandroid.data;

public class LoginExtenedInfo {

	private boolean isMessageNotify;
	private boolean isMessageNotifyBeep;
	private boolean isMessageNotifyVibrate;
	private boolean isAutoAcceptInvite;

	public LoginExtenedInfo() {

	}

	public boolean isMessageNotify() {
		return isMessageNotify;
	}

	public void setMessageNotify(boolean isMessageNotify) {
		this.isMessageNotify = isMessageNotify;
	}

	public boolean isMessageNotifyBeep() {
		return isMessageNotifyBeep;
	}

	public void setMessageNotifyBeep(boolean isMessageNotifyBeep) {
		this.isMessageNotifyBeep = isMessageNotifyBeep;
	}

	public boolean isMessageNotifyVibrate() {
		return isMessageNotifyVibrate;
	}

	public void setMessageNotifyVibrate(boolean isMessageNotifyVibrate) {
		this.isMessageNotifyVibrate = isMessageNotifyVibrate;
	}

	public boolean isAutoAcceptInvite() {
		return isAutoAcceptInvite;
	}

	public void setAutoAcceptInvite(boolean isAutoAcceptInvite) {
		this.isAutoAcceptInvite = isAutoAcceptInvite;
	}

}
