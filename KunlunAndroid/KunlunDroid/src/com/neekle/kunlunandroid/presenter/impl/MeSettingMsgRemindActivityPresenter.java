package com.neekle.kunlunandroid.presenter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.data.UserGlobalPartInfo;
import com.neekle.kunlunandroid.db.DbConstants;
import com.neekle.kunlunandroid.db.TbAccountUserInfoController;
import com.neekle.kunlunandroid.db.TbRecordSinglePhoneController;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingMsgRemindActivity;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;

public class MeSettingMsgRemindActivityPresenter {

	private Context mContext;
	private IMeSettingMsgRemindActivity mIView;

	public MeSettingMsgRemindActivityPresenter(
			IMeSettingMsgRemindActivity view, Context context) {
		mIView = view;
		mContext = context;
	}

	public void init() {
		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		boolean isRevNewMsgRemind = info.isRevNewMsgRemind();
		boolean isSound = info.isRevNewMsgSound();
		boolean isVibrate = info.isRevNewMsgVibrate();

		mIView.showCheck(isRevNewMsgRemind, isSound, isVibrate);
	}

	public void doUpdateOnRevNewMsgRemindCheck(boolean isChecked) {
		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		info.setRevNewMsgRemind(isChecked);

		doUpdateDbOnRevNewMsgRemindCheck(isChecked);
	}

	private void doUpdateDbOnRevNewMsgRemindCheck(boolean isChecked) {
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
			result = controller.updateMessageNotify(myBareJid, isChecked);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	public void doUpdateOnSoundCheck(boolean isChecked) {
		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		info.setRevNewMsgSound(isChecked);

		doUpdateDbOnSoundCheck(isChecked);
	}

	private void doUpdateDbOnSoundCheck(boolean isChecked) {
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
			result = controller.updateMessageNotifyBeep(myBareJid, isChecked);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			controller.close();
		}
	}

	public void doUpdateOnVibrateCheck(boolean isChecked) {
		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		info.setRevNewMsgVibrate(isChecked);

		doUpdateDbOnVibrateCheck(isChecked);
	}

	private void doUpdateDbOnVibrateCheck(boolean isChecked) {
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
			result = controller.updateMessageNotifyShake(myBareJid, isChecked);
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
