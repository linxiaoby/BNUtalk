package com.bnutalk.util;

import com.bnutalk.util.CommonUtil;
import android.graphics.Bitmap;
/**
 * Created on 2016-05-21
 * Author:linxiaoby
 * contact entity
 */
public class ContactEntity implements Comparable<ContactEntity> {
	private Bitmap avatar;
	private String uid;
	private String nick;
	private String nationality;


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


	public String getNationality() {
		return nationality;
	}


	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	/**
	 * sort by nick
	 */
	@Override
	public int compareTo(ContactEntity a) {
		return CommonUtil.compareTime(this.nick, a.nick);
	}

}
