package com.neekle.kunlunandroid.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.xmpp.myWRAP.SubscriptionType;

import com.iflytek.a.f;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.data.ContactStarInfo;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;
import com.neekle.kunlunandroid.xmpp.data.XmppFriendPresenExtra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TbFriendsController {

	private DbHelper mDbHelper;

	private static final int UPDATE_SUCCESS = -2;
	private static final int FRIEND_STARED = 1;
	private static final int FRIEND_NORMAL = 0;

	private final static String TABLE_NAME = "Tb_Friends";
	public final static String SQL_CREATE_TB = "Create table " + TABLE_NAME
			+ "(" + DbConstants.FIELD_ID
			+ " Integer primary key AUTOINCREMENT,"
			+ DbConstants.TB_FRIENDS_FIELD_MY_JID + " VARCHAR(50),"
			+ DbConstants.TB_FRIENDS_FIELD_FRIEND_JID + " VARCHAR(50),"
			+ DbConstants.TB_FRIENDS_FIELD_NAME + " VARCHAR(50),"
			+ DbConstants.TB_FRIENDS_FIELD_NAME_PINYIN + " VARCHAR(50),"
			+ DbConstants.TB_FRIENDS_FIELD_REMARK_NAME + " VARCHAR(50),"
			+ DbConstants.TB_FRIENDS_FIELD_REMARK_NAME_PINYIN + " VARCHAR(50),"
			+ DbConstants.TB_FRIENDS_FIELD_SIGNATURE + " VARCHAR(280),"
			+ DbConstants.TB_FRIENDS_FIELD_SEX + " VARCHAR(6),"
			+ DbConstants.TB_FRIENDS_FIELD_MOBILE_PHONE + " VARCHAR(20),"
			+ DbConstants.TB_FRIENDS_FIELD_EMAIL + " VARCHAR(100),"
			+ DbConstants.TB_FRIENDS_FIELD_PHONE + " VARCHAR(20),"
			+ DbConstants.TB_FRIENDS_FIELD_HEAD_PORTRAIT + " VARCHAR(256),"
			+ DbConstants.TB_FRIENDS_FIELD_BACKGROUND_PIC + " VARCHAR(256),"
			+ DbConstants.TB_FRIENDS_FIELD_INVITE_STATE + " VARCHAR(10),"
			+ DbConstants.TB_FRIENDS_FIELD_STAR_SIGN + " VARCHAR(1),"
			+ DbConstants.TB_FRIENDS_FIELD_IGNORE_MICROBLOG + " VARCHAR(1),"
			+ DbConstants.TB_FRIENDS_FIELD_IN_BLACKLIST + " VARCHAR(1));";

	public TbFriendsController(Context context) {
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

	// 判断这条记录是否存在
	public boolean judgeIfExist(String myJid, String friendJid) {
		Cursor cursor = queryAll(myJid, friendJid);
		int count = cursor.getCount();
		boolean flag = false;

		if (count != 0) {
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}

	public Cursor queryAll(String myJid, String friendJid) {
		Cursor cursor = mDbHelper.query(null,
				DbConstants.TB_FRIENDS_FIELD_MY_JID + "=? and "
						+ DbConstants.TB_FRIENDS_FIELD_FRIEND_JID + "=? ",
				new String[] { myJid, friendJid }, null, null, null);
		return cursor;
	}

	/**
	 * 根据登陆者的jid获取其联系人
	 * 
	 * */
	public Cursor queryAllRecordsByJid(String jid) {
		Cursor cursor = mDbHelper.query(null,
				DbConstants.TB_FRIENDS_FIELD_MY_JID + "=?",
				new String[] { jid }, null, null, null);
		return cursor;
	}

	public ArrayList<ContactStarInfo> getContactStarList(String myJid) {
		ArrayList<ContactStarInfo> list = new ArrayList<ContactStarInfo>();

		Cursor cursor = mDbHelper.query(new String[] {
				DbConstants.TB_FRIENDS_FIELD_MY_JID,
				DbConstants.TB_FRIENDS_FIELD_FRIEND_JID,
				DbConstants.TB_FRIENDS_FIELD_STAR_SIGN },
				DbConstants.TB_FRIENDS_FIELD_MY_JID + "=? ",
				new String[] { myJid }, null, null, null);

		int myJidIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_MY_JID);
		int friendJidIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_FRIEND_JID);
		int starSignIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_STAR_SIGN);
		while (cursor.moveToNext()) {
			myJid = cursor.getString(myJidIndex);
			String friendJid = cursor.getString(friendJidIndex);
			String starSign = cursor.getString(starSignIndex);
			boolean isStarSign = getIsStar(starSign);

			ContactStarInfo starInfo = new ContactStarInfo(myJid, friendJid,
					isStarSign);
			list.add(starInfo);
		}

		return list;
	}

	public ArrayList<XmppFriend> getFriendList(String myJid) {
		ArrayList<XmppFriend> list = new ArrayList<XmppFriend>();
		Cursor cursor = queryAllRecordsByJid(myJid);

		if ((cursor == null) || (cursor.getCount() == 0)) {
			return list;
		}

		while (cursor.moveToNext()) {
			XmppFriend xmppFriend = getXmppFriend(cursor);
			list.add(xmppFriend);
		}

		return list;
	}

	private XmppFriend getXmppFriend(Cursor cursor) {
		int myJidIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_MY_JID);
		String myJid = cursor.getString(myJidIndex);

		int friendJidIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_FRIEND_JID);
		String friendJid = cursor.getString(friendJidIndex); 

		int nameIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_NAME);
		String name = cursor.getString(nameIndex);

		int namePinyinIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_NAME_PINYIN);
		String namePinyin = cursor.getString(namePinyinIndex);

		int remarkNameIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_REMARK_NAME);
		String remarkName = cursor.getString(remarkNameIndex);

		int remarkNamePinyinIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_REMARK_NAME_PINYIN);
		String remarkNamePinyin = cursor.getString(remarkNamePinyinIndex);

		int signatureIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_SIGNATURE);
		String signature = cursor.getString(signatureIndex);

		int sexIndex = cursor.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_SEX);
		String sex = cursor.getString(sexIndex);

		int mobilePhoneIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_MOBILE_PHONE);
		String mobilePhone = cursor.getString(mobilePhoneIndex);

		int emailIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_EMAIL);
		String email = cursor.getString(emailIndex);

		int phoneIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_PHONE);
		String phone = cursor.getString(phoneIndex);

		int headPortraitIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_HEAD_PORTRAIT);
		String headPortrait = cursor.getString(headPortraitIndex);

		int backgroundPicIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_BACKGROUND_PIC);
		String backgroundPic = cursor.getString(backgroundPicIndex);

		int inviteStateIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_INVITE_STATE);
		String inviteStateString = cursor.getString(inviteStateIndex);
		int inviteState = SubscriptionType.S10nBoth.swigValue();
		if ((inviteStateString != null)
				&& (!inviteStateString.equals(Constants.EMPTY_STRING))) {
			inviteState = Integer.valueOf(inviteStateString);
		}

		int starSignIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_STAR_SIGN);
		String starSign = cursor.getString(starSignIndex);
		boolean isStarSign = getIsTrue(starSign);

		int ignoreMicroblogIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_IGNORE_MICROBLOG);
		String ignoreMicroblog = cursor.getString(ignoreMicroblogIndex);
		boolean isIgnoreMicroBlog = getIsTrue(ignoreMicroblog);

		int inBlacklistIndex = cursor
				.getColumnIndex(DbConstants.TB_FRIENDS_FIELD_IN_BLACKLIST);
		String inBlacklist = cursor.getString(inBlacklistIndex);
		boolean isInBlacklist = getIsTrue(inBlacklist);

		XmppFriend xmppFriend = new XmppFriend();
		xmppFriend.setFriendJid(friendJid);

		XmppFriendPresenExtra friendPresenExtra = new XmppFriendPresenExtra();
		xmppFriend.setFriendPresenExtra(friendPresenExtra);

		xmppFriend.setGroupList(null);
		xmppFriend.setInviteState(inviteState);
		xmppFriend.setMemFlag(false);
		xmppFriend.setMobilePhone(mobilePhone);
		xmppFriend.setEmail(email);
		xmppFriend.setPhone(phone);
		xmppFriend.setHeadPortrait(headPortrait);
		xmppFriend.setBackgroundPic(backgroundPic);
		xmppFriend.setMyJid(myJid);

		xmppFriend.setName(name);
		ArrayList<String> namePinyinList = new ArrayList<String>();
		if (namePinyin != null) {
			namePinyinList.add(namePinyin);
		}
		xmppFriend.setNamePinyinList(namePinyinList);

		xmppFriend.setOnline(false);

		xmppFriend.setRemarkName(remarkName);
		ArrayList<String> remarkNamePinyinList = new ArrayList<String>();
		if (remarkNamePinyin != null) {
			remarkNamePinyinList.add(remarkNamePinyin);
		}
		xmppFriend.setRemarkNamePinyinList(remarkNamePinyinList);

		xmppFriend.setSignature(signature);
		xmppFriend.setSex(sex);
		xmppFriend.setStarSign(isStarSign);
		xmppFriend.setIgnoreMicroBlog(isIgnoreMicroBlog);
		xmppFriend.setInBlacklist(isInBlacklist);

		String showName = XmppOperation.getShowName(xmppFriend);
		xmppFriend.setShowName(showName);

		String showNamePinyin = XmppOperation.getShowNamePinyin(xmppFriend);
		xmppFriend.setShowNamePinyin(showNamePinyin);

		return xmppFriend;
	}

	private boolean getIsTrue(String string) {
		boolean flag = false;
		if ((string != null) && (!string.equals(Constants.EMPTY_STRING))) {
			int inBlacklistValue = Integer.valueOf(string);
			if (inBlacklistValue == 1) {
				flag = true;
			}
		}

		return flag;
	}

	/**
	 * 插入，否则更新必要的字段
	 * 
	 * */
	public long insertOrNeededUpdate(XmppFriend xmpFriend) {
		String myJid = xmpFriend.getMyJid();
		String friendJid = xmpFriend.getFriendJid();
		Cursor cursor = queryAll(myJid, friendJid);
		int count = cursor.getCount();
		boolean flag = false;
		if (count != 0) {
			flag = true;
		} else {
			flag = false;
		}

		long row = UPDATE_SUCCESS;
		ContentValues cv = getContentValues(xmpFriend);
		if (!flag) {
			row = doInsert(cv);
		} else {
			// cursor需要往下移动，后面需要使用到
			cursor.moveToNext();
			doUpdateIfNeeded(cursor, myJid, friendJid, cv);
		}

		return row;
	}

	private ContentValues getContentValues(XmppFriend xmpFriend) {
		String myJid = xmpFriend.getMyJid();
		String friendJid = xmpFriend.getFriendJid();

		String name = xmpFriend.getName();
		ArrayList<String> namePinyinList = xmpFriend.getNamePinyinList();
		String namePinyin = null;
		if ((namePinyinList != null) && (namePinyinList.size() != 0)) {
			namePinyin = namePinyinList.get(0);
		}

		String remarkName = xmpFriend.getRemarkName();
		ArrayList<String> remarkNamePinyinList = xmpFriend
				.getRemarkNamePinyinList();
		String remarkNamePinyin = null;
		if ((remarkNamePinyinList != null)
				&& (remarkNamePinyinList.size() != 0)) {
			remarkNamePinyin = remarkNamePinyinList.get(0);
		}

		String signature = xmpFriend.getSignature();
		String sex = xmpFriend.getSex();
		String mobilePhone = xmpFriend.getMobilePhone();
		String email = xmpFriend.getEmail();
		String phone = xmpFriend.getPhone();
		String headPortrait = xmpFriend.getHeadPortrait();
		String backgroundPic = xmpFriend.getBackgroundPic();
		int inviteState = xmpFriend.getInviteState();
		String inviteStateString = inviteState + "";
		String starSignstrString = getStarSign(xmpFriend);
		boolean isIgnoreMicroblog = xmpFriend.isIgnoreMicroBlog();
		String isIgnoreMicroblogString = getAsIntString(isIgnoreMicroblog);
		boolean isInBlacklist = xmpFriend.isInBlacklist();
		String isInBlacklistString = getAsIntString(isInBlacklist);

		ContentValues cv = new ContentValues();
		cv.put(DbConstants.TB_FRIENDS_FIELD_MY_JID, myJid);
		cv.put(DbConstants.TB_FRIENDS_FIELD_FRIEND_JID, friendJid);
		cv.put(DbConstants.TB_FRIENDS_FIELD_NAME, name);
		cv.put(DbConstants.TB_FRIENDS_FIELD_NAME_PINYIN, namePinyin);
		cv.put(DbConstants.TB_FRIENDS_FIELD_REMARK_NAME, remarkName);
		cv.put(DbConstants.TB_FRIENDS_FIELD_REMARK_NAME_PINYIN,
				remarkNamePinyin);
		cv.put(DbConstants.TB_FRIENDS_FIELD_SIGNATURE, signature);
		cv.put(DbConstants.TB_FRIENDS_FIELD_SEX, sex);
		cv.put(DbConstants.TB_FRIENDS_FIELD_MOBILE_PHONE, mobilePhone);
		cv.put(DbConstants.TB_FRIENDS_FIELD_EMAIL, email);
		cv.put(DbConstants.TB_FRIENDS_FIELD_PHONE, phone);
		cv.put(DbConstants.TB_FRIENDS_FIELD_HEAD_PORTRAIT, headPortrait);
		cv.put(DbConstants.TB_FRIENDS_FIELD_BACKGROUND_PIC, backgroundPic);
		cv.put(DbConstants.TB_FRIENDS_FIELD_INVITE_STATE, inviteStateString);
		cv.put(DbConstants.TB_FRIENDS_FIELD_STAR_SIGN, starSignstrString);
		cv.put(DbConstants.TB_FRIENDS_FIELD_IGNORE_MICROBLOG,
				isIgnoreMicroblogString);
		cv.put(DbConstants.TB_FRIENDS_FIELD_IN_BLACKLIST, isInBlacklistString);

		return cv;
	}

	private String getAsIntString(boolean flag) {
		String string = null;
		if (flag) {
			string = 1 + Constants.EMPTY_STRING;
		} else {
			string = 0 + Constants.EMPTY_STRING;
		}

		return string;
	}

	private String getStarSign(XmppFriend xmpFriend) {
		String starSignstrString;
		boolean isStar = xmpFriend.isStarSign();
		if (isStar) {
			starSignstrString = FRIEND_STARED + "";
		} else {
			starSignstrString = FRIEND_NORMAL + "";
		}

		return starSignstrString;
	}

	private boolean getIsStar(String starSign) {
		boolean isStar = false;

		String friendStared = FRIEND_STARED + "";
		if (starSign.trim().equals(friendStared)) {
			isStar = true;
		}

		return isStar;
	}

	private long doInsert(ContentValues cv) {
		// 返回那个自增的ID号
		long row = mDbHelper.insert(null, cv);
		return row;
	}

	private void doUpdateIfNeeded(Cursor cursor, String myJid,
			String friendJid, ContentValues cv) {
		Set<Entry<String, Object>> set = cv.valueSet();
		Iterator<Entry<String, Object>> iterator = set.iterator();

		ContentValues needToUpdateCv = new ContentValues();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			String key = entry.getKey();
			Object valueObject = entry.getValue();
			String newValue = (String) valueObject;

			int columnIndex = cursor.getColumnIndex(key);
			String oldValue = cursor.getString(columnIndex);

			if ((oldValue == null) && (newValue != null)) {
				needToUpdateCv.put(key, newValue);
			} else if ((oldValue != null) && (newValue == null)) {
				needToUpdateCv.put(key, newValue);
			} else if ((oldValue != null) && (newValue != null)
					&& !oldValue.trim().equals(newValue.trim())) {
				needToUpdateCv.put(key, newValue);
			}
		}

		update(myJid, friendJid, needToUpdateCv);
	}

	public void delete(String myJid, String friendJid) {
		String where = DbConstants.TB_FRIENDS_FIELD_MY_JID + "=? and "
				+ DbConstants.TB_FRIENDS_FIELD_FRIEND_JID + "=? ";
		String[] whereValue = { myJid, friendJid };
		mDbHelper.delete(where, whereValue);
	}

	public void updateStarSign(String myJid, String friendJid, boolean isStar) {
		String starSignString;
		if (isStar) {
			starSignString = FRIEND_STARED + "";
		} else {
			starSignString = FRIEND_NORMAL + "";
		}

		ContentValues cv = new ContentValues();
		cv.put(DbConstants.TB_FRIENDS_FIELD_STAR_SIGN, starSignString);

		update(myJid, friendJid, cv);
	}

	public void updateContactDetailMoreInfo(String myJid, String friendJid,
			String email, String mobile, String phone) {
		ContentValues cv = new ContentValues();
		cv.put(DbConstants.TB_FRIENDS_FIELD_EMAIL, email);
		cv.put(DbConstants.TB_FRIENDS_FIELD_MOBILE_PHONE, mobile);
		cv.put(DbConstants.TB_FRIENDS_FIELD_PHONE, phone);

		update(myJid, friendJid, cv);
	}

	private void update(String myJid, String friendJid, ContentValues cv) {
		String where = DbConstants.TB_FRIENDS_FIELD_MY_JID + "=? and "
				+ DbConstants.TB_FRIENDS_FIELD_FRIEND_JID + "=? ";
		String[] whereValue = { myJid, friendJid };

		if (cv.size() != 0) {
			mDbHelper.update(cv, where, whereValue);
		}
	}

}
