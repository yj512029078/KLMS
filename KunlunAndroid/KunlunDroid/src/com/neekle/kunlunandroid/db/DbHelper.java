package com.neekle.kunlunandroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	private String mTbName;

	private final static String DATABASE_NAME = "Kunlun_Db.db";
	private final static int DATABASE_VERSION = 1;

	private SQLiteDatabase mDatabase;

	public DbHelper(Context context, String tbName) throws SQLiteException {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mTbName = tbName;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		mDatabase = db;
	}

	public synchronized void open() {
		boolean isShouldOpen = false;
		if (mDatabase == null) {
			isShouldOpen = true;
		} else if (!mDatabase.isOpen()) {
			isShouldOpen = true;
		}

		if (isShouldOpen) {
			mDatabase = getWritableDatabase();
		}
	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();

		if (mDatabase != null) {
			mDatabase.close();
			mDatabase = null;
		}
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		// TODO Auto-generated method stub
		return super.getReadableDatabase();
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		return super.getWritableDatabase();
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void createTable(String sql) {
		boolean flag = IsTableExist(mTbName);

		if (!flag) {
			getWritableDatabase().execSQL(sql);
		}
	}

	public void createTableWithNotJudgeExist(String sql) {
		getWritableDatabase().execSQL(sql);
	}

	/**
	 * 判断某张表是否存在
	 * 
	 * @param tabName
	 *            表名
	 * @return
	 */
	public boolean IsTableExist(String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}

		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = this.getReadableDatabase();
			String sql = "select count(*) as c from Sqlite_master where type ='table' and name ='"
					+ tableName.trim() + "' ";
			cursor = db.rawQuery(sql, null);

			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
			db.close();
		}

		return result;
	}

	public Cursor query(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		return mDatabase.query(mTbName, columns, selection, selectionArgs,
				groupBy, having, orderBy);
	}

	public Cursor query(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		return mDatabase.query(mTbName, columns, selection, selectionArgs,
				groupBy, having, orderBy, limit);
	}

	public long insert(String nullColumnHack, ContentValues values) {
		long value = mDatabase.insert(mTbName, nullColumnHack, values);
		return value;
	}

	public int delete(String whereClause, String[] whereArgs) {
		return mDatabase.delete(mTbName, whereClause, whereArgs);
	}

	public int update(ContentValues values, String whereClause,
			String[] whereArgs) {
		return mDatabase.update(mTbName, values, whereClause, whereArgs);
	}

}
