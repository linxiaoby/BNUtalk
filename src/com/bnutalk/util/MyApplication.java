package com.bnutalk.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

public class MyApplication extends Application {
	private List<ContactEntity> conList;
	private String uid;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		conList=new ArrayList<ContactEntity>();
	}
	public List<ContactEntity> getConList() {
		return conList;
	}

	public void setConList(List<ContactEntity> conList) {
		this.conList.clear();
		this.conList.addAll(conList);
	}
	

}
