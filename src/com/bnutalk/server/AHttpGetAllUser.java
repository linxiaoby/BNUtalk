package com.bnutalk.server;

import java.util.List;

import org.apache.http.Header;

import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.UserEntity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.os.Handler;
import android.os.Message;

/**
 * Creaded by linxiaobai 2016-05-18
 *
 */
public class AHttpGetAllUser {
    private static final int  SERVEREXCEPTION=0X002;
    private static final int  SUCCESS=0X001;
    
	private List<UserEntity> list;
	private String uid;
	private String strJson;
	private Handler handler;
	private Message msg=new Message();
	public AHttpGetAllUser() {

	}

	public AHttpGetAllUser(List<UserEntity> list) {
		this.list=list;
	}
	public AHttpGetAllUser(List<UserEntity> list,String uid) {
		this.list=list;
		this.uid=uid;
		this.strJson=null;
	}
	
	public AHttpGetAllUser(List<UserEntity> list,String uid,Handler handler) {
		this.list=list;
		this.uid=uid;
		this.strJson=null;
		this.handler=handler;
	}
	
	public void getAllUser() {
		String ip = new GetServerIp().getServerIp();
		String url = "http://" + ip + ":8080/web/GetAllUserServlet?&strUid=" + uid;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int status, Header[] header, byte[] response) {
				strJson=new String(response);
				CommonUtil.parseJsonUser(strJson, list);
				msg.what=SUCCESS;
				msg.obj=list;
				handler.sendMessage(msg);
			}
			
			@Override
			public void onFailure(int status, Header[] header, byte[] response, Throwable error) {
				msg.what=SERVEREXCEPTION;
				msg.obj=list;
				handler.sendMessage(msg);
			}
		});

	}

}
