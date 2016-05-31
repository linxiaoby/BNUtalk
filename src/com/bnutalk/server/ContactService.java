package com.bnutalk.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MyApplication;
import com.bnutalk.util.UserEntity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class ContactService extends Service {
	private MyApplication myApp;
	private String ip;
	private String url;
	private DBopenHelper helper;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		helper = new DBopenHelper(getApplicationContext());
		myApp = (MyApplication) getApplicationContext();
		ip = ServerConn.serverIp;
		url = "http://" + ip + ":8080/web/getContactServlet?&uid=" + myApp.getUid() + "&size"
				+ myApp.getConList().size();
		super.onStart(intent, startId);
	}

	/**
	 * listening for new contacts
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					Log.v("ContactService", "Thread is called on " + new Date());
					AsyncHttpClient client = new AsyncHttpClient();
					client.get(url, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int status, Header[] header, byte[] response) {
							// Json解析
							String strJson = new String(response);
							final List<UserEntity> list = new ArrayList<UserEntity>();
							CommonUtil.parseJsonUser(strJson, list);
							new Thread(new Runnable() {
								public void run() {
									helper.addAllContacts(myApp.getUid(), list);
								}
							}).start();
							// send broadcast
							Intent intent = new Intent();
							intent.setAction("ContactReceiver");
							sendBroadcast(intent);
						}

						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
						}
					});
					try {
						Thread.sleep(1000 * 30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}
}
