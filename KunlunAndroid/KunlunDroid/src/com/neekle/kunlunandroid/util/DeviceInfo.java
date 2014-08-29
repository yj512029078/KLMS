package com.neekle.kunlunandroid.util;

import java.io.File;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class DeviceInfo {

	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();
		return deviceId;
	}

	public static String getPhoneNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = tm.getLine1Number();
		return phoneNumber;
	}

	public static int getScreenWidth(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		int width = dm.widthPixels;
		return width;
	}

	public static int getScreenHeight(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		int width = dm.heightPixels;
		return width;
	}

	public static String getSdcardPath() {
		File file = Environment.getExternalStorageDirectory();
		String path = file.getAbsolutePath();

		return path;
	}
}
