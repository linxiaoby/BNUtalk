package com.bnutalk.socket;

/**
 * create by linxiaobai 2016-05-17
 */
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import com.bnutalk.ui.RecentMsgEntity;

import android.R.menu;
import android.content.ContentValues;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.widget.TableLayout;
import android.database.sqlite.SQLiteOpenHelper;

public class DBopenHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "bnutalk.db";
	private static final String TABLE_MESSAGE_HISTOTY = "message_history";
	private static final String TABLE_RECENT_MSG = "rencent_message";

	private static final int DATABASE_VERSION = 1;

	private static final String KEY_UID = "uid";
	private static final String KEY_CONTENT = "content";
	private static final String KEY_TIME = "time";
	private static final String KEY_TYPE = "type";
	private static final String KEY_ISREAD = "isread";
	private static final String KEY_AVATAR = "avatar";
	private static final String KEY_NICK = "nick";

	public DBopenHelper(Context context, String name) {
		super(context, name, null, 1);
	}

	public DBopenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists message_history (" + "uid text," + "content text," + "time text,"
				+ "type integer)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE_HISTOTY);
		this.onCreate(db);
	}

	public void updateDb() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT_MSG);
		db.execSQL("create table if not exists rencent_message" + "(uid text primary key," + "nick text,"
				+ "content text," + "time text," + "isread integer," + "avatar blob)");

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
		// Integer id = Integer.valueOf(fuid);
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

	public long addRecentMsgList(RecentMsgEntity rEntity) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Bitmap bmp = rEntity.getAvatar();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, os);

		values.put(KEY_AVATAR, os.toByteArray());
		values.put(KEY_UID, rEntity.getUid());
		values.put(KEY_NICK, rEntity.getNick());
		values.put(KEY_TIME, rEntity.getTime());
		values.put(KEY_CONTENT, rEntity.getMsgContent());
		values.put(KEY_ISREAD, rEntity.isRead());

		long res = db.insert(TABLE_RECENT_MSG, null, values);
		return res;
	}

	public void getAllRecentMsgList(List<RecentMsgEntity> list) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String sql = "select* from " + TABLE_RECENT_MSG;
		Cursor c = db.rawQuery(sql, null);
		if (c != null) {
			while (c.moveToNext()) {
				RecentMsgEntity rEntity = new RecentMsgEntity();
				rEntity.setUid(c.getString(c.getColumnIndex(KEY_UID)));
				rEntity.setMsgContent(c.getString(c.getColumnIndex(KEY_CONTENT)));
				rEntity.setNick(c.getString(c.getColumnIndex(KEY_NICK)));
				rEntity.setTime(c.getString(c.getColumnIndex(KEY_TIME)));
				rEntity.setRead(c.getInt(c.getColumnIndex(KEY_ISREAD)));

				byte[] in = c.getBlob(c.getColumnIndex(KEY_AVATAR));
				Bitmap bmp = BitmapFactory.decodeByteArray(in, 0, in.length);
				rEntity.setAvatar(bmp);
				list.add(rEntity);
			}
		}

	}
}
