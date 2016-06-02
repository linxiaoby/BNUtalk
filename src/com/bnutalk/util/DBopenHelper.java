package com.bnutalk.util;

/**
 * create by linxiaobai 2016-05-17
 */
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import android.R.integer;
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
import android.os.DropBoxManager;
import android.util.Log;
import android.widget.TableLayout;
import android.database.sqlite.SQLiteOpenHelper;

public class DBopenHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "bnutalk.db";
	private static final String TABLE_MESSAGE_HISTOTY = "message_history";
	private static final String TABLE_RECENT_MSG = "rencent_message";
	private static final String TABLE_USER_CARD = "user_card";
	private static final String TABLE_CONTACT = "contacts";
	private static final String TABLE_SELF_INFO = "self_info";

	private static final int DATABASE_VERSION = 1;

	private static final String KEY_UID = "uid";
	private static final String KEY_CUID = "cuid";
	private static final String KEY_CONTENT = "content";
	private static final String KEY_TIME = "time";
	private static final String KEY_TYPE = "type";
	private static final String KEY_ISREAD = "isread";
	private static final String KEY_AVATAR = "avatar";
	private static final String KEY_NICK = "nick";

	private static final String KEY_SEX = "sex";
	private static final String KEY_AGE = "age";
	private static final String KEY_FACULTY = "faculty";
	private static final String KEY_NATIONALITY = "nationality";
	private static final String KEY_NATIVE_LANGUAGE = "native_language";
	private static final String KEY_LIKE_LANGUAGE = "like_language";
	private static final String KEY_PLACE = "place";

	public DBopenHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	public DBopenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists message_history (" + "uid text,cuid text," + "content text,"
				+ "time text," + "type integer,isread integer)");

		db.execSQL("create table if not exists rencent_message" + "(uid text," + "cuid text," + "nick text,"
				+ "content text," + "time text," + "isread integer," + "avatar blob)");

		db.execSQL("create table if not exists user_card(uid text,cuid text,"
				+ "sex int,nick text,age int,faculty text," + "nationality text," + "native_language text,"
				+ "like_language text," + "place text,avatar blob)");

		db.execSQL("create table if not exists contacts(uid text,cuid text," + "sex int,nick text,age int,faculty text,"
				+ "nationality text," + "native_language text," + "like_language text," + "place text,avatar blob)");

		db.execSQL("create table if not exists self_info(uid text," + "sex int,nick text,age int,faculty text,"
				+ "nationality text," + "native_language text," + "like_language text," + "place text,avatar blob)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE_HISTOTY);
		this.onCreate(db);
	}

	/**
	 * delete database
	 * 
	 * @param context
	 * @return
	 */
	public boolean deleteDatabase(Context context) {
		return context.deleteDatabase(DATABASE_NAME);
	}

	/**
	 * 
	 */
	public void updateDb() {

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("drop table message_history");
		db.execSQL("drop table rencent_message");
		db.execSQL("drop table user_card");
		db.execSQL("drop table contacts");

		db.execSQL("create table if not exists message_history (" + "uid text,cuid text," + "content text,"
				+ "time text," + "type integer,isread integer)");

		db.execSQL("create table if not exists rencent_message" + "(uid text," + "cuid text," + "nick text,"
				+ "content text," + "time text," + "isread integer," + "avatar blob)");

		db.execSQL("create table if not exists user_card(uid text,cuid text,"
				+ "sex int,nick text,age int,faculty text," + "nationality text," + "native_language text,"
				+ "like_language text," + "place text,avatar blob)");

		db.execSQL("create table if not exists contacts(uid text,cuid text," + "sex int,nick text,age int,faculty text,"
				+ "nationality text," + "native_language text," + "like_language text," + "place text,avatar blob)");

		db.execSQL("create table if not exists self_info(uid text," + "sex int,nick text,age int,faculty text,"
				+ "nationality text," + "native_language text," + "like_language text," + "place text,avatar blob)");
	}

	/**
	 * save selfinfo to local cache
	 * 
	 * @param uid
	 * @param list
	 */
	public void addSelfInfo(String uid, List<UserEntity> list) {
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		db.execSQL("delete from " + TABLE_SELF_INFO + " where uid=" + uid);// delete
																			// first

		Iterator<UserEntity> iterator = list.iterator();
		UserEntity uEntity = new UserEntity();
		while (iterator.hasNext()) {
			uEntity = iterator.next();

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Bitmap bmp = uEntity.getAvatar();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, os);

			values.put(KEY_AVATAR, os.toByteArray());
			values.put(KEY_UID, uid);
			values.put(KEY_NICK, uEntity.getNick());
			values.put(KEY_SEX, uEntity.getSex());
			values.put(KEY_AGE, uEntity.getAge());
			values.put(KEY_FACULTY, uEntity.getFaculty());
			values.put(KEY_NATIVE_LANGUAGE, uEntity.getMotherTone());
			values.put(KEY_LIKE_LANGUAGE, uEntity.getLikeLanguage());
			values.put(KEY_PLACE, uEntity.getPlace());

			db.insert(TABLE_SELF_INFO, null, values);
		}

	}

	public void getSelfInfo(String uid, List<UserEntity> list) {
		if (list.size() != 0)
			list.clear();
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String asql = "select* from " + TABLE_SELF_INFO + " where uid=" + uid;
		Cursor c = db.rawQuery(asql, null);
		if (c != null) {
			while (c.moveToNext()) {
				UserEntity uEntity = new UserEntity();
				uEntity.setUid(c.getString(c.getColumnIndex(KEY_UID)));
				uEntity.setAge(c.getInt(c.getColumnIndex(KEY_AGE)));
				uEntity.setNick(c.getString(c.getColumnIndex(KEY_NICK)));
				uEntity.setFaculty(c.getString(c.getColumnIndex(KEY_FACULTY)));
				uEntity.setMotherTone(c.getString(c.getColumnIndex(KEY_NATIVE_LANGUAGE)));
				uEntity.setLikeLanguage(c.getString(c.getColumnIndex(KEY_LIKE_LANGUAGE)));
				uEntity.setSex(c.getInt(c.getColumnIndex(KEY_SEX)));
				uEntity.setPlace(c.getString(c.getColumnIndex(KEY_PLACE)));
				byte[] in = c.getBlob(c.getColumnIndex(KEY_AVATAR));
				Bitmap bmp = BitmapFactory.decodeByteArray(in, 0, in.length);
				uEntity.setAvatar(bmp);
				list.add(uEntity);
			}
		}
	}

	/**
	 * save user cards to local cache
	 * 
	 * @param list
	 */
	public void addUserCard(String uid, List<UserEntity> list) {

		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		db.execSQL("delete from " + TABLE_USER_CARD + " where uid=" + uid);// delete

		Iterator<UserEntity> iterator = list.iterator();
		UserEntity uEntity = new UserEntity();
		while (iterator.hasNext()) {
			uEntity = iterator.next();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Bitmap bmp = uEntity.getAvatar();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
			values.put(KEY_AVATAR, os.toByteArray());
			values.put(KEY_UID, uid);
			values.put(KEY_CUID, uEntity.getUid());
			values.put(KEY_NICK, uEntity.getNick());
			values.put(KEY_SEX, uEntity.getSex());
			values.put(KEY_AGE, uEntity.getAge());
			values.put(KEY_FACULTY, uEntity.getFaculty());
			values.put(KEY_NATIVE_LANGUAGE, uEntity.getMotherTone());
			values.put(KEY_LIKE_LANGUAGE, uEntity.getLikeLanguage());
			values.put(KEY_PLACE, uEntity.getPlace());
			values.put(KEY_NATIONALITY, uEntity.getNationality());
			db.insert(TABLE_USER_CARD, null, values);
		}
	}

	/**
	 * get user card from TABLE_USER_CARD,this will be called when adding friend
	 * 
	 * @param list
	 */
	public void getUserCard(String uid, List<UserEntity> list) {
		if (list.size() != 0)
			list.clear();
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String asql = "select* from " + TABLE_USER_CARD + " where uid=" + uid;
		Cursor c = db.rawQuery(asql, null);
		if (c != null) {
			while (c.moveToNext()) {
				UserEntity uEntity = new UserEntity();
				uEntity.setUid(c.getString(c.getColumnIndex(KEY_CUID)));
				uEntity.setAge(c.getInt(c.getColumnIndex(KEY_AGE)));
				uEntity.setNick(c.getString(c.getColumnIndex(KEY_NICK)));
				uEntity.setFaculty(c.getString(c.getColumnIndex(KEY_FACULTY)));
				uEntity.setMotherTone(c.getString(c.getColumnIndex(KEY_NATIVE_LANGUAGE)));
				uEntity.setLikeLanguage(c.getString(c.getColumnIndex(KEY_LIKE_LANGUAGE)));
				uEntity.setSex(c.getInt(c.getColumnIndex(KEY_SEX)));
				uEntity.setPlace(c.getString(c.getColumnIndex(KEY_PLACE)));
				uEntity.setNationality(c.getString(c.getColumnIndex(KEY_NATIONALITY)));
				byte[] in = c.getBlob(c.getColumnIndex(KEY_AVATAR));
				Bitmap bmp = BitmapFactory.decodeByteArray(in, 0, in.length);
				uEntity.setAvatar(bmp);
				list.add(uEntity);
			}
		}

	}

	/**
	 * save message history to local storage,this is call when a new message is
	 * sent or received
	 * 
	 * @param mEntity
	 * @return
	 */
	public long addMsgHistory(String uid, MsgEntity mEntity) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_UID, uid);
		values.put(KEY_CUID, mEntity.getSendToUid());
		values.put(KEY_CONTENT, mEntity.getContent());
		values.put(KEY_ISREAD, mEntity.getIsRead());
		values.put(KEY_TYPE, mEntity.getType());
		values.put(KEY_TIME, mEntity.getTime());

		long ret = db.insert(TABLE_MESSAGE_HISTOTY, null, values);

		db.close();
		return ret;
	}

	/**
	 * update MsgHistory from UNREAD to ISREAD
	 * 
	 * @param uid
	 * @param cuid
	 */
	public void updateMsgHistory(String uid, String cuid) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String whereClause = "uid=" + uid + " and cuid=" + cuid + " and " + KEY_ISREAD + "=" + 0;
		values.put(KEY_ISREAD, 1);
		long res = db.update(TABLE_MESSAGE_HISTOTY, values, whereClause, null);
	}

	/**
	 * 
	 * @param fuid
	 * @param list
	 */
	public void getAllMsgHistory(String uid, String cuid, List<MsgEntity> list, Bitmap avatar, Bitmap cavatar) {
		// Integer id = Integer.valueOf(fuid);
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String asql = "select* from " + TABLE_MESSAGE_HISTOTY + " where uid=" + uid + " and cuid=" + cuid;
		Cursor cursor = db.rawQuery(asql, null);

		if (cursor.moveToFirst()) {
			do {
				MsgEntity mEntity = new MsgEntity();
				int fid = cursor.getInt(0);
				mEntity.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
				mEntity.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));
				mEntity.setType(cursor.getInt(cursor.getColumnIndex(KEY_TYPE)));
				if (mEntity.getType() == MsgEntity.TYPE_RECEIVED)
					mEntity.setCavatar(cavatar);
				else {
					mEntity.setAvatar(avatar);
				}
				list.add(mEntity);
			} while (cursor.moveToNext());
		}
		db.close();
	}

	/**
	 * 
	 * @param rEntity
	 * @return
	 */
	public long addRecentMsgList(String uid, RecentMsgEntity rEntity) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Bitmap bmp = rEntity.getAvatar();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, os);

		values.put(KEY_AVATAR, os.toByteArray());
		values.put(KEY_UID, uid);
		values.put(KEY_CUID, rEntity.getUid());

		values.put(KEY_NICK, rEntity.getNick());
		values.put(KEY_TIME, rEntity.getTime());
		values.put(KEY_CONTENT, rEntity.getMsgContent());
		values.put(KEY_ISREAD, rEntity.isRead());

		long res = db.insert(TABLE_RECENT_MSG, null, values);
		return res;
	}

	public void addAllRecentMsgList(String uid, List<RecentMsgEntity> list) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		db.execSQL("delete from " + TABLE_RECENT_MSG + " where uid=" + uid);// delete
																			// first
		Iterator<RecentMsgEntity> iterator = list.iterator();
		while (iterator.hasNext()) {
			RecentMsgEntity rEntity = new RecentMsgEntity();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Bitmap bmp = rEntity.getAvatar();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, os);

			values.put(KEY_AVATAR, os.toByteArray());
			values.put(KEY_UID, uid);
			values.put(KEY_CUID, rEntity.getUid());

			values.put(KEY_NICK, rEntity.getNick());
			values.put(KEY_TIME, rEntity.getTime());
			values.put(KEY_CONTENT, rEntity.getMsgContent());
			values.put(KEY_ISREAD, rEntity.isRead());
			db.insert(TABLE_RECENT_MSG, null, values);
		}
	}

	/**
	 * 
	 * @param list
	 */
	public void getAllRecentMsgList(String uid, List<RecentMsgEntity> list) {
		if (list.size() != 0)
			list.clear();
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String asql = "select a.cuid as cuid,nick,content,time,isread,avatar from"
				+ "((select cuid,content,time,isread from"
				+ "(select distinct  cuid,content,time,isread from message_history where uid=" + uid
				+ " order by time asc)f "
				+ "group by cuid)a left outer join (select cuid,nick,avatar from contacts where uid=" + uid
				+ ")b on a.cuid=b.cuid)";
		// String sql = "select* from " + TABLE_RECENT_MSG + " where uid=" +
		// uid;
		Cursor c = db.rawQuery(asql, null);
		if (c != null) {
			while (c.moveToNext()) {
				RecentMsgEntity rEntity = new RecentMsgEntity();
				rEntity.setUid(c.getString(c.getColumnIndex(KEY_CUID)));
				String content = c.getString(c.getColumnIndex(KEY_CONTENT));
//				BufferedReader reader = new BufferedReader(new StringReader(content));
//				try {
//				content = reader.readLine();
//				} catch (IOException e) {
//					Log.v("Content split", "failed!");
//				}
				String[] a=content.split("\\n");
				if (a[0].length() > 15) {
					content = a[0].substring(0, 15) + "...";
				}
				else if(a.length>1)
					content=a[0]+"...";
				rEntity.setMsgContent(content);
				rEntity.setNick(c.getString(c.getColumnIndex(KEY_NICK)));
				rEntity.setTime(c.getString(c.getColumnIndex(KEY_TIME)));
				rEntity.setRead(c.getInt(c.getColumnIndex(KEY_ISREAD)));

				byte[] in = c.getBlob(c.getColumnIndex(KEY_AVATAR));
				if (in != null) {
					Bitmap bmp = BitmapFactory.decodeByteArray(in, 0, in.length);
					rEntity.setAvatar(bmp);
				}
				list.add(rEntity);
			}
		}

	}
	/**
	 * add a single contact to local db
	 * @param uid
	 * @param uEntity
	 */
	public void addContact(String uid,UserEntity uEntity) {
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		db.execSQL("delete from " + TABLE_CONTACT + " where uid=" + uid+" and cuid="+uEntity.getUid());// delete
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Bitmap bmp = uEntity.getAvatar();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, os);

		values.put(KEY_AVATAR, os.toByteArray());
		values.put(KEY_UID, uid);
		values.put(KEY_CUID, uEntity.getUid());
		values.put(KEY_NICK, uEntity.getNick());
		values.put(KEY_SEX, uEntity.getSex());
		values.put(KEY_AGE, uEntity.getAge());
		values.put(KEY_FACULTY, uEntity.getFaculty());
		values.put(KEY_NATIVE_LANGUAGE, uEntity.getMotherTone());
		values.put(KEY_LIKE_LANGUAGE, uEntity.getLikeLanguage());
		values.put(KEY_PLACE, uEntity.getPlace());
		values.put(KEY_NATIONALITY, uEntity.getNationality());

		db.insert(TABLE_CONTACT, null, values);
	}

	/**
	 * save user cards to local cache
	 * 
	 * @param list
	 */
	public void addAllContacts(String uid, List<UserEntity> list) {

		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		db.execSQL("delete from " + TABLE_CONTACT + " where uid=" + uid);// delete

		Iterator<UserEntity> iterator = list.iterator();
		UserEntity uEntity = new UserEntity();
		while (iterator.hasNext()) {
			uEntity = iterator.next();

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Bitmap bmp = uEntity.getAvatar();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, os);

			values.put(KEY_AVATAR, os.toByteArray());
			values.put(KEY_UID, uid);
			values.put(KEY_CUID, uEntity.getUid());
			values.put(KEY_NICK, uEntity.getNick());
			values.put(KEY_SEX, uEntity.getSex());
			values.put(KEY_AGE, uEntity.getAge());
			values.put(KEY_FACULTY, uEntity.getFaculty());
			values.put(KEY_NATIVE_LANGUAGE, uEntity.getMotherTone());
			values.put(KEY_LIKE_LANGUAGE, uEntity.getLikeLanguage());
			values.put(KEY_PLACE, uEntity.getPlace());
			values.put(KEY_NATIONALITY, uEntity.getNationality());

			db.insert(TABLE_CONTACT, null, values);
		}
	}

	/**
	 * get user card from TABLE_USER_CARD,this will be called when adding friend
	 * 
	 * @param list
	 */
	public void getContacts(String uid, List<UserEntity> list) {
		if (list.size() != 0)
			list.clear();
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String asql = "select* from " + TABLE_CONTACT + " where uid=" + uid;
		Cursor c = db.rawQuery(asql, null);
		if (c != null) {
			while (c.moveToNext()) {
				UserEntity uEntity = new UserEntity();
				uEntity.setUid(c.getString(c.getColumnIndex(KEY_CUID)));
				uEntity.setAge(c.getInt(c.getColumnIndex(KEY_AGE)));
				uEntity.setNick(c.getString(c.getColumnIndex(KEY_NICK)));
				uEntity.setFaculty(c.getString(c.getColumnIndex(KEY_FACULTY)));
				uEntity.setMotherTone(c.getString(c.getColumnIndex(KEY_NATIVE_LANGUAGE)));
				uEntity.setLikeLanguage(c.getString(c.getColumnIndex(KEY_LIKE_LANGUAGE)));
				uEntity.setSex(c.getInt(c.getColumnIndex(KEY_SEX)));
				uEntity.setPlace(c.getString(c.getColumnIndex(KEY_PLACE)));
				uEntity.setNationality(c.getString(c.getColumnIndex(KEY_NATIONALITY)));
				byte[] in = c.getBlob(c.getColumnIndex(KEY_AVATAR));
				Bitmap bmp = BitmapFactory.decodeByteArray(in, 0, in.length);
				uEntity.setAvatar(bmp);
				list.add(uEntity);
			}
		}
	}
}
