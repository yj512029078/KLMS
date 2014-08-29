package com.neekle.kunlunandroid.common;

public class Constants {
	public static final int PART_MSG_MAX_LENGTH = 50;

	public static final String SHARED_PREF_FILE_NAME = "prefs.xml";
	public static final String HUDDLE_IDENTIFIER = "@muc.";
	public static final String EMPTY_STRING = "";

	public static class FriendPinyinCatlog {
		public static final int FRIEND_STAR = 0; // *
		public static final int FRIEND_EXTRA = 1; // #
		public static final int FRIEND_NORMAL = 2;
	}

	public static class MsgSendOrRcvTag {
		public static final String SENDING = "Sending";
		public static final String SEND = "Send";
		public static final String REV = "Receive";
	}

	public static class MsgBodyType {
		public static final String TEXT = "text";
		public static final String XHTML = "xhtml";
		public static final String GEOLOC = "geoloc";
	}

	public static class MsgInfoType {
		public static final int INSTANT = 0;
		public static final int HINT = 3;
		public static final int HELLO = 4;
		public static final int OTHER = 5;
		public static final int INVITING = 6;
		public static final int REV_INVITE = 7;
		public static final int DENY_INVITE = 8;
		public static final int IMPORTANT = 9;
		public static final int IMPORTANT_RECEIPT = 10;

		public static final int ATTENTION = 14;// 振屏消息
		public static final int IMAIL_NOTIFICATION = 15;// 即时邮件通知
		public static final int IMAIL_NOTIFICATION_RECEIPT = 16;// 即使邮件通知回执

	}

	public static class NotificationType {
		public static class NormalMsg {
			public static final int INSTANT = 1;
			public static final int HUDDLE = 2;
			public static final int HINT = 3;
			public static final int HELLO = 4;
			public static final int OTHER = 5;
			public static final int INVITING = 6;
		}

	}

	public static class NotificationMsgType {

		public static final int MICROBLOG = 1;
		public static final int NORMALMSG = 2;
	}

	public static class ChatFontSize {
		public static final int SMALL_SP = 18;
		public static final int MEDIUM_SP = 20;
		public static final int LARGE_SP = 22;
		public static final int VERY_LARGE_SP = 24;
	}

	public static class YesOrNo {
		public static final String YES = "y";
		public static final String NO = "n";
	}

	public static final int DEF_INT_VALUE = -1;

	public static class XmlType {
		public static final int SYS_CONFIG_INFO = 0;
		public static final int ADDSBOOK = 1;
	}

	public static class AddsbookNodeType {
		public static final int ADDSBOOK = 0;
		public static final int FOLDER = 1;
		public static final int XCARD = 2;
	}

	public static class ServerTypeCode {
		public static final String XMPP = "XMPP";
		public static final String SIP = "SIP";
	}

	public static class SexType {
		public static final String MALE = "Male";
		public static final String FEMALE = "Female";
		public static final String OTHER = "Other";
	}

}
