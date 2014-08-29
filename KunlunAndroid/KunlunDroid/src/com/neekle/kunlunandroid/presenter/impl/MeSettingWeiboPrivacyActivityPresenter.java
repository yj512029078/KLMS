package com.neekle.kunlunandroid.presenter.impl;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingPrivacyActivity;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingWeiboPrivacyActivity;
import com.neekle.kunlunandroid.screens.AddsbookActivity;
import com.neekle.kunlunandroid.util.FileOperation;
import com.neekle.kunlunandroid.xmpp.XmppOperation;

public class MeSettingWeiboPrivacyActivityPresenter {

	private static final String BACK_SLASH = "/";
	private static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	private static final String BASE_FOLDER_RELATIVE_PATH_TO_SDCARD = "Kunlun/";
	private static final String BASE_FOLDER_ABSOLUTE_PATH = SDCARD_PATH
			+ BACK_SLASH + BASE_FOLDER_RELATIVE_PATH_TO_SDCARD;
	private static final String MICRO_BLOG = "MicroBlog";

	private Context mContext;
	private IMeSettingWeiboPrivacyActivity mIView;

	public MeSettingWeiboPrivacyActivityPresenter(
			IMeSettingWeiboPrivacyActivity view, Context context) {
		mIView = view;
		mContext = context;
	}

	public void init() {

	}

	public void doOnDeleteTempFilesClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		String positiveText = mContext.getString(R.string.confirm);
		String negativeText = mContext.getString(R.string.cancle);
		String message = mContext
				.getString(R.string.are_you_sure_to_delete_temp_files);
		builder.setMessage(message);

		builder.setPositiveButton(positiveText,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteTempFiles();
					}
				});

		builder.setNegativeButton(negativeText,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		builder.create().show();
	}

	private void deleteTempFiles() {
		String myBareJid = XmppOperation.getMyBareJid();
		String folderPath = BASE_FOLDER_ABSOLUTE_PATH + myBareJid + BACK_SLASH
				+ MICRO_BLOG + BACK_SLASH;

		File file = new File(folderPath);
		Boolean sdcardpresent = android.os.Environment
				.getExternalStorageState().equals(
						android.os.Environment.MEDIA_MOUNTED);
		if (file.exists() && sdcardpresent) {
			FileOperation.deleteFileAndFolder(file);
		}
	}

	public void doSwitchMeSettingActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingWeiboPrivacyNotSeeActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingPrivacyActivity(Context context,
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
