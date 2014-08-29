package com.neekle.kunlunandroid.util;

import java.util.ArrayList;
import java.util.Map;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class OperatePreference {

	public static boolean writePreferences(Context context, String fileName,
			String key, String value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				fileName, Context.MODE_WORLD_WRITEABLE).edit();
		editor.putString(key, value);
		boolean flag = editor.commit();

		Log.i("write", flag + "");
		return flag;
	}

	public static boolean writePreferences(Context context, String fileName,
			String key, int value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				fileName, Context.MODE_WORLD_WRITEABLE).edit();
		editor.putInt(key, value);
		boolean flag = editor.commit();

		Log.i("write", flag + "");
		return flag;
	}

	public static boolean writePreferences(Context context, String fileName,
			String key, Boolean value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				fileName, Context.MODE_WORLD_WRITEABLE).edit();
		editor.putBoolean(key, value);
		boolean flag = editor.commit();

		Log.i("write", flag + "");
		return flag;
	}

	public static String readPreferences(Context context, String fileName,
			String key, String defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				fileName, Context.MODE_WORLD_READABLE);
		String str = sharedPreferences.getString(key, defValue);

		return str;
	}

	public static int readPreferences(Context context, String fileName,
			String key, int defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				fileName, Context.MODE_WORLD_READABLE);
		int value = sharedPreferences.getInt(key, defValue);

		return value;
	}

	public static boolean readPreferences(Context context, String fileName,
			String key, Boolean defValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				fileName, Context.MODE_WORLD_READABLE);
		boolean value = sharedPreferences.getBoolean(key, defValue);

		return value;
	}

	public static boolean isContain(Context context, String fileName, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				fileName, Context.MODE_WORLD_READABLE);
		boolean flag = sharedPreferences.contains(key);

		return flag;
	}

	public static void deleteItem(Context context, String fileName, String key) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				fileName, Context.MODE_WORLD_WRITEABLE).edit();
		editor.remove(key);
		editor.commit();
	}

	public static ArrayList<String> readAll(Context context, String fileName) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				fileName, Context.MODE_WORLD_READABLE);
		Map<String, ?> map = sharedPreferences.getAll();

		ArrayList<String> arrayList = new ArrayList<String>();

		for (Map.Entry<String, ?> m : map.entrySet()) {
			String string = (String) m.getValue();
			arrayList.add(string);
		}

		return arrayList;
	}
}
