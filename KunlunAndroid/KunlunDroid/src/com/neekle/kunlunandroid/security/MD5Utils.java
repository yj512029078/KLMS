package com.neekle.kunlunandroid.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 对外提供getMD5(String)方法
 * 
 * 
 */
public class MD5Utils {

	public static String getMD5(String val) {
		String string = null;
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		if (md5 != null) {
			md5.update(val.getBytes());
			byte[] m = md5.digest();
			string = getString(m);
		} else {
			string = val;
		}

		return string;
	}

	private static String getString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(b[i]);
		}
		return sb.toString();
	}
}