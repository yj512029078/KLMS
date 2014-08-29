package com.neekle.kunlunandroid.db;

import java.util.ArrayList;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.data.LoginBasicInfo;
import com.neekle.kunlunandroid.data.LoginExtenedInfo;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TbAccountUserInfoController {
	private DbHelper mDbHelper;

	private static final int UPDATE_SUCCESS = -2;

	private final static String TABLE_NAME = "Tb_Account_UserInfo";
	public final static String SQL_CREATE_TB = "Create table " + TABLE_NAME
			+ "(" + DbConstants.TB_ACCOUNT_USERINFO_JID
			+ " VARCHAR(50) primary key,"
			+ DbConstants.TB_ACCOUNT_USERINFO_NAME + " VARCHAR(50),"
			+ DbConstants.TB_ACCOUNT_USERINFO_REMARK_NAME + " VARCHAR(50),"
			+ DbConstants.TB_ACCOUNT_USERINFO_SIGNATURE + " VARCHAR(280),"
			+ DbConstants.TB_ACCOUNT_USERINFO_HEAD_PORTRAIT + " VARCHAR(256),"
			+ DbConstants.TB_ACCOUNT_USERINFO_BACKGROUND_PIC + " VARCHAR(256),"
			+ DbConstants.TB_ACCOUNT_USERINFO_BIRTHDAY + " VARCHAR(8),"
			+ DbConstants.TB_ACCOUNT_USERINFO_SEX + " VARCHAR(6),"
			+ DbConstants.TB_ACCOUNT_USERINFO_CITY + " VARCHAR(50),"
			+ DbConstants.TB_ACCOUNT_USERINFO_EMAIL + " VARCHAR(100),"
			+ DbConstants.TB_ACCOUNT_USERINFO_MOBILE_PHONE + " VARCHAR(20),"
			+ DbConstants.TB_ACCOUNT_USERINFO_WORK_PHONE + " VARCHAR(20),"
			+ DbConstants.TB_ACCOUNT_USERINFO_REMEMBER_PASSWD + " VARCHAR(1),"
			+ DbConstants.TB_ACCOUNT_USERINFO_AUTO_LOGIN + " VARCHAR(1),"
			+ DbConstants.TB_ACCOUNT_USERINFO_LOGIN_PASSWD + " VARCHAR(100),"
			+ DbConstants.TB_ACCOUNT_USERINFO_LOGIN_TAG + " VARCHAR(1),"
			+ DbConstants.TB_ACCOUNT_USERINFO_LOGIN_ADDRESS + " VARCHAR(30),"
			+ DbConstants.TB_ACCOUNT_USERINFO_LAST_LOGIN_TIME
			+ " VARCHAR(100),"
			+ DbConstants.TB_ACCOUNT_USERINFO_AUTO_SIGN_ON_LOGIN
			+ " VARCHAR(1),"
			+ DbConstants.TB_ACCOUNT_USERINFO_AUTO_SIGN_DURATION
			+ " VARCHAR(10)," + DbConstants.TB_ACCOUNT_USERINFO_WEB_OR_POP3
			+ " VARCHAR(1)," + DbConstants.TB_ACCOUNT_USERINFO_POP3_SERVER
			+ " VARCHAR(20)," + DbConstants.TB_ACCOUNT_USERINFO_POP3_USERID
			+ " VARCHAR(100)," + DbConstants.TB_ACCOUNT_USERINFO_POP3_PASSWD
			+ " VARCHAR(100),"
			+ DbConstants.TB_ACCOUNT_USERINFO_CAN_MULTI_TIME_LOGIN
			+ " VARCHAR(1),"
			+ DbConstants.TB_ACCOUNT_USERINFO_AUTO_ACCEPT_INVITE
			+ " VARCHAR(1),"
			+ DbConstants.TB_ACCOUNT_USERINFO_IS_MULTI_PAGE_CHAT
			+ " VARCHAR(1),"
			+ DbConstants.TB_ACCOUNT_USERINFO_SAVE_RECORD_ONLINE
			+ " VARCHAR(1)," + DbConstants.TB_ACCOUNT_USERINFO_UI_SHOW_STYLE
			+ " VARCHAR(1)," + DbConstants.TB_ACCOUNT_USERINFO_MESSAGE_NOTIFY
			+ " VARCHAR(1),"
			+ DbConstants.TB_ACCOUNT_USERINFO_MESSAGE_NOTIFY_BEEP
			+ " VARCHAR(1),"
			+ DbConstants.TB_ACCOUNT_USERINFO_MESSAGE_NOTIFY_SHAKE
			+ " VARCHAR(1)," + DbConstants.TB_ACCOUNT_USERINFO_MICROBLOG_NOTIFY
			+ " VARCHAR(1),"
			+ DbConstants.TB_ACCOUNT_USERINFO_MICROBLOG_NOTIFY_BEEP
			+ " VARCHAR(1),"
			+ DbConstants.TB_ACCOUNT_USERINFO_MICROBLOG_NOTIFY_SHAKE
			+ " VARCHAR(1)," + DbConstants.TB_ACCOUNT_USERINFO_FRIENDS_NOTIFY
			+ " VARCHAR(1),"
			+ DbConstants.TB_ACCOUNT_USERINFO_FRIENDS_NOTIFY_BEEP
			+ " VARCHAR(1),"
			+ DbConstants.TB_ACCOUNT_USERINFO_FRIENDS_NOTIFY_SHAKE
			+ " VARCHAR(1));";

	public TbAccountUserInfoController(Context context) {
		mDbHelper = new DbHelper(context, TABLE_NAME);
		createTable();
	}

	public void createTable() {
		mDbHelper.createTable(SQL_CREATE_TB);
	}

	public synchronized void open() {
		mDbHelper.open();
	}

	public synchronized void close() {
		mDbHelper.close();
	}

	public boolean judgeIfExist(String myBareJid) {
		Cursor cursor = queryAll(myBareJid);
		int count = cursor.getCount();
		boolean flag = false;

		if (count != 0) {
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}

	public Cursor queryAllUserJid() {
		Cursor cursor = mDbHelper.query(
				new String[] { DbConstants.TB_ACCOUNT_USERINFO_JID }, null,
				null, null, null,
				DbConstants.TB_ACCOUNT_USERINFO_LAST_LOGIN_TIME + " desc");
		return cursor;
	}

	public Cursor queryAll() {
		Cursor cursor = mDbHelper.query(null, null, null, null, null,
				DbConstants.TB_ACCOUNT_USERINFO_LAST_LOGIN_TIME + " desc");
		return cursor;
	}

	public Cursor queryAll(String myBareJid) {
		Cursor cursor = mDbHelper.query(null,
				DbConstants.TB_ACCOUNT_USERINFO_JID + "=? ",
				new String[] { myBareJid }, null, null, null);
		return cursor;
	}

	public void insertOrNeededUpdate(LoginBasicInfo loginBasicInfo) {
		String bareJid = loginBasicInfo.getBareJid();
		ContentValues cv = getContentValues(loginBasicInfo);

		boolean isExist = judgeIfExist(bareJid);
		if (isExist) {
			doUpdate(bareJid, cv);
		} else {
			setDefValueToField(cv);
			doInsert(cv);
		}
	}

	private ContentValues getContentValues(LoginBasicInfo loginBasicInfo) {
		String bareJid = loginBasicInfo.getBareJid();
		String encriedPasswd = loginBasicInfo.getEncriedPasswd();
		String recentLoginTime = loginBasicInfo.getRecentLoginTime();
		String serverAddress = loginBasicInfo.getServerAddress();
		boolean isAutoLogin = loginBasicInfo.isAutoLogin();
		boolean isRememberPasswd = loginBasicInfo.isRememberPasswd();
		boolean isStartToMain = loginBasicInfo.isStartToMain();

		ContentValues cv = new ContentValues();
		cv.put(DbConstants.TB_ACCOUNT_USERINFO_JID, bareJid);
		cv.put(DbConstants.TB_ACCOUNT_USERINFO_LOGIN_PASSWD, encriedPasswd);
		cv.put(DbConstants.TB_ACCOUNT_USERINFO_LAST_LOGIN_TIME, recentLoginTime);
		cv.put(DbConstants.TB_ACCOUNT_USERINFO_LOGIN_ADDRESS, serverAddress);
		cv.put(DbConstants.TB_ACCOUNT_USERINFO_AUTO_LOGIN, isAutoLogin);
		cv.put(DbConstants.TB_ACCOUNT_USERINFO_REMEMBER_PASSWD,
				isRememberPasswd);
		cv.put(DbConstants.TB_ACCOUNT_USERINFO_LOGIN_TAG, isStartToMain);

		return cv;
	}

	private void setDefValueToField(ContentValues cv) {
		boolean isMessageNotify = true;
		cv.put(DbConstants.TB_ACCOUNT_USERINFO_MESSAGE_NOTIFY, isMessageNotify);
	}

	private LoginBasicInfo getLoginBasicInfo(Cursor cursor) {
		LoginBasicInfo info = new LoginBasicInfo();

		int jidIndex = cursor
				.getColumnIndex(DbConstants.TB_ACCOUNT_USERINFO_JID);
		String bareJid = cursor.getString(jidIndex);
		info.setBareJid(bareJid);

		int encriedPasswdIndex = cursor
				.getColumnIndex(DbConstants.TB_ACCOUNT_USERINFO_LOGIN_PASSWD);
		String encriedPasswd = cursor.getString(encriedPasswdIndex);
		info.setEncriedPasswd(encriedPasswd);

		int loginTimeIndex = cursor
				.getColumnIndex(DbConstants.TB_ACCOUNT_USERINFO_LAST_LOGIN_TIME);
		String loginTime = cursor.getString(loginTimeIndex);
		info.setRecentLoginTime(loginTime);

		int loginAddressIndex = cursor
				.getColumnIndex(DbConstants.TB_ACCOUNT_USERINFO_LOGIN_ADDRESS);
		String serverAddress = cursor.getString(loginAddressIndex);
		info.setServerAddress(serverAddress);

		int autoLoginIndex = cursor
				.getColumnIndex(DbConstants.TB_ACCOUNT_USERINFO_AUTO_LOGIN);
		String autoLogin = cursor.getString(autoLoginIndex);
		boolean isAutoLogin = getConvertedValue(autoLogin);
		info.setAutoLogin(isAutoLogin);

		int rememberPasswdIndex = cursor
				.getColumnIndex(DbConstants.TB_ACCOUNT_USERINFO_REMEMBER_PASSWD);
		String rememberPasswd = cursor.getString(rememberPasswdIndex);
		boolean isRememberPasswd = getConvertedValue(rememberPasswd);
		info.setRememberPasswd(isRememberPasswd);

		int loginTagIndex = cursor
				.getColumnIndex(DbConstants.TB_ACCOUNT_USERINFO_LOGIN_TAG);
		String loginTag = cursor.getString(loginTagIndex);
		boolean isStartToMain = getConvertedValue(loginTag);
		info.setStartToMain(isStartToMain);

		return info;
	}

	private LoginExtenedInfo getLoginExtendedInfo(Cursor cursor) {
		LoginExtenedInfo info = new LoginExtenedInfo();

		int messageNotifyIndex = cursor
				.getColumnIndex(DbConstants.TB_ACCOUNT_USERINFO_MESSAGE_NOTIFY);
		String messageNotify = cursor.getString(messageNotifyIndex);
		boolean isMessageNotify = getConvertedValue(messageNotify);
		info.setMessageNotify(isMessageNotify);

		int messageNotifyBeepIndex = cursor
				.getColumnIndex(DbConstants.TB_ACCOUNT_USERINFO_MESSAGE_NOTIFY_BEEP);
		String messageNotifyBeep = cursor.getString(messageNotifyBeepIndex);
		boolean isMessageNotifyBeep = getConvertedValue(messageNotifyBeep);
		info.setMessageNotifyBeep(isMessageNotifyBeep);

		int messageNotifyShakeIndex = cursor
				.getColumnIndex(DbConstants.TB_ACCOUNT_USERINFO_MESSAGE_NOTIFY_SHAKE);
		String messageNotifyShake = cursor.getString(messageNotifyShakeIndex);
		boolean isMessageNotifyVibrate = getConvertedValue(messageNotifyShake);
		info.setMessageNotifyVibrate(isMessageNotifyVibrate);

		int autoAcceptInviteIndex = cursor
				.getColumnIndex(DbConstants.TB_ACCOUNT_USERINFO_AUTO_ACCEPT_INVITE);
		String autoAcceptInvite = cursor.getString(autoAcceptInviteIndex);
		boolean isAutoAcceptInvite = getConvertedValue(autoAcceptInvite);
		info.setAutoAcceptInvite(isAutoAcceptInvite);

		return info;
	}

	private boolean getConvertedValue(String string) {
		boolean flag = false;

		if (string != null) {
			int dbValue = Integer.valueOf(string);
			if (dbValue == 1) {
				flag = true;
			}
		}

		return flag;
	}

	private long doInsert(ContentValues cv) {
		// 返回那个自增的ID号
		long row = mDbHelper.insert(null, cv);
		return row;
	}

	public int updateLoginTag(String myBareJid, boolean isStartToMain) {
		ContentValues cv = new ContentValues();
		cv.put(DbConstants.TB_ACCOUNT_USERINFO_LOGIN_TAG, isStartToMain);
		return doUpdate(myBareJid, cv);
	}

	public int updateMessageNotify(String myBareJid, boolean isMessageNotify) {
		ContentValues cv = new ContentValues();
		cv.put(DbConstants.TB_ACCOUNT_USERINFO_MESSAGE_NOTIFY, isMessageNotify);
		return doUpdate(myBareJid, cv);
	}

	public int updateMessageNotifyBeep(String myBareJid,
			boolean isMessageNotifyBeep) {
		ContentValues cv = new ContentValues();
		cv.put(DbConstants.TB_ACCOUNT_USERINFO_MESSAGE_NOTIFY_BEEP,
				isMessageNotifyBeep);
		return doUpdate(myBareJid, cv);
	}

	public int updateMessageNotifyShake(String myBareJid,
			boolean isMessageNotifyShake) {
		ContentValues cv = new ContentValues();
		cv.put(DbConstants.TB_ACCOUNT_USERINFO_MESSAGE_NOTIFY_SHAKE,
				isMessageNotifyShake);
		return doUpdate(myBareJid, cv);
	}

	public int updateAutoAcceptInvite(String myBareJid,
			boolean isAutoAcceptInvite) {
		ContentValues cv = new ContentValues();
		cv.put(DbConstants.TB_ACCOUNT_USERINFO_AUTO_ACCEPT_INVITE,
				isAutoAcceptInvite);
		return doUpdate(myBareJid, cv);
	}

	private int doUpdate(String myBareJid, ContentValues cv) {
		String where = DbConstants.TB_ACCOUNT_USERINFO_JID + "=? ";
		String[] whereValue = { myBareJid };

		int result = Constants.DEF_INT_VALUE;
		if (cv.size() != 0) {
			result = mDbHelper.update(cv, where, whereValue);
		}

		return result;
	}

	public ArrayList<String> getTimeDescOrderedHisAccount() {
		ArrayList<String> list = new ArrayList<String>();

		Cursor cursor = queryAllUserJid();
		if (cursor == null) {
			return list;
		}

		if (cursor.getCount() == 0) {
			return list;
		}

		int columnIndex = cursor
				.getColumnIndex(DbConstants.TB_ACCOUNT_USERINFO_JID);
		while (cursor.moveToNext()) {
			String string = cursor.getString(columnIndex);
			list.add(string);
		}

		return list;
	}

	public LoginBasicInfo getRecentLoginBasicInfo() {
		LoginBasicInfo loginBasicInfo = null;

		Cursor cursor = queryAll();
		if (cursor == null) {
			return loginBasicInfo;
		}

		if (cursor.getCount() == 0) {
			return loginBasicInfo;
		}

		boolean isFirst = cursor.moveToFirst();
		if (isFirst) {
			loginBasicInfo = getLoginBasicInfo(cursor);
		}

		return loginBasicInfo;
	}

	public LoginExtenedInfo getRecentLoginExtenedInfo() {
		LoginExtenedInfo loginExtenedInfo = null;

		Cursor cursor = queryAll();
		if (cursor == null) {
			return loginExtenedInfo;
		}

		if (cursor.getCount() == 0) {
			return loginExtenedInfo;
		}

		boolean isFirst = cursor.moveToFirst();
		if (isFirst) {
			loginExtenedInfo = getLoginExtendedInfo(cursor);
		}

		return loginExtenedInfo;
	}
}
