package com.neekle.kunlunandroid.presenter.impl;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.data.UserGlobalPartInfo;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingGeneralChatBgActivity;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingGeneralFontActivity;
import com.neekle.kunlunandroid.screens.MeSettingGeneralChatBgSelectActivity;
import com.neekle.kunlunandroid.util.DeviceInfo;
import com.neekle.kunlunandroid.util.PictureOperaterUtil;
import com.neekle.kunlunandroid.util.TimeOperater;

public class MeSettingGeneralChatBgActivityPresenter {

	private static final String INTENT_SELECTED_DRAWABLE_BG_ID = "selected_drawable_bg_id";

	private static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	private static final String PRE_PLACE_BGS_FOLDER_RELATIVE_PATH_TO_SDCARD = "KunlunDroid/img/def_bgs/";
	private static final String PRE_PLACE_BGS_FOLDER_ABSOLUTE_PATH = SDCARD_PATH
			+ "/" + PRE_PLACE_BGS_FOLDER_RELATIVE_PATH_TO_SDCARD;

	private Context mContext;
	private IMeSettingGeneralChatBgActivity mIView;

	public MeSettingGeneralChatBgActivityPresenter(
			IMeSettingGeneralChatBgActivity view, Context context) {
		mIView = view;
		mContext = context;
	}

	public void init() {

	}

	public void doOnSelectBgFromPrePlacedBgRelati(Context context,
			int requestCode) {
		Intent intent = new Intent();
		Class<? extends Activity> targetActivity = MeSettingGeneralChatBgSelectActivity.class;
		intent.setClass(context, targetActivity);

		Activity activity = (Activity) context;
		activity.startActivityForResult(intent, requestCode);
	}

	public void doOnSelectBgFromPhoneGalleryRelati(Context context,
			int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		Activity activity = (Activity) context;
		activity.startActivityForResult(intent, requestCode);
	}

	public void doOnPhotoFromPrePlacedPicked(Intent data) {

	}

	public void doOnPhotoFromGallaryPicked(Intent data) {
		String picturePath = getPhotoPath(data);

		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		String chatBgPath = getHandleBitmap(picturePath);
		info.setChatBgPath(chatBgPath);

		String hint = mContext
				.getString(com.neekle.kunlunandroid.R.string.set_ok);
		mIView.showHint(hint);
	}

	private String getHandleBitmap(String picturePath) {
		int screenWidth = DeviceInfo.getScreenWidth(mContext);
		int screenHeight = DeviceInfo.getScreenHeight(mContext);
		Bitmap bitmap = PictureOperaterUtil.getImageThumbnail(picturePath,
				screenWidth, screenHeight);

		int suffixIndex = picturePath.lastIndexOf(".");
		String suffix = Constants.EMPTY_STRING;
		if (suffixIndex != Constants.DEF_INT_VALUE) {
			suffix = picturePath.substring(suffixIndex);
		}
		String fileName = TimeOperater.getCurrentTimeAsNoSpace() + suffix;
		String destiPath = PRE_PLACE_BGS_FOLDER_ABSOLUTE_PATH + fileName;
		PictureOperaterUtil.saveBitmapAsJpeg(destiPath, bitmap);

		return destiPath;
	}

	private String getPhotoPath(Intent data) {
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = mContext.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();

		return picturePath;
	}

	public void doSwitchMeSettingActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingPrivacyActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingGeneralActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingGeneralBgSelectActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	private void switchActivityWithNoFinish(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle) {
		Activity activity = (Activity) context;
		Intent intent = new Intent();
		intent.setClass(activity, targetActivity);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		ActivityUtil.switchTo(activity, intent);
	}

	private void switchActivity(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle) {
		switchActivityWithNoFinish(context, targetActivity, bundle);

		Activity activity = (Activity) context;
		activity.finish();
	}
}
