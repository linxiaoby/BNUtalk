package com.bnutalk.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
/*
 * Author:by linxiaobai 2016/04/30
 * 功能：聊天好友列表
 */
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

import com.bnutalk.server.AHttpMsgFriendDload;
import com.bnutalk.server.GetServerIp;
import com.bnutalk.server.ReadFromServThread;
import com.bnutalk.ui.LoginActivity;
import com.bnutalk.ui.R;
import com.bnutalk.ui.SignUpPersInfoActivity;
import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MsgEntity;
import com.bnutalk.util.RecentMsgAdapter;
import com.bnutalk.util.RecentMsgEntity;
import com.google.gson.Gson;

public class RecentMsgListActivity extends Activity implements OnItemClickListener, OnScrollListener {
	private ListView listView;
	private List<RecentMsgEntity> list;
	private int i = 0;
	private Handler handler;
	private RecentMsgAdapter recentMsgAdapter;

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
		setContentView(R.layout.activity_recent_msglist);
		initEvent();
		// download msgfriends from server
		new AHttpMsgFriendDload(uid, handler, list,openHepler).msgFriDloadRequest();
		
		// get socket with server
		serverConn();
	}

	public void initEvent() {
		// 匹配布局文件中的ListView控件
		listView = (ListView) findViewById(R.id.lvMsgFriend);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		list = new ArrayList<RecentMsgEntity>();
		
		recentMsgAdapter = new RecentMsgAdapter(RecentMsgListActivity.this, list);
		listView.setAdapter(recentMsgAdapter);
		
		msgListPref = getSharedPreferences("recent_msg_list", 0);
		openHepler=new DBopenHelper(getApplicationContext());
		// get the current user id
		getCurrentUid();

		// handler operation
		defHandler();
		//openHepler.updateDb();
		openHepler.getAllRecentMsgList(list);
		recentMsgAdapter.notifyDataSetChanged();
		if(list.size()==0)
		{
			Toast toast=Toast.makeText(RecentMsgListActivity.this, "还没有好友，赶快点击右上角添加吧", Toast.LENGTH_LONG);
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

					listView.setAdapter(recentMsgAdapter);
					recentMsgAdapter.notifyDataSetChanged();
					break;
				case 0x002:// there is a new message arrived
					showBadge((MsgEntity) msg.obj);
					// save message to local file

					break;
				default:
					break;
				}
			}
		};
	}

	/**
	 * save allRecentMsgList to local
	 */
	public void saveRecentMsgList() {

		Gson gson = new Gson();
		String strJson = gson.toJson(list);
		SharedPreferences pref = getSharedPreferences("recent_msg_list", 0);
		Editor editor = pref.edit();
		editor.clear();
		editor.putString("allRecentMsgList", strJson);
		editor.commit();
	}

	/**
	 * get allRecentMsgList from local
	 */
	public void getAllRecentMsgList() {
		SharedPreferences pref = getSharedPreferences("recent_msg_list", 0);
		String strJson = null;
		strJson = pref.getString("allRecentMsgList", "");
		if (strJson != null)
			CommonUtil.parseJsonMsg(strJson, list);
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

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	// (5)事件处理监听器方法
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 获取点击ListView item中的内容信息
		RecentMsgEntity rEntity = (RecentMsgEntity) listView.getItemAtPosition(position);
		String fuid = rEntity.getUid();
		// 弹出Toast信息显示点击位置和内容
		Toast.makeText(RecentMsgListActivity.this, "position=" + position + " content=" + fuid, 0).show();

		// update listview:clear badge
		rEntity.setRead(RecentMsgEntity.READ);
		recentMsgAdapter.notifyDataSetChanged();

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

	/**
	 * show a badge on the specific listview item when there is a new message
	 * arrived
	 */
	public void showBadge(MsgEntity msgEntity) {
		String fuid = msgEntity.getFromUid();
		// update listview
		RecentMsgEntity re = new RecentMsgEntity();
		Iterator it = list.iterator();
		if (list != null && list.size() != 0) {
			while (it.hasNext()) {
				re = (RecentMsgEntity) it.next();
				if (re.getUid().equals(fuid)) {
					list.remove(re);
					break;
				}
			}
		}

		// get friendInfo from server
		getFriendInfo();
		re.setMsgContent(msgEntity.getContent());
		re.setTime(msgEntity.getTime());
		re.setRead(RecentMsgEntity.UNREAD);
		list.add(re);

		// sort list by time
		CommonUtil.sortListByTime(list);
		recentMsgAdapter.notifyDataSetChanged();
	}

	public void getFriendInfo() {

	}

	/*
	 * 服务器操作：建立和服务器的socket连接 功能：创建一个线程，用来建立socket==>每次加载聊天消息界面都会重新建立socket
	 */
	public void serverConn() {
		new Thread(new Runnable() {
			public void run() {
				try {
					// check network state
					boolean flag = new GetServerIp().checkNetworkState(RecentMsgListActivity.this);
					if (flag) {
						Log.v("network state", "network is  available");
					} else
						Log.v("network state", "network is unavailable");

					String servIp = new GetServerIp().getServerIp();
					int servPort = new GetServerIp().getServScoketPrt();
					socket = new Socket(servIp, servPort);
					Log.v("Socket线程", "socket建立成功");
					os = socket.getOutputStream();
					// 在创建socket的时候发送uid
					os.write((uid + "\r\n").getBytes());
					os.flush();

					// read message from server
					new Thread(new ReadFromServThread(handler)).start();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
}
