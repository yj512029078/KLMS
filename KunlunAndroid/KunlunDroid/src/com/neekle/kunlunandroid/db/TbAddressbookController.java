package com.neekle.kunlunandroid.db;

import java.util.ArrayList;

import org.xmpp.myWRAP.SubscriptionType;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.data.AddressBookData;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;
import com.neekle.kunlunandroid.xmpp.data.XmppFriendPresenExtra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TbAddressbookController {
	private DbHelper mDbHelper;

	private static final int UPDATE_SUCCESS = -2;

	private final static String TABLE_NAME = "Tb_AddressBook";
	public final static String SQL_CREATE_TB = "Create table " + TABLE_NAME
			+ "(" + DbConstants.FIELD_ID
			+ " Integer primary key AUTOINCREMENT,"
			+ DbConstants.TB_ADDRESSBOOK_FIELD_MY_JID + " VARCHAR(100),"
			+ DbConstants.TB_ADDRESSBOOK_FIELD_ROOT_JID + " VARCHAR(100),"
			+ DbConstants.TB_ADDRESSBOOK_FIELD_ROOT_NAME + " VARCHAR(100),"
			+ DbConstants.TB_ADDRESSBOOK_FIELD_FILE_CONTENT + " TEXT,"
			+ DbConstants.TB_ADDRESSBOOK_FIELD_IS_SHARED + " VARCHAR(1),"
			+ DbConstants.TB_ADDRESSBOOK_FIELD_OWNER_JID + " VARCHAR(100));";

	public TbAddressbookController(Context context) {
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

	public boolean judgeIfExist(String myBareJid, String addressbookId) {
		Cursor cursor = queryAll(myBareJid, addressbookId);
		int count = cursor.getCount();
		boolean flag = false;

		if (count != 0) {
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}

	public Cursor queryAll(String myBareJid, String addressbookId) {
		Cursor cursor = mDbHelper.query(null,
				DbConstants.TB_ADDRESSBOOK_FIELD_MY_JID + "=? and "
						+ DbConstants.TB_ADDRESSBOOK_FIELD_ROOT_JID + "=? ",
				new String[] { myBareJid, addressbookId }, null, null, null);
		return cursor;
	}

	public Cursor queryAllRecordsByJid(String myBareJid) {
		Cursor cursor = mDbHelper.query(null,
				DbConstants.TB_ADDRESSBOOK_FIELD_MY_JID + "=?",
				new String[] { myBareJid }, null, null, null);
		return cursor;
	}

	public long insertOrNeededUpdate(AddressBookData data) {
		String myBareJid = data.getMyJid();
		String addsbookId = data.getAddbookId();

		Cursor cursor = queryAll(myBareJid, addsbookId);
		int count = cursor.getCount();
		boolean flag = false;
		if (count != 0) {
			flag = true;
		} else {
			flag = false;
		}

		long row = UPDATE_SUCCESS;
		ContentValues cv = getContentValues(data);
		if (!flag) {
			row = doInsert(cv);
		} else {
			cursor.moveToNext();
			doUpdate(myBareJid, addsbookId, cv);
		}

		return row;
	}

	private ContentValues getContentValues(AddressBookData data) {
		String myJid = data.getMyJid();
		String addsbookId = data.getAddbookId();
		String rootName = data.getRootName();
		String fileContent = data.getFileContent();
		boolean isShared = data.isShared();
		String ownerJid = data.getOwnerJid();

		ContentValues cv = new ContentValues();
		cv.put(DbConstants.TB_ADDRESSBOOK_FIELD_MY_JID, myJid);
		cv.put(DbConstants.TB_ADDRESSBOOK_FIELD_ROOT_JID, addsbookId);
		cv.put(DbConstants.TB_ADDRESSBOOK_FIELD_ROOT_NAME, rootName);
		cv.put(DbConstants.TB_ADDRESSBOOK_FIELD_FILE_CONTENT, fileContent);
		cv.put(DbConstants.TB_ADDRESSBOOK_FIELD_IS_SHARED, isShared);
		cv.put(DbConstants.TB_ADDRESSBOOK_FIELD_OWNER_JID, ownerJid);

		return cv;
	}

	private long doInsert(ContentValues cv) {
		// 返回那个自增的ID号
		long row = mDbHelper.insert(null, cv);
		return row;
	}

	private int doUpdate(String myBareJid, String addressbookId,
			ContentValues cv) {
		String where = DbConstants.TB_ADDRESSBOOK_FIELD_MY_JID + "=? and "
				+ DbConstants.TB_ADDRESSBOOK_FIELD_ROOT_JID + "=? ";
		String[] whereValue = { myBareJid, addressbookId };

		int result = Constants.DEF_INT_VALUE;
		if (cv.size() != 0) {
			result = mDbHelper.update(cv, where, whereValue);
		}

		return result;
	}

	public ArrayList<AddressBookData> getAddressBookDataList(String myBareJid) {
		ArrayList<AddressBookData> list = new ArrayList<AddressBookData>();
		Cursor cursor = queryAllRecordsByJid(myBareJid);

		if ((cursor == null) || (cursor.getCount() == 0)) {
			return list;
		}

		while (cursor.moveToNext()) {
			AddressBookData data = getAddressBookData(cursor);
			list.add(data);
		}

		return list;
	}

	private AddressBookData getAddressBookData(Cursor cursor) {
		int idIndex = cursor.getColumnIndex(DbConstants.FIELD_ID);
		String dbIdString = cursor.getString(idIndex);
		int dbId = Integer.valueOf(dbIdString);

		int myJidIndex = cursor
				.getColumnIndex(DbConstants.TB_ADDRESSBOOK_FIELD_MY_JID);
		String myJid = cursor.getString(myJidIndex);

		int addressbookIdIndex = cursor
				.getColumnIndex(DbConstants.TB_ADDRESSBOOK_FIELD_ROOT_JID);
		String addressbookId = cursor.getString(addressbookIdIndex);

		int addressbookNameIndex = cursor
				.getColumnIndex(DbConstants.TB_ADDRESSBOOK_FIELD_ROOT_NAME);
		String addressbookName = cursor.getString(addressbookNameIndex);

		int fileContentIndex = cursor
				.getColumnIndex(DbConstants.TB_ADDRESSBOOK_FIELD_FILE_CONTENT);
		String fileContent = cursor.getString(fileContentIndex);

		int isSharedIndex = cursor
				.getColumnIndex(DbConstants.TB_ADDRESSBOOK_FIELD_IS_SHARED);
		String isSharedString = cursor.getString(isSharedIndex);
		boolean isShared = getBoolean(isSharedString);

		int ownerJidIndex = cursor
				.getColumnIndex(DbConstants.TB_ADDRESSBOOK_FIELD_OWNER_JID);
		String ownerJid = cursor.getString(ownerJidIndex);

		AddressBookData data = new AddressBookData();
		data.setDbId(dbId);
		data.setAddbookId(addressbookId);
		data.setRootName(addressbookName);
		data.setMyJid(myJid);
		data.setFileContent(fileContent);
		data.setShared(isShared);
		data.setOwnerJid(ownerJid);

		return data;
	}

	private boolean getBoolean(String string) {
		boolean flag = false;

		if ((string == null) || (string.equals(Constants.EMPTY_STRING))) {
			return flag;
		}

		int value = Integer.valueOf(string);
		if (value == 1) {
			flag = true;
		}

		return flag;
	}

	public void updateAddressbookContent(String addressbookId,
			String addressbookXml) {
		ContentValues cv = new ContentValues();
		cv.put(DbConstants.TB_ADDRESSBOOK_FIELD_FILE_CONTENT, addressbookXml);

		update(addressbookId, cv);
	}

	private void update(String addressbookId, ContentValues cv) {
		String where = DbConstants.TB_ADDRESSBOOK_FIELD_ROOT_JID + "=? ";
		String[] whereValue = { addressbookId };

		if (cv.size() != 0) {
			mDbHelper.update(cv, where, whereValue);
		}
	}
}
