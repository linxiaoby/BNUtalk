package com.bnutalk.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

public class MyApplication extends Application {
	private List<UserEntity> conList;
	private List<UserEntity> selfInfoList;
	private String uid;
	
	@Override
	public void onCreate() {
		super.onCreate();
		conList=new ArrayList<UserEntity>();
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
	
	public List<UserEntity> getConList() {
		return conList;
	}

	public void setConList(List<UserEntity> conList) {
		this.conList.clear();
		this.conList.addAll(conList);
	}
	

}
