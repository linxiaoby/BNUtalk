package com.bnutalk.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;

import android.R.integer;
import android.graphics.Bitmap;

import java.io.InputStream;


/**
 * Created On 2016/05/29
 * @author 王琳—PC
 *
 */
public class SmsgEntity implements Serializable {
	
	private static final long serialVersionUID=-3982748458049500750L;
	public static final int ISREAD=1;
	public static final int UNREAD=0;
	public static final int TYPE_RECEIVED = 0;
	public static final int TYPE_SENT = 1;
	private String fromUid, sendToUid, time;
	private String content;
	private int type;
	private int isRead;

	
	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	public  SmsgEntity() {
	}
	public SmsgEntity(String content,int type) {
		this.content = content;
		this.type = type;
	}


	public SmsgEntity(String content, String time,int type) {
		
		this.content = content;
		this.time=time;
		this.type = type;
	}

	public SmsgEntity(String fromUid, String sendToUid, String time, String content, int type) {
		this.fromUid = fromUid;
		this.sendToUid = sendToUid;
		this.time = time;
		this.content = content;
		this.type = type;
	}

	/**
	 * transfer a msgentity to byte[]
	 * 
	 * @param obj
	 * @return byte[]
	 */
	public static byte[] ObjectToByte(java.lang.Object obj) {
		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();
			bo.close();
			oo.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * transfer byte[] to msgEntity
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object ByteToObject(byte[] bytes) {
		Object obj = null;
		try {
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = oi.readObject();
			bi.close();
			oi.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}
	
	
	public String getFromUid() {
		return fromUid;
	}

	public void setFromUid(String fromUid) {
		this.fromUid = fromUid;
	}

	public String getSendToUid() {
		return sendToUid;
	}

	public void setSendToUid(String sendToUid) {
		this.sendToUid = sendToUid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static int getTypeReceived() {
		return TYPE_RECEIVED;
	}

	public static int getTypeSent() {
		return TYPE_SENT;
	}

}
