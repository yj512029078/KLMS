package com.neekle.kunlunandroid.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.db.TbAccountUserInfoController;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingActivity;
import com.neekle.kunlunandroid.services.AutoConnectService;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.XmppService;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;

public class MeSettingActivityPresenter {
	private Context mContext;
	private IMeSettingActivity mIView;

	public MeSettingActivityPresenter(IMeSettingActivity view, Context context) {
		mIView = view;
		mContext = context;
	}

	public void doLogout() {
		AutoConnectService.stopAutoConnectService(mContext);

		// 考虑下，登出是子线程操作，在子线程没有执行完的时刻，如下代码已执行，是否有问题
		// 后面要做默认不退出，此代码也不应该在此处
		XmppService xmppService = XmppService.getSingleton();
		xmppService.logout();

		XmppData xmppData = XmppData.getSingleton();
		XmppJid xmppJid = xmppData.getJid();
		if (xmppJid == null) {
			return;
		}

		String myBareJid = xmppJid.getBare();
		TbAccountUserInfoController controller = new TbAccountUserInfoController(
				mContext);
		try {
			controller.open();
			controller.updateLoginTag(myBareJid, false);
		} catch (Exception e) {

		} finally {
			controller.close();
		}
	}

	public void doSwitchMainScreenActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingMsgRemindActivity(Context context,
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

	public void doSwitchLoginActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingAboutTeamRelati(Context context,
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
