package com.neekle.kunlunandroid.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingAboutTeamActivity;

public class MeSettingAboutTeamActivityPresenter {
	private Context mContext;
	private IMeSettingAboutTeamActivity mIView;

	public MeSettingAboutTeamActivityPresenter(
			IMeSettingAboutTeamActivity view, Context context) {
		mIView = view;
		mContext = context;
	}

	public void doSwitchMeSettingActivity(Context context,
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
