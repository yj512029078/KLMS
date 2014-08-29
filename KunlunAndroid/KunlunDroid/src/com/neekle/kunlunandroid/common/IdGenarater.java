package com.neekle.kunlunandroid.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IdGenarater {

	public static class MsgId {
		public static String generateMsgId() {
			String string = getTimeAsMsgId();
			return string;
		}

		private static String getTimeAsMsgId() {
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyyMMddHHmmssSSS");
			String time = simpleDateFormat.format(date);

			return time;
		}
	}

	public static class ThreadId {
		public static String generateThreadId() {
			String string = getTimeAsThreadId();
			return string;
		}

		private static String getTimeAsThreadId() {
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyyMMddHHmmss");
			String time = simpleDateFormat.format(date);

			return time;
		}
	}
}
