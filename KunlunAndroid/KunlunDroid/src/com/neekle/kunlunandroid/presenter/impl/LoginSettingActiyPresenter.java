package com.neekle.kunlunandroid.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.presenter.interf.ILoginSettingActivity;
import com.neekle.kunlunandroid.presenter.interf.ILoginSettingActiyPresenterCb;

public class LoginSettingActiyPresenter implements
		ILoginSettingActiyPresenterCb {

	private Context mContext;
	private ILoginSettingActivity mIView;

	public LoginSettingActiyPresenter(ILoginSettingActivity view,
			Context context) {
		mIView = view;
		mContext = context;
	}

	public void doSwitchLoginActivity(Context context,
			Class<? extends Activity> targetActivity) {
		// mMyXmppCallback.setMainActiyCallback(null);

		switchActivity(context, targetActivity, null);
	}

	public void doSwitchSoftwareSettingActivity(Context context,
			Class<? extends Activity> targetActivity) {
		// mMyXmppCallback.setMainActiyCallback(null);

		switchActivity(context, targetActivity, null);
	}

	private void switchActivity(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle) {
		Activity activity = (Activity) context;
		Intent intent = new Intent();
		intent.setClass(activity, targetActivity);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		ActivityUtil.switchTo(activity, intent);
		activity.finish();
	}

	@Override
	public void testPresenter() {

	}

}
