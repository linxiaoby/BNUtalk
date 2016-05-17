package com.bnutalk.socket;
/**
 * create by linxiaobai 2016-05-17
 */
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import android.R.menu;
import android.content.ContentValues;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.widget.TableLayout;
import android.database.sqlite.SQLiteOpenHelper;

public class DBopenHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "bnutalk.db";
	private static final String TABLE_MESSAGE_HISTOTY = "message_history";
	private static final int DATABASE_VERSION = 1;

	private static final String KEY_UID = "uid";
	private static final String KEY_CONTENT = "content";
	private static final String KEY_TIME = "time";
	private static final String KEY_TYPE = "type";

	public DBopenHelper(Context context, String name) {
		super(context, name,null,1);
	}
	public DBopenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists message_history ("
				+ "uid text,"
				+ "content text,"
				+ "time text,"
				+ "type integer)");
		
		db.execSQL("create table if not exists rencent_message"
				+ "(id integer primary key,content text,time text,isread integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE_HISTOTY);
		this.onCreate(db);
	}

	public long addMsgHistory(MsgEntity mEntity) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_UID, mEntity.getSendToUid());
		values.put(KEY_CONTENT, mEntity.getContent());
		values.put(KEY_TYPE, mEntity.getType());
		values.put(KEY_TIME, mEntity.getTime());

		long ret = db.insert(TABLE_MESSAGE_HISTOTY, null, values);

		db.close();
		return ret;
	}

	public void getAllMsgHistory(String fuid, List<MsgEntity> list) {
//		Integer id = Integer.valueOf(fuid);
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String asql = "select* from " + TABLE_MESSAGE_HISTOTY + " where uid=" + fuid;
		Cursor cursor = db.rawQuery(asql, null);

		if (cursor.moveToFirst()) {
			do {
				MsgEntity mEntity = new MsgEntity();
				int fid = cursor.getInt(0);
				mEntity.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
				mEntity.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));
				mEntity.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
				list.add(mEntity);
			} while (cursor.moveToNext());
		}
		db.close();
	}
}
