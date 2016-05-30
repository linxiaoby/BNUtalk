package com.bnutalk.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;

public class MyApplication extends Application {
	private List<UserEntity> conList;
	private List<UserEntity> selfInfoList;
	

	private String uid;
	private Socket socket;
	private OutputStream os;
	private InputStream is;
	private String chatUid;//current chat contact uid
	@Override
	public void onCreate() {
		super.onCreate();
		conList = new ArrayList<UserEntity>();
		selfInfoList = new ArrayList<UserEntity>();
		socket = new Socket();
		chatUid="null";
	}
	public String getChatUid() {
		return chatUid;
	}
	public void setChatUid(String chatUid) {
		this.chatUid = chatUid;
	}
	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public OutputStream getOs() {
		return os;
	}

	public void setOs(OutputStream os) {
		this.os = os;
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
