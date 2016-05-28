package com.bnutalk.ui;

/*
 * Author:by linxiaobai 2016/04/30
 * 功能：聊天好友列表
 */
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract.Helpers;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.CursorJoiner.Result;
import android.database.DefaultDatabaseErrorHandler;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Toast;

import com.bnutalk.server.AHttpGetContacts;
import com.bnutalk.server.GetServerIp;
import com.bnutalk.server.ReadFromServThread;
import com.bnutalk.server.UpdateContactService;
import com.bnutalk.ui.LoginActivity;
import com.bnutalk.ui.R;
import com.bnutalk.ui.SignUpPersInfoActivity;
import com.bnutalk.ui.TestActivity.AlarmReceiver;
import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MsgEntity;
import com.bnutalk.util.MyApplication;
import com.bnutalk.util.RecentMsgAdapter;
import com.bnutalk.util.RecentMsgEntity;
import com.bnutalk.util.UserEntity;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ContactActivity extends Activity implements OnItemClickListener, OnScrollListener {
	private static final String TAG = "ContactActivity";
	private ListView listView;
	private Handler handler;
	private ContactAdapter contactAdapter;

	// server operation：用于socket的成员变量
	public static OutputStream os;
	public static Socket socket;
	private String uid;
	private SharedPreferences msgListPref;
	private DBopenHelper helper;
	private MyApplication myApp;
	private AlarmReceiver receiver;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		android.util.Log.v(TAG, "onCreate() called!");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contacts);
		initEvent();
	}

	public void initEvent() {
		// 匹配布局文件中的ListView控件
		listView = (ListView) findViewById(R.id.lsContacts);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		myApp = (MyApplication) getApplicationContext();
		contactAdapter = new ContactAdapter(ContactActivity.this, myApp.getConList());
		listView.setAdapter(contactAdapter);
		defHandler();
		helper=new DBopenHelper(getApplicationContext());
		getCurrentUid();

	}
	@Override
	protected void onResume() {
		super.onResume();
		android.util.Log.v(TAG, "onResume() called!");
		getContact();
	}

	@Override
	protected void onPause() {
		super.onPause();
		android.util.Log.v(TAG, "onResume() called!");
	}
	/**
	 * define handler server operation:get contacts data,and update ui
	 */
	public void defHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0x001:// contacts list download success
					if (myApp.getConList().size() == 0) 
						showToast("还没有好友，赶快点击右上角添加吧");
					else 
					{
						saveContact();
						contactAdapter.notifyDataSetChanged();
					}
					break;
				case 0x002:

					break;
				default:
					break;
				}
			}
		};
	}
	public void getContact() {
		helper.getContacts(uid, myApp.getConList());
		if (myApp.getConList().size() != 0)
			contactAdapter.notifyDataSetChanged();
		else // load from server
			getServContact(handler);
	}
	/**
	 * download contacts from server
	 * @param handler
	 */
	public void getServContact(final Handler handler) {
		String ip = GetServerIp.serverIp;
		String url = "http://" + ip + ":8080/web/getContactServlet?&uid=" + uid;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int status, Header[] header, byte[] response) {
				// Json解析
				String strJson = new String(response);
				List<UserEntity> list = new ArrayList<UserEntity>();
				CommonUtil.parseJsonUser(strJson,myApp.getConList());
				Message tmsg=new Message();
				tmsg.what=0x001;
				handler.sendMessage(tmsg);
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			}
		});
	}
	/**
	 *open a thread to save contacts into  local cache 
	 */
	public void saveContact()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {
				helper.addContacts(uid, myApp.getConList());
			}
		}).start();
	}
	
	public void showToast(String text)
	{
		Toast toast = Toast.makeText(ContactActivity.this,text,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	/**
	 * get the current user uid from the local cache
	 */
	public void getCurrentUid() {
		SharedPreferences pref = getSharedPreferences("user_login", 0);
		uid = pref.getString("uid", "");
	}

	/**
	 * show person info card
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Bundle bundle = new Bundle();
		bundle.putInt("index", position);
		Intent intent = new Intent();
		intent.setClass(this, ContactInfoCardActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
}
