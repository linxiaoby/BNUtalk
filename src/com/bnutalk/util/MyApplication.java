package com.bnutalk.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

public class MyApplication extends Application {
	private List<ContactEntity> conList;
	private List<UserEntity> selfInfoList;
	private String uid;
	
	@Override
	public void onCreate() {
		super.onCreate();
		conList=new ArrayList<ContactEntity>();
		selfInfoList=new ArrayList<UserEntity>();
	}
	public List<UserEntity> getSelfInfoList() {
		return selfInfoList;
	}
	public void setSelfInfoList(List<UserEntity> selfInfoList) {
		this.selfInfoList = selfInfoList;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public List<ContactEntity> getConList() {
		return conList;
	}

	public void setConList(List<ContactEntity> conList) {
		this.conList.clear();
		this.conList.addAll(conList);
	}
	

}
