package com.neekle.kunlunandroid.util;

import android.util.Log;

public class LogUtil {

	private static boolean isLogTurnOn = true;

	public static void log(int level, String tag, String msg) {

		switch (level) {
		case Log.ASSERT: {
			logAssert(tag, msg);
			break;
		}
		case Log.ERROR: {
			logError(tag, msg);
			break;
		}
		case Log.DEBUG: {
			logDebug(tag, msg);
			break;
		}
		case Log.INFO: {
			logInfo(tag, msg);
			break;
		}
		case Log.VERBOSE: {
			logVerbose(tag, msg);
			break;
		}
		case Log.WARN: {
			logWarn(tag, msg);
			break;
		}
		default: {
			break;
		}

		}
	}

	public static void logInfo(String tag, String msg) {
		if (isLogTurnOn) {
			Log.i(tag, msg);
		}
	}

	public static void logError(String tag, String msg) {
		if (isLogTurnOn) {
			Log.e(tag, msg);
		}
	}

	public static void logAssert(String tag, String msg) {
		if (isLogTurnOn) {
			Log.e(tag, msg);
		}
	}

	public static void logWarn(String tag, String msg) {
		if (isLogTurnOn) {
			Log.w(tag, msg);
		}
	}

	public static void logDebug(String tag, String msg) {
		if (isLogTurnOn) {
			Log.d(tag, msg);
		}
	}

	public static void logVerbose(String tag, String msg) {
		if (isLogTurnOn) {
			Log.v(tag, msg);
		}
	}

	public static void turnOnLog() {
		isLogTurnOn = true;
	}

	public static void turnOffLog() {
		isLogTurnOn = false;
	}
}
