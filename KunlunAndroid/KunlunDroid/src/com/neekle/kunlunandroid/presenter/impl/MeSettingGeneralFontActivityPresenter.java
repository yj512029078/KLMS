package com.neekle.kunlunandroid.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.data.UserGlobalPartInfo;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingGeneralFontActivity;

public class MeSettingGeneralFontActivityPresenter {
	private Context mContext;
	private IMeSettingGeneralFontActivity mIView;

	public MeSettingGeneralFontActivityPresenter(
			IMeSettingGeneralFontActivity view, Context context) {
		mIView = view;
		mContext = context;
	}

	public void init() {

	}

	public int getInitFontSize() {
		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		return info.getChatFontSize();
	}

	public void doSaveFontPreference(int fontSize) {
		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		info.setChatFontSize(fontSize);
		// ...
		// 目前是只保存在内存中的，后续可能需要持久化到数据库
	}

	public void doSwitchMeSettingActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingGeneralActivity(Context context,
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
