package com.neekle.kunlunandroid.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.data.UserGlobalPartInfo;
import com.neekle.kunlunandroid.db.TbAccountUserInfoController;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingPrivacyActivity;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;

public class MeSettingPrivacyActivityPresenter {
	private Context mContext;
	private IMeSettingPrivacyActivity mIView;

	public MeSettingPrivacyActivityPresenter(IMeSettingPrivacyActivity view,
			Context context) {
		mIView = view;
		mContext = context;
	}

	public void init() {
		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		boolean isNeedInviteToFriendWithMe = info.isNeedInviteToFriendWithMe();

		mIView.showCheck(isNeedInviteToFriendWithMe);
	}

	public void doUpdateOnRevNeedToInvitedFriendWithMeCheck(boolean isChecked) {
		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		info.setNeedInviteToFriendWithMe(isChecked);

		boolean isAutoAcceptInvite = !isChecked;
		doUpdateDbOnRevNeedToInvitedFriendWithMeCheck(isAutoAcceptInvite);
	}

	private void doUpdateDbOnRevNeedToInvitedFriendWithMeCheck(
			boolean isAutoAcceptInvite) {
		Context context = KunlunApplication.getContext();
		TbAccountUserInfoController controller = new TbAccountUserInfoController(
				context);

		XmppData xmppData = XmppData.getSingleton();
		XmppJid xmppJid = xmppData.getJid();
		if (xmppJid == null) {
			return;
		}

		int result = Constants.DEF_INT_VALUE;
		String myBareJid = xmppJid.getBare();
		try {
			controller.open();
			result = controller.updateAutoAcceptInvite(myBareJid,
					isAutoAcceptInvite);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	public void doSwitchMeSettingActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingBlacklistActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingWeiboPrivacyActivity(Context context,
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
