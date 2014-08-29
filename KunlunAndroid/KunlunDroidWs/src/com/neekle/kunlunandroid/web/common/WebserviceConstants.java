package com.neekle.kunlunandroid.web.common;

public class WebserviceConstants {

	public static class TaskThreadType {
		public static final int ASYNC_TASK_TYPE = 0;
		public static final int WEBSERVICE_INTENT_SERVICE_TYPE = 1;
		public static final int BLOCK_TASK_TYPE = 2;
	}

	public enum WebserviceType {

		TYPE_RETURN, TYPE_STRING, TYPE_XML_FRIENDS, TYPE_FRIEND_DATAILS, TYPE_CIRCLES, TYPE_DIC_TABLES, TYPE_DIC_COLUMNS, TYPE_DIC_PUBSUB_NODES, TYPE_ADDSBOOK_VIEWS, TYPE_VALIDATE_CODE, TYPE_SQL_RESULT, TYPE_DATETIME, TYPE_GRANT_RET, TYPE_USER, TYPE_DIC_CITY, TYPE_USER_BLACKLIST, TYPE_SERVER_ADDRESS, TYPE_DIC_PROVINCE, TYPE_DIC_COUNTRY, TypePasswordResetWays, TYPE_RETURN_CODE
	}

	public static final String NAME_SPACE = "http://delegate.ws.kunlun.sj.com";
	public static final String SOAP_ACTION_PREFIX = "urn:";

	// public static final String END_POINT_BASE_URL_TAO =
	// "http://172.30.3.85:7777/kunlunWebApp/services/";
	// public static final String END_POINT_BASE_URL =
	// "https://172.30.3.107:8443/kunlunWs/services/";
	public static final String END_POINT_BASE_URL = "http://172.30.3.107:3080/kunlunWs/services/";
	public static final String END_POINT_BASE_URL_OUTER = "http://172.30.3.107:3080/kunlunWs/services/";
	public static final String END_POINT_GLOCAL = END_POINT_BASE_URL + "Global";
	public static final String END_POINT_ADDSBOOK = END_POINT_BASE_URL
			+ "Addsbook";
	public static final String END_POINT_CIRCLE = END_POINT_BASE_URL + "Circle";
	public static final String END_POINT_FRIEND = END_POINT_BASE_URL + "Friend";
	public static final String END_POINT_MANAGE = END_POINT_BASE_URL + "Manage";
	public static final String END_POINT_USER = END_POINT_BASE_URL + "User";
	public static final String END_POINT_USERPROFILE = END_POINT_BASE_URL
			+ "UserProfile";
	public static final String END_POINT_DICQUERY = END_POINT_BASE_URL
			+ "DicQuery";

	public static final String GLOCAL_METHOD_LOGIN = "login";

	public static final String MANAGE_METHOD_MANAGE_GET_SERVERS_ADDRESS = "manageGetServersAddress";
	public static final String MANAGE_METHOD_GET_DIC_TABLES = "getDicTables";
	public static final String MANAGE_METHOD_GET_DIC_COLUMNS = "getDicColumns";
	public static final String MANAGE_METHOD_GET_TYPE_DIC_PUBSUB_NODES = "getTypeDicPubsubNodes";

	public static final String ADDSBOOK_METHOD_PUBLISH = "addsbookPublish";
	public static final String ADDSBOOK_METHOD_NAME_CHANGE = "addsbookNameChange";
	public static final String ADDSBOOK_METHOD_ITEM_MOVE = "addsbookItemMove";
	public static final String ADDSBOOK_METHOD_ITEM_DELETE = "addsbookItemDelete";
	public static final String ADDSBOOK_METHOD_ITEM_ADD = "addsbookItemAdd";
	public static final String ADDSBOOK_METHOD_GET_ADDSBOOK_LISTVIEW = "addsbookGetAddsbookListView";
	public static final String ADDSBOOK_METHOD_GET = "addsbookGet";
	public static final String ADDSBOOK_METHOD_FRIEND_ADDSBOOK_RENAME = "addsbookFriendAddsbookRename";
	public static final String ADDSBOOK_METHOD_FRIEND_ADDSBOOK_DELETE = "addsbookFriendAddsbookDelete";
	public static final String ADDSBOOK_METHOD_FRIEND_ADDSBOOK_ADD = "addsbookFriendAddsbookAdd";
	public static final String ADDSBOOK_METHOD_FOLDER_RENAME = "addsbookFolderRename";
	public static final String ADDSBOOK_METHOD_FOLDER_DELETE = "addsbookFolderDelete";
	public static final String ADDSBOOK_METHOD_DELETE = "addsbookDelete";
	public static final String ADDSBOOK_METHOD_CREATE = "addsbookCreate";

	public static final String FRIEND_METHOD_UPLOAD_MRULIST = "friendUploadMRUList";
	public static final String FRIEND_METHOD_SYNC = "friendSync";
	public static final String FRIEND_METHOD_INFOR_UPDATE = "friendInforUpdate";
	public static final String FRIEND_METHOD_INFOR_GET = "friendInforGet";
	public static final String FRIEND_METHOD_GROUP_RENAME = "friendGroupRename";
	public static final String FRIEND_METHOD_GROUP_DELETE = "friendGroupDelete";
	public static final String FRIEND_METHOD_GET_MRULIST = "friendGetMRUList";
	public static final String FRIEND_METHOD_GET_LIST = "friendGetList";
	public static final String FRIEND_METHOD_GET_FRIENDS_CRC = "friendGetFriendsCRC";
	public static final String FRIEND_METHOD_DELETE = "friendDelete";
	public static final String FRIEND_METHOD_CHANGE_GROUP = "friendChangeGroup";
	public static final String FRIEND_METHOD_ADDSBOOK_GET = "friendAddsbookGet";
	public static final String FRIEND_METHOD_ADD_GROUP = "friendAddGroup";
	public static final String FRIEND_METHOD_ADD = "friendAdd";

	public static final String CIRCLE_METHOD_TABLE_SETUP = "circleTableSetup";
	public static final String CIRCLE_METHOD_PUBSUB_SETUP = "circlePubsubSetup";
	public static final String CIRCLE_METHOD_GET_CIRCLE_LIST = "circleGetCircleList";
	public static final String CIRCLE_METHOD_FRIEND_CIRCLE_SETUP = "circleFriendCircleSetup";
	public static final String CIRCLE_METHOD_DELETE = "circleDelete";
	public static final String CIRCLE_METHOD_CREATE = "circleCreate";
	public static final String CIRCLE_METHOD_COLUMN_SETUP = "circleColumnSetup";
	public static final String CIRCLE_METHOD_ATTRIBUTE = "circleAttribute";

	public static final String USER_METHOD_USER_GET_BLACKLIST = "userGetBlackList";
	public static final String USER_METHOD_USER_BLACKLIST = "userBlackList";
	public static final int USER_METHOD_USER_BLACKLIST_ARGS_OP_TYPE_ADD = 0;
	public static final int USER_METHOD_USER_BLACKLIST_ARGS_OP_TYPE_DELETE = 2;

	public static final String MANAGE_METHOD_ACCOUNT_REG = "manageAccountReg";//
	public static final String MANAGE_METHOD_GET_SYSTEM_DOMAIN_NAME = "manageGetSystemDomainName";
	public static final String MANAGE_METHOD_GET_HUMAN_VALIDATE_PICTURE = "manageGetHumanValidatePicture";
	public static final String MANAGE_METHOD_HUMAN_VALIDATE = "manageHumanValidate";
	public static final String MANAGE_METHOD_GET_SYSTEM_TIME = "manageGetSystemTime";
	public static final String MANAGE_WEB_SESSION_VALIDATE = "manageWebSessionValidate";
	public static final String MANAGE_WEB_SESSION_SCAN = "manageWebSessionScan";

	public static final String USER_METHOD_FULLNAME_UPDATE = "userFullnameUpdate";
	public static final String USER_METHOD_UPDATE_PASSWORD = "userUpdatePassword";
	public static final String USER_METHOD_AVATAR_CHANGE = "userAvatarChange";
	public static final String USER_METHOD_AVATAR_DELETE = "userAvatarDelete";
	public static final String USER_METHOD_BACKGROUND_CHANGE = "userBackgroundChange";
	public static final String USER_METHOD_BACKGROUND_DELETE = "userBackgroundDelete";
	public static final String USER_METHOD_GET_PWDRESET_WAYS = "userGetPwdResetWays";
	public static final String USER_METHOD_APPLY_PWDRESET_BY_MOBILE = "userApplyPwdResetByMobile";
	public static final String USER_METHOD_APPLY_PWDRESET_BY_MAIL = "userApplyPwdResetByMail";
	public static final String USER_METHOD_APPLY_PWDRESET_BY_QA = "userApplyPwdResetByQA";
	public static final String USER_METHOD_PWDRESET = "userPwdReset";
	public static final String USER_METHOD_PWDPROTECT_SETUP = "userPwdProtectSetup";
	public static final String USER_METHOD_APPLY_MOBILE_BIND = "userApplyMobileBind";
	public static final String USER_METHOD_APPLY_EMAIL_BIND = "userApplyEmailBind";
	public static final String USER_METHOD_MOBILE_BIND = "userMobileBind";
	public static final String USER_METHOD_EMAIL_BIND = "userEmailBind";
	public static final String USER_METHOD_CHANGE_LANGUAGE = "userChangeLanguage";
	public static final String USER_METHOD_ALLOW_MULTIPOINT_LOGIN = "userAllowMultiPointLogin";
	public static final String USER_METHOD_PUB_SCREEN_FROM_CONTACTS = "userPubScreenFromContacts";
	public static final String USER_METHOD_PUB_HIDE_TO_CONTACTS = "userPubHideToContacts";
	public static final String USER_METHOD_BLACKLIST = "userBlackList";
	public static final String USER_METHOD_UPLOAD_MRULIST = "userUploadMRUList";
	public static final String USER_METHOD_GET_MRULIST = "userGetMRUList";

	public static final String USERPROFILE_METHOD_SELECT = "userProfileSelect";
	public static final String USERPROFILE_METHOD_BASIC_INFOR_UPDATE = "userProfileBasicInforUpdate";
	public static final String USERPROFILE_METHOD_CONTACT_INFOR_UPDATE = "userProfileContactInforUpdate";
	public static final String USERPROFILE_METHOD_GRANT_SELECT = "userProfileGrantSelect";
	public static final String USERPROFILE_METHOD_GRANT_SELECT_GET = "userProfileGrantSelectGet";

	public static final String DICQUERY_METHOD_APP = "dicQueryApp";
	public static final String DICQUERY_METHOD_BLOODTYPE = "dicQueryBloodType";
	public static final String DICQUERY_METHOD_CIRCLETEMPLATE = "dicQueryCircleTemplate";
	public static final String DICQUERY_METHOD_CITY = "dicQueryCity";
	public static final String DICQUERY_METHOD_COMMUNITY = "dicQueryCommunity";
	public static final String DICQUERY_METHOD_COUNTRY = "dicQueryCountry";
	public static final String DICQUERY_METHOD_DISABLETYPE = "dicQueryDisableType";
	public static final String DICQUERY_METHOD_EDUCATION = "dicQueryEducation";
	public static final String DICQUERY_METHOD_ENTITYAPP = "dicQueryEntityApp";
	public static final String DICQUERY_METHOD_FIELDGROUP = "dicQueryFieldGroup";
	public static final String DICQUERY_METHOD_FRIEND_PURPOSE = "dicQueryFriendPurpose";
	public static final String DICQUERY_METHOD_INDUSTRY = "dicQueryIndustry";
	public static final String DICQUERY_METHOD_MARRIAGE = "dicQueryMarriage";
	public static final String DICQUERY_METHOD_PASSWORD_QUESTION = "dicQueryPasswordQuestion";
	public static final String DICQUERY_METHOD_PROVINCE = "dicQueryProvince";
	public static final String DICQUERY_METHOD_RELATION = "dicQueryRelation";
	public static final String DICQUERY_METHOD_SEX = "dicQuerySex";
	public static final String DICQUERY_METHOD_SEXORIENTATION = "dicQuerySexOrientation";
	public static final String DICQUERY_METHOD_SVRTYPE = "dicQuerySvrType";
	public static final String DICQUERY_METHOD_USER_ONLINE_STATUS = "dicQueryUserOnlineStatus";

	public static final String LANG_ZH_CN = "zh_cn";
	public static final String LANG_ZH_TW = "zh_tw";
	public static final String LANG_EN_US = "en_us";
	public static final String LANG_JA_JP = "ja_jp";

	public static final int WERBSERVICE_RESULT_SUCCESS_CODE = 2000;
	public static final int WERBSERVICE_RESULT_FRIEND_EXIST_FRIENDLIST_CODE = 4007;
	public static final int WERBSERVICE_RESULT_FRIEND_EXIST_GROUP_CODE = 4010;
	public static final int WERBSERVICE_RESULT_CIRCLE_FRIEND_EXIST_CODE = 4065;
	public static final int WERBSERVICE_RESULT_ADDRESSBOOK_NAME_EXIST_CODE = 4028;
	public static final int WERBSERVICE_RESULT_ADDRESSBOOK_EXIST_CODE = 4031;

}
