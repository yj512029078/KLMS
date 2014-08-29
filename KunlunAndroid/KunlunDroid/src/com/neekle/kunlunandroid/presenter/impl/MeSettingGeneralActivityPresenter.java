package com.neekle.kunlunandroid.presenter.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.db.TbFriendsController;
import com.neekle.kunlunandroid.db.TbRecordChatSumController;
import com.neekle.kunlunandroid.db.TbRecordMultiChatController;
import com.neekle.kunlunandroid.db.TbRecordSingleChatController;
import com.neekle.kunlunandroid.db.TbUINotificationController;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingGeneralActivity;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingPrivacyActivity;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.xmpp.XmppOperation;

public class MeSettingGeneralActivityPresenter {
	private Context mContext;
	private IMeSettingGeneralActivity mIView;

	public MeSettingGeneralActivityPresenter(IMeSettingGeneralActivity view,
			Context context) {
		mIView = view;
		mContext = context;
	}

	public void doOnClearHisRecordClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		String positiveText = mContext.getString(R.string.confirm);
		String negativeText = mContext.getString(R.string.cancle);
		String message = mContext
				.getString(R.string.are_you_sure_to_clear_his_record);
		builder.setMessage(message);

		builder.setPositiveButton(positiveText,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						clearChatRecordRelated();
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

	private void clearChatRecordRelated() {
		String myBareJid = XmppOperation.getMyBareJid();
		Context context = KunlunApplication.getContext();

		TbRecordSingleChatController tbRecordSingleChatController = new TbRecordSingleChatController(
				context);
		try {
			tbRecordSingleChatController.open();
			tbRecordSingleChatController.delete(myBareJid);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			tbRecordSingleChatController.close();
		}

		TbRecordMultiChatController tbRecordMultiChatController = new TbRecordMultiChatController(
				context);
		try {
			tbRecordMultiChatController.open();
			tbRecordMultiChatController.delete(myBareJid);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			tbRecordMultiChatController.close();
		}

		TbRecordChatSumController tbRecordChatSumController = new TbRecordChatSumController(
				context);
		try {
			tbRecordChatSumController.open();
			tbRecordChatSumController.delete(myBareJid);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			tbRecordChatSumController.close();
		}

		TbUINotificationController tbUINotificationController = new TbUINotificationController(
				context);
		try {
			tbUINotificationController.open();
			tbUINotificationController.delete(myBareJid);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			tbUINotificationController.close();
		}
	}

	public void doSwitchMeSettingActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingGeneralFontActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingGeneralChatBgActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchOptionFeatureActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivityWithNoFinish(context, targetActivity, null);
	}

	public void doSwitchWebKunlunLoginActivity(Context context,
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
