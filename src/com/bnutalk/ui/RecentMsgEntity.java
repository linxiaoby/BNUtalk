package com.bnutalk.ui;

import com.bnutalk.socket.CommonUtil;

/**
 * Created on 2016-05-15
 * Author:linxiaoby
 * recent message entity
 */
import android.graphics.Bitmap;

public class RecentMsgEntity implements Comparable<RecentMsgEntity> {
	private Bitmap avatar;
	private String uid;
	private String nick;
	private String msgContent;
	private String time;
	private boolean isRead;

	public RecentMsgEntity() {
	}

	public RecentMsgEntity(Bitmap avatar, String uid, String nick, String content, String time, boolean isRead) {
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

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	@Override
	public int compareTo(RecentMsgEntity a) {
		return CommonUtil.compareTime(this.time, a.time);
	}

}
