package com.neekle.kunlunandroid.presenter.common;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.neekle.kunlunandroid.R;

public class NotificationService extends Service {

	private static Context mContext;

	private String ns = Context.NOTIFICATION_SERVICE;

	private static NotificationManager mNotificationManager = null;
	private static Notification mNotification = null;

	// Notification跳转
	private static PendingIntent pendingIntent = null;
	private static int mNotificationId = 100;
	// ---Message
	private static Message msg = null;
	private static MHandler mHandler = null;
	// ---View
	public static final int NOTIFICATION_NORMAL = 0x00000011;
	public static final int NOTIFICATION_LARGE = 0x00000012;
	public static final int NOTIFICATION_REMOTEVIEWS = 0x00000013;
	// --- Event
	public static final int NOTIFICATION_CANCEL = 0x00000101;
	public static final int NOTIFICATION_SHOW = 0x00000102;
	public static final int NOTIFICATION_RUNNING_BACKGROUND = Notification.FLAG_NO_CLEAR
			| NOTIFICATION_NORMAL;

	public static final boolean isOpen = false;

	public static void setMsg(Message msg) {
		NotificationService.msg = msg;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mContext = this;
		if (mNotificationManager == null) {
			mNotificationManager = (NotificationManager) getSystemService(ns);
		}
		if (msg != null)
			handleNotification(msg);
		return super.onStartCommand(intent, flags, startId);
	}

	public static void handleNotification(Message msg) {
		if (mNotification == null) {
			mNotification = new Notification();
			mNotification.icon = R.drawable.ic_launcher;
		}
		int flag = 0;
		if (msg != null && !isOpen) {
			flag = msg.what;
		}

		switch (flag) {
		case 0x00000000:
			break;
		case NOTIFICATION_CANCEL:
			mNotificationManager.cancel(mNotificationId);
			break;
		case NOTIFICATION_SHOW:
			mNotification.flags = Notification.FLAG_NO_CLEAR;
			mHandler = (MHandler) msg.getTarget();

			if (mHandler != null)
				setNotificationContent(mContext, mHandler.getContentTitle(),
						mHandler.getContentText(), mHandler.getTargetClass());
			break;
		case NOTIFICATION_RUNNING_BACKGROUND:
			mNotification.flags = Notification.FLAG_NO_CLEAR;
			mHandler = (MHandler) msg.getTarget();

			if (mHandler != null)
				setNotificationContent(mContext, mContext.getResources()
						.getString(R.string.app_name), mContext.getResources()
						.getString(R.string.program_running_background),
						mHandler.getTargetClass());
		default:
			break;

		}

		if (msg != null) {
			msg.recycle();
		}
	}

	public static void setNotificationContent(CharSequence contentTitle,
			CharSequence contentText, Intent notificationIntent) {
		pendingIntent = PendingIntent.getActivity(mContext, mNotificationId,
				notificationIntent, 0);

		mNotification.setLatestEventInfo(mContext, contentTitle, contentText,
				pendingIntent);
		mNotificationManager.notify(mNotificationId, mNotification);
	}

	public static void setNotificationContent(Context context,
			CharSequence contentTitle, CharSequence contentText,
			Class<? extends Activity> targetClass) {
		Intent notificationIntent = new Intent(mContext, targetClass);
		pendingIntent = PendingIntent.getActivity(mContext, mNotificationId,
				notificationIntent, 0);

		mNotification.setLatestEventInfo(mContext, contentTitle, contentText,
				pendingIntent);
		mNotificationManager.notify(mNotificationId, mNotification);
	}

	@Override
	public void onDestroy() {
		Message msg = new Message();
		msg.what = NOTIFICATION_CANCEL;
		handleNotification(msg);
		pendingIntent = null;

		mNotification = null;
		mNotificationManager = null;
		mContext = null;
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 消息管理
	 * 
	 * @author xietao
	 * 
	 */
	public static class MHandler extends Handler {
		private Context mContext = null;
		private Class<? extends Activity> targetClass = null;

		// Notification普通视图参数
		private CharSequence contentTitle = null;
		private CharSequence contentText = null;

		// Notification普通视图参数

		public MHandler() {
			super();
			// TODO Auto-generated constructor stub
		}

		public CharSequence getContentTitle() {
			return contentTitle;
		}

		public void setContentTitle(CharSequence contentTitle) {
			this.contentTitle = contentTitle;
		}

		public CharSequence getContentText() {
			return contentText;
		}

		public void setContentText(CharSequence contentText) {
			this.contentText = contentText;
		}

		public Context getmContext() {
			return mContext;
		}

		public Class<? extends Activity> getTargetClass() {
			return targetClass;
		}

		public MHandler(Context context) {
			super();
			this.mContext = context;
		}

		public void setTargetClass(Class<? extends Activity> targetClass) {
			this.targetClass = targetClass;
		}

	}
}
