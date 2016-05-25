package com.bnutalk.util;

import com.bnutalk.util.CommonUtil;

/**
 * Created on 2016-05-15
 * Author:linxiaoby
 * recent message entity
 */
import android.graphics.Bitmap;

public class RecentMsgEntity implements Comparable<RecentMsgEntity> {
	public static final int READ=1;
	public static final int UNREAD=0;
	private Bitmap avatar;
	private String uid;
	private String nick;
	private String msgContent;
	private String time;
	private int isRead;
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public RecentMsgEntity() {
	}

	public RecentMsgEntity(Bitmap avatar, String uid, String nick, String content, String time, int isRead) {
		this.avatar = avatar;
		this.uid = uid;
		this.nick = nick;
		this.msgContent = content;
		this.time = time;
		this.isRead=isRead;
	}

	public Bitmap getAvatar() {
		return avatar;
	}

	public void setAvatar(Bitmap avatar) {
		this.avatar = avatar;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int isRead() {
		return isRead;
	}

	public void setRead(int i) {
		this.isRead = i;
	}

	@Override
	public int compareTo(RecentMsgEntity a) {
		return CommonUtil.compareTime(a.time,this.time);
	}

}
