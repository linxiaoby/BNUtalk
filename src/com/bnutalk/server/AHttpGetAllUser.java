package com.bnutalk.server;

import java.util.List;

import org.apache.http.Header;

import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.DBopenHelper;
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
    public static final int  SERVEREXCEPTION=0X002;
    public static final int  SUCCESS=0X001;
    
	private List<UserEntity> list;
	private String uid;
	private String strJson;
	private Handler handler;
	private Message msg=new Message();
	private DBopenHelper helper;
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
	
	public AHttpGetAllUser(List<UserEntity> list,String uid,Handler handler,DBopenHelper helper) {
		this.list=list;
		this.uid=uid;
		this.strJson=null;
		this.handler=handler;
		this.helper=helper;
	}
	
	public  void getAllUser() {
		uid="201211011063";
		String ip = new GetServerIp().getServerIp();
		String url = "http://" + ip + ":8080/web/GetAllUserServlet?&uid=" + uid;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int status, Header[] header, byte[] response) {
				strJson=new String(response);
				CommonUtil.parseJsonUser(strJson, list);
				msg.what=SUCCESS;
				msg.obj=list;
				handler.sendMessage(msg);
				
				//save data to local info
				helper.addUserCard(list);
			}
			
			@Override
			public void onFailure(int status, Header[] header, byte[] response, Throwable error) {
//				msg.what=SERVEREXCEPTION;
//				handler.sendMessage(msg);
			}
		});

	}

}
