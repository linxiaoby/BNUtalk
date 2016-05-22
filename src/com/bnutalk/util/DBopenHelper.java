package com.bnutalk.util;

/**
 * create by linxiaobai 2016-05-17
 */
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

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
import android.widget.TableLayout;
import android.database.sqlite.SQLiteOpenHelper;

public class DBopenHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "bnutalk.db";
	private static final String TABLE_MESSAGE_HISTOTY = "message_history";
	private static final String TABLE_RECENT_MSG = "rencent_message";
	private static final String TABLE_USER_CARD = "user_card";
	private static final int DATABASE_VERSION = 1;

	private static final String KEY_UID = "uid";
	private static final String KEY_CONTENT = "content";
	private static final String KEY_TIME = "time";
	private static final String KEY_TYPE = "type";
	private static final String KEY_ISREAD = "isread";
	private static final String KEY_AVATAR = "avatar";
	private static final String KEY_NICK = "nick";
	
	private static final String KEY_SEX="sex";
	private static final String KEY_AGE="age";
	private static final String KEY_FACULTY="faculty";
	private static final String KEY_NATIONALITY="nationality";
	private static final String KEY_NATIVE_LANGUAGE="native_language";
	private static final String KEY_LIKE_LANGUAGE="like_language";
	private static final String KEY_PLACE="place";
	
	
	
	public DBopenHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}
	
	public DBopenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists message_history (" + "uid text," + "content text," + "time text,"
				+ "type integer)");
		
		db.execSQL("create table if not exists rencent_message" + "(uid text primary key," + "nick text,"
				+ "content text," + "time text," + "isread integer," + "avatar blob)");
		
		db.execSQL("create table if not exists user_card(uid text primary key,"
				+ "sex int,nick text,age int,faculty text,"
				+ "nationality text,"
				+ "native_language text,"
				+ "like_language text,"
				+ "place text,avatar blob)");
		
		db.execSQL("create table if not exists contacts" + "(uid text primary key," + "nick text,"
				+ "nationality text," +   "avatar blob)");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE_HISTOTY);
		this.onCreate(db);
	}
	/**
	 * delete database
	 * @param context
	 * @return
	 */
	 public boolean deleteDatabase(Context context)
	 { 
		 return context.deleteDatabase(DATABASE_NAME); 
	 } 
	/**
	 * 
	 */
	public void updateDb() {
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("drop table user_card");
		db.execSQL("create table if not exists message_history (" + "uid text," + "content text," + "time text,"
				+ "type integer)");
		
		db.execSQL("create table if not exists rencent_message" + "(uid text primary key," + "nick text,"
				+ "content text," + "time text," + "isread integer," + "avatar blob)");
		
		db.execSQL("create table if not exists user_card(uid text primary key,"
				+ "sex int,nick text,age int,faculty text,"
				+ "nationality text,"
				+ "native_language text,"
				+ "like_language text,"
				+ "place text,"
				+ "avatar blob)");
		
		db.execSQL("create table if not exists contacts" + "(uid text primary key," + "nick text,"
				+ "nationality text," +   "avatar blob)");
	}
	
	/**
	 * save user cards to local cache
	 * @param list
	 */
	public void  addUserCard(List<UserEntity> list)
	{
		
		SQLiteDatabase db=this.getReadableDatabase();
		ContentValues values=new ContentValues();
		db.execSQL("delete from "+TABLE_USER_CARD);//delete first
		
		Iterator<UserEntity> iterator = list.iterator();
		UserEntity uEntity = new UserEntity();
		while (iterator.hasNext()) {
			uEntity = iterator.next();

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Bitmap bmp = uEntity.getAvatar();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, os);

			values.put(KEY_AVATAR, os.toByteArray());
			values.put(KEY_UID, uEntity.getUid());
			values.put(KEY_NICK, uEntity.getNick());
			values.put(KEY_SEX, uEntity.getSex());
			values.put(KEY_AGE, uEntity.getAge());
			values.put(KEY_FACULTY, uEntity.getFaculty());
			values.put(KEY_NATIVE_LANGUAGE, uEntity.getMotherTone());
			values.put(KEY_LIKE_LANGUAGE, uEntity.getLikeLanguage());
			values.put(KEY_PLACE, uEntity.getPlace());
			
			
			db.insert(TABLE_USER_CARD, null, values);
		}
	}
	/**
	 * get user card from TABLE_USER_CARD,this will be called when adding friend
	 * @param list
	 */
	public void getUserCard(List<UserEntity> list)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String asql = "select* from " + TABLE_USER_CARD;
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
				
				byte[] in = c.getBlob(c.getColumnIndex(KEY_AVATAR));
				Bitmap bmp = BitmapFactory.decodeByteArray(in, 0, in.length);
				uEntity.setAvatar(bmp);
				list.add(uEntity);
			}
		}
		
	}
	/**
	 * save message history to local storage,this is call when a new message is sent or received
	 * @param mEntity
	 * @return
	 */
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
	/**
	 * 
	 * @param fuid
	 * @param list
	 */
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
	/**
	 * 
	 * @param rEntity
	 * @return
	 */
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
	/**
	 * 
	 * @param list
	 */
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
	
	
	/**
	 * save contacs to local cache
	 * @param list
	 */
	public void  addContacts(List<ContactEntity> list)
	{
		
		SQLiteDatabase db=this.getReadableDatabase();
		ContentValues values=new ContentValues();
		db.execSQL("delete from "+TABLE_USER_CARD);//delete first
		
		Iterator<ContactEntity> iterator = list.iterator();
		ContactEntity cEntity = new ContactEntity();
		while (iterator.hasNext()) {
			cEntity = iterator.next();

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Bitmap bmp = cEntity.getAvatar();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, os);

			values.put(KEY_AVATAR, os.toByteArray());
			values.put(KEY_UID, cEntity.getUid());
			values.put(KEY_NICK, cEntity.getNick());
			values.put(KEY_NATIONALITY, cEntity.getNationality());
			
			db.insert(TABLE_USER_CARD, null, values);
		}
	}
	
	/**
	 * get contacs from local 
	 * @param list
	 */
	public void getContacts(List<ContactEntity> list)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String asql = "select* from " + TABLE_USER_CARD;
		Cursor c = db.rawQuery(asql, null);
		if (c != null) {
			while (c.moveToNext()) {
				ContactEntity cEntity = new ContactEntity();
				cEntity.setUid(c.getString(c.getColumnIndex(KEY_UID)));
				cEntity.setNick(c.getString(c.getColumnIndex(KEY_NICK)));
				cEntity.setNationality(c.getString(c.getColumnIndex(KEY_NATIONALITY)));
				
				byte[] in = c.getBlob(c.getColumnIndex(KEY_AVATAR));
				Bitmap bmp = BitmapFactory.decodeByteArray(in, 0, in.length);
				cEntity.setAvatar(bmp);
				
				list.add(cEntity);
			}
		}
		
	}
}
