package com.neekle.kunlunandroid.db;

public class DbConstants {

	public static final int DB_DEF_ROW_ID = -1;
	public static final int DB_ZERO = 0;

	public final static String FIELD_ID = "Id";

	public final static String TB_ACCOUNT_USERINFO_JID = "Jid";
	public final static String TB_ACCOUNT_USERINFO_NAME = "Name";
	public final static String TB_ACCOUNT_USERINFO_REMARK_NAME = "RemarkName";
	public final static String TB_ACCOUNT_USERINFO_SIGNATURE = "Signature";
	public final static String TB_ACCOUNT_USERINFO_HEAD_PORTRAIT = "HeadPortrait";
	public final static String TB_ACCOUNT_USERINFO_BACKGROUND_PIC = "BackgroundPic";
	public final static String TB_ACCOUNT_USERINFO_BIRTHDAY = "Birthday";
	public final static String TB_ACCOUNT_USERINFO_SEX = "Sex";
	public final static String TB_ACCOUNT_USERINFO_CITY = "City";
	public final static String TB_ACCOUNT_USERINFO_EMAIL = "Email";
	public final static String TB_ACCOUNT_USERINFO_MOBILE_PHONE = "MobilePhone";
	public final static String TB_ACCOUNT_USERINFO_WORK_PHONE = "WorkPhone";
	public final static String TB_ACCOUNT_USERINFO_REMEMBER_PASSWD = "RememberPassword";
	public final static String TB_ACCOUNT_USERINFO_AUTO_LOGIN = "AutoLogin";
	public final static String TB_ACCOUNT_USERINFO_LOGIN_PASSWD = "LoginPassword";
	public final static String TB_ACCOUNT_USERINFO_LOGIN_TAG = "LoginTag";
	public final static String TB_ACCOUNT_USERINFO_LOGIN_ADDRESS = "LoginAddress";
	public final static String TB_ACCOUNT_USERINFO_LAST_LOGIN_TIME = "LastLoginTime";
	public final static String TB_ACCOUNT_USERINFO_AUTO_SIGN_ON_LOGIN = "AutoSignOnLogin";
	public final static String TB_ACCOUNT_USERINFO_AUTO_SIGN_DURATION = "AutoSignDuration";
	public final static String TB_ACCOUNT_USERINFO_WEB_OR_POP3 = "WebOrPop3";
	public final static String TB_ACCOUNT_USERINFO_POP3_SERVER = "Pop3Server";
	public final static String TB_ACCOUNT_USERINFO_POP3_USERID = "Pop3UserID";
	public final static String TB_ACCOUNT_USERINFO_POP3_PASSWD = "Pop3Password";
	public final static String TB_ACCOUNT_USERINFO_CAN_MULTI_TIME_LOGIN = "CanMultiTimeLogin";
	public final static String TB_ACCOUNT_USERINFO_AUTO_ACCEPT_INVITE = "AutoAcceptInvite";
	public final static String TB_ACCOUNT_USERINFO_IS_MULTI_PAGE_CHAT = "IsMultiPageChat";
	public final static String TB_ACCOUNT_USERINFO_SAVE_RECORD_ONLINE = "SaveRecordOnLine";
	public final static String TB_ACCOUNT_USERINFO_UI_SHOW_STYLE = "UIShowStyle";
	public final static String TB_ACCOUNT_USERINFO_MESSAGE_NOTIFY = "MessageNotify";
	public final static String TB_ACCOUNT_USERINFO_MESSAGE_NOTIFY_BEEP = "MessageNotifyBeep";
	public final static String TB_ACCOUNT_USERINFO_MESSAGE_NOTIFY_SHAKE = "MessageNotifyShake";
	public final static String TB_ACCOUNT_USERINFO_MICROBLOG_NOTIFY = "MicroblogNotify";
	public final static String TB_ACCOUNT_USERINFO_MICROBLOG_NOTIFY_BEEP = "MicroblogNotifyBeep";
	public final static String TB_ACCOUNT_USERINFO_MICROBLOG_NOTIFY_SHAKE = "MicroblogNotifyShake";
	public final static String TB_ACCOUNT_USERINFO_FRIENDS_NOTIFY = "FriendsNotify";
	public final static String TB_ACCOUNT_USERINFO_FRIENDS_NOTIFY_BEEP = "FriendsNotifyBeep";
	public final static String TB_ACCOUNT_USERINFO_FRIENDS_NOTIFY_SHAKE = "FriendsNotifyShake";

	public final static String TB_FRIENDS_FIELD_MY_JID = "MyJid";
	public final static String TB_FRIENDS_FIELD_FRIEND_JID = "FriendJid";
	public final static String TB_FRIENDS_FIELD_NAME = "Name";
	public final static String TB_FRIENDS_FIELD_NAME_PINYIN = "NamePinYin";
	public final static String TB_FRIENDS_FIELD_REMARK_NAME = "RemarkName";
	public final static String TB_FRIENDS_FIELD_REMARK_NAME_PINYIN = "RemarkNamePinYin";
	public final static String TB_FRIENDS_FIELD_SIGNATURE = "Signature";
	public final static String TB_FRIENDS_FIELD_SEX = "Sex";
	public final static String TB_FRIENDS_FIELD_MOBILE_PHONE = "MobilePhone";
	public final static String TB_FRIENDS_FIELD_EMAIL = "Email";
	public final static String TB_FRIENDS_FIELD_PHONE = "Phone";
	public final static String TB_FRIENDS_FIELD_HEAD_PORTRAIT = "HeadPortrait";
	public final static String TB_FRIENDS_FIELD_BACKGROUND_PIC = "BackgroundPic";
	public final static String TB_FRIENDS_FIELD_INVITE_STATE = "InviteState";
	public final static String TB_FRIENDS_FIELD_STAR_SIGN = "StarSign";
	public final static String TB_FRIENDS_FIELD_IGNORE_MICROBLOG = "IgnoreMicroblog";
	public final static String TB_FRIENDS_FIELD_IN_BLACKLIST = "InBlackList";

	public final static String TB_ADDRESSBOOK_FIELD_MY_JID = "MyJid";
	public final static String TB_ADDRESSBOOK_FIELD_ROOT_JID = "RootJid";
	public final static String TB_ADDRESSBOOK_FIELD_ROOT_NAME = "RootName";
	public final static String TB_ADDRESSBOOK_FIELD_FILE_CONTENT = "FileContent";
	public final static String TB_ADDRESSBOOK_FIELD_IS_SHARED = "IsShared";
	public final static String TB_ADDRESSBOOK_FIELD_OWNER_JID = "OwnerJid";

	// 表名:Tb_Record _ChatSum
	public final static String TB_RECORD_CHAT_SUM_FIELD_MY_JID = "MyJid";
	public final static String TB_RECORD_CHAT_SUM_FIELD_SHOW_JID_OR_ROOMID = "ShowJidOrRoomId";
	public final static String TB_RECORD_CHAT_SUM_FIELD_SHOW_NAME = "ShowName";
	public final static String TB_RECORD_CHAT_SUM_FIELD_IS_SINGLE_CHAT = "IsSingleChat";
	public final static String TB_RECORD_CHAT_SUM_FIELD_DB_TIME = "DbTime";
	public final static String TB_RECORD_CHAT_SUM_FIELD_TIEM_STAMP = "TimeStamp";
	public final static String TB_RECORD_CHAT_SUM_FIELD_GEOLOC = "Geoloc";
	public final static String TB_RECORD_CHAT_SUM_FIELD_PART_MESSAGE = "PartMessage";
	public final static String TB_RECORD_CHAT_SUM_FIELD_INFO_TYPE = "InfoType";
	public final static String TB_RECORD_CHAT_SUM_FIELD_BACKGROUND_PIC = "BackgroundPic";
	public final static String TB_RECORD_CHAT_SUM_FIELD_IS_NEW_MESSAGE_NOTIFY = "IsNewMessageNotify";

	public final static String TB_RECORD_SINGLE_PHONE_FIELD_MY_JID = "MyJid";
	public final static String TB_RECORD_SINGLE_PHONE_FIELD_FROM_JID = "FromJid";
	public final static String TB_RECORD_SINGLE_PHONE_FIELD_TO_JID = "ToJid";
	public final static String TB_RECORD_SINGLE_PHONE_FIELD_SEND_OR_RCV_TAG = "SendOrRcvTag";
	public final static String TB_RECORD_SINGLE_PHONE_FIELD_IS_CONNECT = "IsConnect";
	public final static String TB_RECORD_SINGLE_PHONE_FIELD_PHONE_NUM = "PhoneNum";
	public final static String TB_RECORD_SINGLE_PHONE_FIELD_DB_TIME = "DbTime";
	public final static String TB_RECORD_SINGLE_PHONE_FIELD_TIME_STAMP = "TimeStamp";
	public final static String TB_RECORD_SINGLE_PHONE_FIELD_AV_TAG = "AvTag";

	public final static String TB_RECORD_SINGLE_CHAT_FIELD_MY_JID = "MyJid";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_FROM_JID = "FromJid";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_FROM_NAME = "FromName";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_TO_JID = "ToJid";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_TO_NAME = "ToName";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_PARTNER_JID = "PartnerJid";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_SEND_OR_RCV_TAG = "SendOrRcvTag";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_MESSAGE_ID = "MessageID";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_MEAASGE_TYPE = "MessageType";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_THREAD_ID = "ThreadID";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_TIME_STAMP = "TimeStamp";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_GEOLOC = "Geoloc";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_DB_TIME = "DbTime";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_BODY_TYPE = "BodyType";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_TEXT_BODY = "TextBody";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_MSG_BODY = "MsgBody";
	public final static String TB_RECORD_SINGLE_CHAT_FIELD_INFO_TYPE = "InfoType";

	public final static String TB_GROUPS_FIELD_GROUP_NAME = "GroupName";
	public final static String TB_GROUPS_FIELD_GROUP_TYPE = "GroupType";
	public final static String TB_GROUPS_FIELD_XH = "Xh";

	public final static String TB_FRIENDS_GROUPS_FIELD_JID = "Jid";
	public final static String TB_FRIENDS_GROUPS_FIELD_GROUP_NAME = "GroupName";
	public final static String TB_FRIENDS_GROUPS_FIELD_XH = "Xh";

	// Tb_Record_Multi_Chat

	public final static String TB_RECORD_MULTI_CHAT_ID = "Id";
	public final static String TB_RECORD_MULTI_CHAT_MY_JID = "MyJid";
	public final static String TB_RECORD_MULTI_CHAT_FROM_JID = "FromJid";
	public final static String TB_RECORD_MULTI_CHAT_FROM_NAME = "FromName";
	public final static String TB_RECORD_MULTI_CHAT_CREATOR_JID = "CreatorJid";

	public final static String TB_RECORD_MULTI_CHAT_ROOM_JID = "RoomJid";
	public final static String TB_RECORD_MULTI_CHAT_SEND_OR_RCV_TAG = "SendOrRcvTag";
	public final static String TB_RECORD_MULTI_CHAT_MESSAGE_ID = "MessageID";
	public final static String TB_RECORD_MULTI_CHAT_MESSAGE_TYPE = "MessageType";
	public final static String TB_RECORD_MULTI_CHAT_THREAD_ID = "ThreadID";
	public final static String TB_RECORD_MULTI_CHAT_GEOLOC = "Geoloc";

	public final static String TB_RECORD_MULTI_CHAT_TIME_STAMP = "TimeStamp";
	public final static String TB_RECORD_MULTI_CHAT_DB_TIME = "DbTime";
	public final static String TB_RECORD_MULTI_CHAT_BODY_TYPE = "BodyType";
	public final static String TB_RECORD_MULTI_CHAT_TEXT_BODY = "TextBody";
	public final static String TB_RECORD_MULTI_CHAT_MSG_BODY = "MsgBody";
	public final static String TB_RECORD_MULTI_CHAT_INFO_TYPE = "InfoType";
	public final static String TB_RECORD_MULTI_CHAT_SEND_SUCCESS_TAG = "SendSuccessTag";
	public final static String TB_RECORD_MULTI_CHAT_ISDELETE = "IsDelete";

	// Tb_Multi_Chat_RoomInfo
	public final static String TB_MULTI_CHAT_ROOMINFO_ID = "Id";
	public final static String TB_MULTI_CHAT_ROOMINFO_MY_JID = "MyJid";
	public final static String TB_MULTI_CHAT_ROOMINFO_ROOM_JID = "RoomJid";
	public final static String TB_MULTI_CHAT_ROOMINFO_OCCUPANT_JID = "occupantJID";

	public final static String TB_MULTI_CHAT_ROOMINFO_OCCUPANT_NAME = "occupantName";
	public final static String TB_MULTI_CHAT_ROOMINFO_ENTER_LEAVE_TAG = "EnterLeaveTag";
	public final static String TB_MULTI_CHAT_ROOMINFO_DB_TIME = "DbTime";

	// Tb_UI_Notification
	public final static String TB_UI_NOTIFICATION_ID = "Id";
	public final static String TB_UI_NOTIFICATION_MY_JID = "MyJid";
	public final static String TB_UI_NOTIFICATION_NOTIFICATION_TYPE = "NotificationType";
	public final static String TB_UI_NOTIFICATION_NOTIFICATION_VALUE = "NotificationValue";
	public final static String TB_UI_NOTIFICATION_RECEIVED_TIME = "ReceivedTime";

	// 表名:Tb_Huddle_Setting 群设置
	public final static String TB_HUDDLE_SETTING_ID = "Id";
	public final static String TB_HUDDLE_SETTING_MY_JID = "MyJid";
	public final static String TB_HUDDLE_SETTING_ROOM_JID = "RoomJid";
	public final static String TB_HUDDLE_SETTING_HUDDLE_TITLE = "HuddleTitle";
	public final static String TB_HUDDLE_SETTING_IS_CLOSE_NOTIFY = "IsCloseNotify";

	public final static String TB_HUDDLE_SETTING_NICK_NAME = "NickName";
	public final static String TB_HUDDLE_SETTING_IS_SHOW_NICKNAME = "IsShowNickName";
	public final static String TB_HUDDLE_SETTING_IS_SAVE_ADDRESSBOOK = "IsSaveAddressBook";
	public final static String TB_HUDDLE_SETTING_IS_SET_TOP = "IsSetTop";
	public final static String TB_HUDDLE_SETTING_STAR_SIGN = "StarSign";

	// Tb_Account_UI_FeatureList
	public final static String TB_ACCOUNT_UI_FEATURE_LIST_ID = "Id";
	public final static String TB_ACCOUNT_UI_FEATURE_LIST_MY_JID = "MyJid";
	public final static String TB_ACCOUNT_UI_FEATURE_LIST_FEATURE_ID = "FeatureId";
	public final static String TB_ACCOUNT_UI_FEATURE_LIST_ENABLE_TAG = "EnableTag";

	// Tb_UI_FeatureList

	public final static String TB_UI_FEATURE_LIST_FEATURE_ID = "FeatureId";
	public final static String TB_UI_FEATURE_LIST_FEATURE_SHOW_NAME = "FeatureShowName";
	public final static String TB_UI_FEATURE_LIST_FEATURE_INFO = "FeatureInfo";
	public final static String TB_UI_FEATURE_LIST_FEATURE_IMAGE = "FeatureImage";
	public final static String TB_UI_FEATURE_LIST_FEATURE_UI_INDEX = "FeatureUIIndex";
	public final static String TB_UI_FEATURE_LIST_MAIN_VIEW_INDEX = "MainViewIndex";
	public final static String TB_UI_FEATURE_LIST_CLASS_NAME = "ClassName";

	// Tb_ChatInput_FeatureList

	public final static String TB_CHATINPUT_FEATURE_LIST_FEATURE_ID = "FeatureId";
	public final static String TB_CHATINPUT_FEATURE_LIST_IS_SINGLE_CHAT = "IsSingleChat";
	public final static String TB_CHATINPUT_FEATURE_LIST_FEATURE_SHOW_NAME = "FeatureShowName";
	public final static String TB_CHATINPUT_FEATURE_LIST_FEATURE_INFO = "FeatureInfo";
	public final static String TB_CHATINPUT_FEATURE_LIST_FEATURE_IMAGE = "FeatureImage";
	public final static String TB_CHATINPUT_FEATURE_LIST_FEATURE_UI_INDEX = "FeatureUIIndex";
	public final static String TB_CHATINPUT_FEATURE_LIST_MAIN_VIEW_INDEX = "MainViewIndex";
	public final static String TB_CHATINPUT_FEATURE_LIST_CLASS_NAME = "ClassName";

	// Tb_Account_ChatInput_FeatureList

	public final static String TB_ACCOUNT_CHATINPUT_FEATURE_LIST_ID = "Id";
	public final static String TB_ACCOUNT_CHATINPUT_FEATURE_LIST_MY_JID = "MyJid";
	public final static String TB_ACCOUNT_CHATINPUT_FEATURE_LIST_FEATURE_ID = "FeatureId";
	public final static String TB_ACCOUNT_CHATINPUT_FEATURE_LIST_ENABLE_TAG = "EnableTag";

	// Tb_Record_Microblog
	public final static String TB_RECORD_MICROBLOG_ID = "Id";
	public final static String TB_RECORD_MICROBLOG_MY_JID = "MyJid";
	public final static String TB_RECORD_MICROBLOG_FROM_JID = "FromJid";
	public final static String TB_RECORD_MICROBLOG_FROM_NAME = "FromName";

	public final static String TB_RECORD_MICROBLOG_SEND_OR_RCV_TAG = "SendOrRcvTag";
	public final static String TB_RECORD_MICROBLOG_MICROBLOG_ID = "MicroblogId";
	public final static String TB_RECORD_MICROBLOG_TIME_STAMP = "TimeStamp";
	public final static String TB_RECORD_MICROBLOG_GEOLOC = "Geoloc";

	public final static String TB_RECORD_MICROBLOG_DB_TIME = "DbTime";
	public final static String TB_RECORD_MICROBLOG_BODY_TYPE = "BodyType";
	public final static String TB_RECORD_MICROBLOG_MSG_BODY = "MsgBody";
	public final static String TB_RECORD_MICROBLOG_INFO_TYPE = "InfoType";

	public final static String TB_RECORD_MICROBLOG_LINK_ID = "LinkId";
	public final static String TB_RECORD_MICROBLOG_IS_SEND_FAILED = "IsSendFailed";

}
