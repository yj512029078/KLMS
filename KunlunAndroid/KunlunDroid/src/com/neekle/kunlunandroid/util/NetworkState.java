package com.neekle.kunlunandroid.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

public class NetworkState {

	/**
	 * 检测当的网络（WLAN、3G/2G）状态
	 * 
	 * @param context
	 *            Context
	 * @return true 表示网络可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				// 当前网络是连接的
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					// 当前所连接的网络可用
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isWifiActive(Context context) {
		boolean flag = false;

		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connManager != null) {
			NetworkInfo netInfo = connManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (netInfo.isConnected()) {
				flag = true;
			}
		}

		return flag;
	}

	public static String getLocalIPAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String ipAddress = FormatIP(wifiInfo.getIpAddress());
		return ipAddress;
	}

	private static String FormatIP(int ip) {
		return Formatter.formatIpAddress(ip);
	}

}
