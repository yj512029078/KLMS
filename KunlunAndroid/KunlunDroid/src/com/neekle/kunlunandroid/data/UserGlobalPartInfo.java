package com.neekle.kunlunandroid.data;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.web.data.TypeServerAddress;

public class UserGlobalPartInfo {

	private volatile static UserGlobalPartInfo mSingleton;

	private boolean isRevNewMsgRemind;
	private boolean isRevNewMsgSound;
	private boolean isRevNewMsgVibrate;
	private boolean isNeedInviteToFriendWithMe;

	private int chatFontSize = Constants.ChatFontSize.SMALL_SP;
	// 以下三项member和自定义bg有关
	private int chatBgImgId = Constants.DEF_INT_VALUE;
	private String chatBgPath;
	private boolean isBgInternalResource;

	private TypeServerAddress typeXmppServerAddress;
	private TypeServerAddress typeSipServerAddress;
	private String sessionId;

	private UserGlobalPartInfo() {

	}

	public static UserGlobalPartInfo getSingleton() {
		if (mSingleton == null) {
			synchronized (UserGlobalPartInfo.class) {
				if (mSingleton == null) {
					mSingleton = new UserGlobalPartInfo();
				}
			}
		}

		return mSingleton;
	}

	public synchronized boolean isRevNewMsgRemind() {
		return isRevNewMsgRemind;
	}

	public synchronized void setRevNewMsgRemind(boolean isRevNewMsgRemind) {
		this.isRevNewMsgRemind = isRevNewMsgRemind;
	}

	public synchronized boolean isRevNewMsgSound() {
		return isRevNewMsgSound;
	}

	public synchronized void setRevNewMsgSound(boolean isRevNewMsgSound) {
		this.isRevNewMsgSound = isRevNewMsgSound;
	}

	public synchronized boolean isRevNewMsgVibrate() {
		return isRevNewMsgVibrate;
	}

	public synchronized void setRevNewMsgVibrate(boolean isRevNewMsgVibrate) {
		this.isRevNewMsgVibrate = isRevNewMsgVibrate;
	}

	public synchronized boolean isNeedInviteToFriendWithMe() {
		return isNeedInviteToFriendWithMe;
	}

	public synchronized void setNeedInviteToFriendWithMe(
			boolean isNeedInviteToFriendWithMe) {
		this.isNeedInviteToFriendWithMe = isNeedInviteToFriendWithMe;
	}

	public synchronized int getChatFontSize() {
		return chatFontSize;
	}

	public synchronized void setChatFontSize(int chatFontSize) {
		this.chatFontSize = chatFontSize;
	}

	public synchronized int getChatBgDrawableId() {
		if (isBgInternalResource) {
			return chatBgImgId;
		} else {
			return Constants.DEF_INT_VALUE;
		}
	}

	public synchronized void setChatBgImgId(int chatBgImgId) {
		this.chatBgImgId = chatBgImgId;
		isBgInternalResource = true;
	}

	public synchronized void setChatBgPath(String chatBgPath) {
		this.chatBgPath = chatBgPath;
		isBgInternalResource = false;
	}

	public synchronized Bitmap getBgBitmap() {
		Context context = KunlunApplication.getContext();
		Bitmap bitmap = null;

		if (isBgInternalResource) {
			Resources res = context.getResources();
			bitmap = BitmapFactory.decodeResource(res, chatBgImgId);
		} else {
			bitmap = BitmapFactory.decodeFile(chatBgPath);
		}

		return bitmap;
	}

	public synchronized TypeServerAddress getTypeXmppServerAddress() {
		return typeXmppServerAddress;
	}

	public synchronized void setTypeXmppServerAddress(
			TypeServerAddress typeXmppServerAddress) {
		this.typeXmppServerAddress = typeXmppServerAddress;
	}

	public synchronized TypeServerAddress getTypeSipServerAddress() {
		return typeSipServerAddress;
	}

	public synchronized void setTypeSipServerAddress(
			TypeServerAddress typeSipServerAddress) {
		this.typeSipServerAddress = typeSipServerAddress;
	}

	public synchronized String getSessionId() {
		return sessionId;
	}

	public synchronized void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
