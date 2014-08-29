package com.neekle.kunlunandroid.presenter.common;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.data.UserGlobalPartInfo;
import com.neekle.kunlunandroid.screens.KunlunApplication;
import com.neekle.kunlunandroid.screens.MainScreenActivity;

public class NotificationOperation {

	// 备注：这部分还有个比较严重的BUG，具体是关于点击后，跳转到首屏的

	private static final int NEW_MSG_NOTIFICATION_ID = 1024;

	private Context mContext;
	private NotificationManager mNotificationManager;

	public NotificationOperation() {
		mContext = KunlunApplication.getContext();
		mNotificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void doShowNotification() {
		UserGlobalPartInfo info = UserGlobalPartInfo.getSingleton();
		boolean isNewMsgRemind = info.isRevNewMsgRemind();
		if (!isNewMsgRemind) {
			return;
		}

		boolean isVibrate = info.isRevNewMsgVibrate();
		boolean isSound = info.isRevNewMsgSound();

		String title = mContext.getString(R.string.new_msg_hint);
		String content = mContext.getString(R.string.you_have_new_msg);
		String tickerText = mContext.getString(R.string.new_msg);

		PendingIntent intent = getIntent2(MainScreenActivity.class);

		showIntentActivityNotify(title, content, tickerText, isVibrate,
				isSound, intent);
	}

	private PendingIntent getIntent(Class<? extends Activity> desiredActivity) {
		Intent appIntent = new Intent();
		// appIntent.setAction(Intent.ACTION_MAIN);
		// appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		appIntent.setClass(mContext, desiredActivity);
		// appIntent.setComponent(new ComponentName(mContext.getPackageName(),
		// MainScreenActivity.class);
		appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
				appIntent, 0);

		return contentIntent;
	}

	private PendingIntent getIntent2(Class<? extends Activity> desiredActivity) {
		Intent intent = new Intent();
		intent.setClass(mContext, desiredActivity);
		// Intent.FLAG_ACTIVITY_SINGLE_TOP;
		// Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
		// Intent.FLAG_ACTIVITY_NEW_TASK
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		return pendingIntent;
	}

	public void showIntentActivityNotify(String title, String content,
			String tickerText, boolean isVibrate, boolean isSound,
			PendingIntent intent) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				mContext);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setTicker(tickerText);

		long time = System.currentTimeMillis();
		builder.setWhen(time);

		builder.setOngoing(false);

		int icon = R.drawable.cm_ic_launcher;
		builder.setSmallIcon(icon);

		int defaults = getDefaults(isVibrate, isSound);
		if (defaults != Constants.DEF_INT_VALUE) {
			builder.setDefaults(defaults);
		}

		builder.setContentIntent(intent);
		builder.setAutoCancel(true);

		Notification notification = builder.build();
		mNotificationManager.notify(NEW_MSG_NOTIFICATION_ID, notification);
	}

	private int getDefaults(boolean isVibrate, boolean isSound) {
		int defaults = Constants.DEF_INT_VALUE;

		if (isVibrate && isSound) {
			defaults = Notification.DEFAULT_VIBRATE
					| Notification.DEFAULT_SOUND;
		} else if (isVibrate) {
			defaults = Notification.DEFAULT_VIBRATE;
		} else if (isSound) {
			defaults = Notification.DEFAULT_SOUND;
		}

		return defaults;
	}

}
