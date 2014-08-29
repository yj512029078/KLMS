package com.neekle.kunlunandroid.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 监听程序是否在前后台运行Service
 * 
 * @author xietao
 */
public class AppStatusService extends Service {
	private static final String TAG = "AppStatusService";
	private ActivityManager activityManager;
	private String packageName;
	private boolean isStop = false;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		activityManager = (ActivityManager) this
				.getSystemService(Context.ACTIVITY_SERVICE);
		packageName = this.getPackageName();
		System.out.println("启动服务");

		new Thread() {
			public void run() {
				try {
					while (!isStop) {
						Thread.sleep(1000);
						getCurrentIntent();
						if (isAppOnForeground()) {
							// Log.v(TAG, "前台运行");
						} else {
							// Log.v(TAG, "后台运行");
						}

						OnAppStatusChanged(isAppOnForeground(),
								getCurrentIntent());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

		return super.onStartCommand(intent, flags, startId);
	}

	private Intent currentIntent = null;

	public Intent getCurrentIntent() {

		packageName = this.getPackageName();
		List<RunningTaskInfo> appTask = activityManager.getRunningTasks(1);
		//
		if (appTask != null)
			if (appTask.size() > 0)
				if (appTask.get(0).topActivity.toString().contains(packageName)) {
					// Log.i("topActivity",
					// appTask.get(0).topActivity.toString());
					ComponentName cn = appTask.get(0).topActivity;
					Log.i("ComponentName", cn.getClassName());
					Intent intent = new Intent();
					intent.setClassName(packageName, cn.getClassName());

					currentIntent = intent;
					return currentIntent;
				}
		return currentIntent;
	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("停止服务");
		isStop = true;
	}

	//
	public static interface OnAppStatusChangeListener {
		/**
		 * 
		 * @param isForeground
		 *            当前程序是否在前台运行
		 * @param intent
		 *            历史Activity中的topActivity<br>
		 *            供继续当前操作使用
		 */
		public void onAppStatusChange(boolean isForeground, Intent intent);
	}

	private static OnAppStatusChangeListener mOnAppStatusChangeListener = null;

	public static void setOnAppStatusChangeListener(OnAppStatusChangeListener l) {
		if (l != null) {
			mOnAppStatusChangeListener = l;
		}
	}

	public void OnAppStatusChanged(boolean isForeground, Intent intent) {
		mOnAppStatusChangeListener.onAppStatusChange(isForeground, intent);
	}
}