package com.bnutalk.util;

/**
 * Created by linxiaobai 2016-05-18
 */
import android.graphics.Bitmap;

public class UserEntity implements Comparable<UserEntity>{
	private static final int FEMAL = 1;
	private static final int MAL = 0;

	private String uid;
	private String nick;
	private int age;
	private int sex;
	private Bitmap avatar;
	private String motherTone;
	private String likeLanguage;
	private String faculty, nationality;

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	private String place;// set value:BNU
	private String voice;// not be used

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

	public int getAge() {
		return age;
	}

	public String getFaculty() {
		return faculty;
	}

	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public Bitmap getAvatar() {
		return avatar;
	}

	public void setAvatar(Bitmap avatar) {
		this.avatar = avatar;
	}

	public String getMotherTone() {
		return motherTone;
	}

	public void setMotherTone(String motherTone) {
		this.motherTone = motherTone;
	}

	public String getLikeLanguage() {
		return likeLanguage;
	}

	public void setLikeLanguage(String likeLanguage) {
		this.likeLanguage = likeLanguage;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public static int getFemal() {
		return FEMAL;
	}

	public static int getMal() {
		return MAL;
	}

	@Override
	public int compareTo(UserEntity a) {
		return this.nick.compareTo(a.nick);
	}
}
