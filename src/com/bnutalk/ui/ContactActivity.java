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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.app.Activity;
import android.content.Intent;
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
import com.bnutalk.server.AHttpMsgFriendDload;
import com.bnutalk.server.GetServerIp;
import com.bnutalk.server.ReadFromServThread;
import com.bnutalk.ui.LoginActivity;
import com.bnutalk.ui.R;
import com.bnutalk.ui.SignUpPersInfoActivity;
import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.ContactEntity;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MsgEntity;
import com.bnutalk.util.RecentMsgAdapter;
import com.bnutalk.util.RecentMsgEntity;
import com.google.gson.Gson;

public class ContactActivity extends Activity implements OnItemClickListener, OnScrollListener {
	private ListView listView;
	private List<ContactEntity> list;
	private int i = 0;
	private Handler handler;
	private ContactAdapter contactAdapter;

	// server operation：用于socket的成员变量
	public static OutputStream os;
	public static Socket socket;
	private String uid;
	private SharedPreferences msgListPref;
	private DBopenHelper openHepler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contacts);
		initEvent();
		// download msgfriends from server
		new AHttpGetContacts(uid, handler, list,openHepler).getContactsRequest();
	}

	public void initEvent() {
		// 匹配布局文件中的ListView控件
		uid="201211011063";
		listView = (ListView) findViewById(R.id.lsContacts);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		list = new ArrayList<ContactEntity>();
		contactAdapter = new ContactAdapter(ContactActivity.this, list);
		listView.setAdapter(contactAdapter);
		
		msgListPref = getSharedPreferences("recent_msg_list", 0);
		openHepler=new DBopenHelper(getApplicationContext());
		
		// get the current user id
//		getCurrentUid();

		// handler operation
		defHandler();
		openHepler.getContacts(list);
		contactAdapter.notifyDataSetChanged();
		if(list.size()==0)
		{
			Toast toast=Toast.makeText(ContactActivity.this, "还没有好友，赶快点击右上角添加吧", Toast.LENGTH_LONG);
			 toast.setGravity(Gravity.CENTER, 0, 0);
			 toast.show();
		}
	}

	/**
	 * define handler server operation:get msgfriend data,and update ui
	 */
	public void defHandler() {
		/* server operation:get msgfriend data,and update ui */
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.v("handler", "handler called");
				System.out.println("msgwhat" + msg.what);
				switch (msg.what) {
				case 0x001:// friend list download success
					contactAdapter.notifyDataSetChanged();
					if(list.size()==0)
					{
						Toast toast=Toast.makeText(ContactActivity.this, "还没有好友，赶快点击右上角添加吧", Toast.LENGTH_LONG);
						 toast.setGravity(Gravity.CENTER, 0, 0);
						 toast.show();
					}
					break;

				case 0x002:// there is a new message arrived
					// save message to local file

					break;
				default:
					break;
				}
			}
		};
	}

	/**
	 * get the current user uid from the local cache
	 */
	public void getCurrentUid() {
		SharedPreferences pref = getSharedPreferences("user_login", 0);
		uid = pref.getString("uid", "");
		// uid = "201211011063";
		Log.v("get current uid", uid);
	}

	// (5)事件处理监听器方法
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 获取点击ListView item中的内容信息
		ContactEntity cEntity = (ContactEntity) listView.getItemAtPosition(position);
		String fuid = cEntity.getUid();
		// 弹出Toast信息显示点击位置和内容
		Toast.makeText(ContactActivity.this, "position=" + position + " content=" + fuid, 0).show();

		// 弹出聊天窗口
		Bundle bundle = new Bundle();
		bundle.putString("uid", uid);
		bundle.putString("fuid", fuid);
		Intent intent = new Intent();
		intent.setClass(this, ChatActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		// 手指离开屏幕前，用力滑了一下
		// if (scrollState == SCROLL_STATE_FLING) {
		// Toast.makeText(MsgFriendListActivity.this, "用力滑一下",0).show();
		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("text", "滚动添加 "+i++);
		// map.put("image", R.drawable.ic_launcher);
		// list.add(map);
		// listView.setAdapter(simple_adapter);
		// simple_adapter.notifyDataSetChanged();
		// } else
		// // 停止滚动
		// if (scrollState == SCROLL_STATE_IDLE) {
		//
		// } else
		// // 正在滚动
		// if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
		//
		// }
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}
}
