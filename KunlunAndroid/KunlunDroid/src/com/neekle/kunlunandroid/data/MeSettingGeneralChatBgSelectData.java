package com.neekle.kunlunandroid.data;

import android.graphics.Bitmap;

public class MeSettingGeneralChatBgSelectData {
	private Bitmap bitmap;
	private boolean isSelected;

	public MeSettingGeneralChatBgSelectData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MeSettingGeneralChatBgSelectData(Bitmap bitmap, boolean isSelected) {
		super();
		this.bitmap = bitmap;
		this.isSelected = isSelected;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
