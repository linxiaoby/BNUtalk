package com.bnutalk.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;

import com.bnutalk.server.ServerConn;
import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MyApplication;
import com.bnutalk.util.UserEntity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * created on 2016/05/26
 * @author 王琳—PC
 *Settings
 */
public class SettingsAcitivity extends Activity {
	private SharedPreferences msgListPref;
	private DBopenHelper helper;
	private String uid, nick;
	private ImageView ivAvatar;
	private TextView tvNick;
	private Handler handler;
	private Message msg;
	private MyApplication myApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settings);
		initEvent();
		getSelfInfo();
	}

	public void initEvent() {
		ivAvatar = (ImageView) findViewById(R.id.helloText);
		tvNick = (TextView) findViewById(R.id.user_name);
		helper = new DBopenHelper(SettingsAcitivity.this);
		myApp=(MyApplication) getApplicationContext();
		ivAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSelfCard();
			}
		});
		
		defHandler();
		getCurrentUid();
	}

	/**
	 * get the current user uid from the local cache
	 */
	public void getCurrentUid() {
		SharedPreferences pref = getSharedPreferences("user_login", 0);
		uid = pref.getString("uid", "");
	}

	public void getSelfInfo() {
		helper.getSelfInfo(uid, myApp.getSelfInfoList());
		// if get from local failed,then get from server
		if (myApp.getSelfInfoList().size() == 0) {
			getServerInfo(uid,handler);
		}
		else
			showSelfInfo();
	}
	public void showSelfCard()
	{
		Intent intent=new Intent();
		intent.setClass(SettingsAcitivity.this,SettingSelfCardActivity.class);
		startActivity(intent);
	}
	public void getServerInfo(String uid,final Handler thandler) {
		String ip = ServerConn.serverIp;
		String url = "http://" + ip + ":8080/web/GetSelfInfoServlet?&uid=" + uid;
		final Message tmsg=new Message();
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int status, Header[] header, byte[] response) {
				String strJson = new String(response);
				CommonUtil.parseJsonUser(strJson, myApp.getSelfInfoList());
				tmsg.what = 0x001;
				thandler.sendMessage(tmsg);
			}
			@Override
			public void onFailure(int status, Header[] header, byte[] response, Throwable error) {
				tmsg.what = 0x002;
				thandler.sendMessage(tmsg);
			}
		});
	}

	public void defHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0x001:// show self info on ui
					if (myApp.getSelfInfoList().size() != 0) {
						showSelfInfo();
						saveSelfInfo();
					}
					break;
				case 0x002:
					Toast.makeText(SettingsAcitivity.this, "unable to access server!", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
	}

	public void showSelfInfo() {
		UserEntity uEntity = new UserEntity();
		Iterator<UserEntity> it = myApp.getSelfInfoList().iterator();
		uEntity = it.next();
		ivAvatar.setImageBitmap(uEntity.getAvatar());
		tvNick.setText(uEntity.getNick());
	}
	/**
	 *open a thread to save self into  local cache 
	 */
	public void saveSelfInfo()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				helper.addSelfInfo(uid, myApp.getSelfInfoList());
			}
		}).start();
	}
}
