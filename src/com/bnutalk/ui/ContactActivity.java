package com.bnutalk.ui;

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
import com.bnutalk.server.ServerConn;
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

/**
 * Created On 2016/04/30
 * 
 * @author 王琳—PC
 *
 */
public class ContactActivity extends Activity implements OnItemClickListener, OnScrollListener {
	private static final String TAG = "ContactActivity";
	private ListView listView;
	private Handler handler;
	private ContactAdapter contactAdapter;
	private String uid;
	private SharedPreferences msgListPref;
	private DBopenHelper helper;
	private MyApplication myApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		android.util.Log.v(TAG, "onCreate() called!");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contacts);
		initView();
		defHandler();
	}

	public void initView() {
		// 匹配布局文件中的ListView控件
		listView = (ListView) findViewById(R.id.lsContacts);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		myApp = (MyApplication) getApplicationContext();
		contactAdapter = new ContactAdapter(ContactActivity.this, myApp.getConList());
		listView.setAdapter(contactAdapter);
		helper = new DBopenHelper(getApplicationContext());
		uid = myApp.getUid();
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
						// saveContact();
//						showToast("you have new friends!");
						contactAdapter.notifyDataSetChanged();
					break;
				case 0x002:

					break;
				default:
					break;
				}
			}
		};
	}

	/**
	 * load contact read local first,and then read from server
	 */
	public void getContact() {
		List<UserEntity> list=new ArrayList<UserEntity>();
		helper.getContacts(uid, list);
		if (list.size() != myApp.getConList().size())
		{
		CommonUtil.sortListByNick(list);
		myApp.getConList().clear();
		myApp.getConList().addAll(list);
		contactAdapter.notifyDataSetChanged();
		}
		// else // load from server
		getServContact(handler);
	}

	/**
	 * download contacts from server
	 * 
	 * @param handler
	 */
	public void getServContact(final Handler handler) {
		String ip = ServerConn.serverIp;
		String url = "http://" + ip + ":8080/web/getContactServlet?&uid=" + uid;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int status, Header[] header, byte[] response) {
				// Json解析
				Log.v("getServContact", "success!");
				String strJson = new String(response);
				List<UserEntity> list = new ArrayList<UserEntity>();
				CommonUtil.parseJsonUser(strJson, list);
				if (list.size()!=myApp.getConList().size()) {
					Log.v("getServContact", "handler send msg!");
					CommonUtil.sortListByNick(list);
					myApp.getConList().clear();
					myApp.getConList().addAll(list);
					contactAdapter.notifyDataSetChanged();
					Message tmsg = new Message();
					tmsg.what = 0x001;
					handler.sendMessage(tmsg);
					saveContact(list);
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			}
		});
	}

	/**
	 * open a thread to save contacts into local cache
	 */
	public void saveContact(final List<UserEntity> list) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				helper.addAllContacts(uid, list);
			}
		}).start();
	}

	public void showToast(String text) {
		Toast toast = Toast.makeText(ContactActivity.this, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
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
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}
}
