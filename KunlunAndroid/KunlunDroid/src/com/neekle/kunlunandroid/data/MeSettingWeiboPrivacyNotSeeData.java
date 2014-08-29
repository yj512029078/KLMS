package com.neekle.kunlunandroid.data;

import android.graphics.Bitmap;

public class MeSettingWeiboPrivacyNotSeeData {
	private Bitmap bitmap;
	private String name;
	private boolean isDeleteIconVisible;
	private int itemType;
	private String bareJid;

	public MeSettingWeiboPrivacyNotSeeData() {

	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDeleteIconVisible() {
		return isDeleteIconVisible;
	}

	public void setDeleteIconVisible(boolean isDeleteIconVisible) {
		this.isDeleteIconVisible = isDeleteIconVisible;
	}

	public int getItemType() {
		return itemType;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public String getBareJid() {
		return bareJid;
	}

	public void setBareJid(String bareJid) {
		this.bareJid = bareJid;
	}

}
