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
import android.graphics.BitmapFactory;
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

import com.bnutalk.server.ServerConn;
import com.bnutalk.ui.LoginActivity;
import com.bnutalk.ui.R;
import com.bnutalk.ui.SignUpPersInfoActivity;
import com.bnutalk.ui.ChatActivity.ChatReceiver;
import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MsgEntity;
import com.bnutalk.util.MyApplication;
import com.bnutalk.util.RecentMsgAdapter;
import com.bnutalk.util.RecentMsgEntity;
import com.bnutalk.util.SmsgEntity;
import com.google.gson.Gson;

public class RecentMsgListActivity extends Activity implements OnItemClickListener, OnScrollListener {
	private static final String TAG="RecentMsgListActivity";
	private ListView listView;
	private List<RecentMsgEntity> list;
	private int i=0;
	private Handler handler;
	private RecentMsgAdapter recentMsgAdapter;
	private String uid;
	private DBopenHelper openHepler;
	private RecentMsgReceiver recentMsgReceiver;
	private MyApplication myApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v(TAG,"onCreate() called!");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_recent_msglist);
		initEvent();
		registerRecentMsgReceiver();// register receiver for listening msg receive
	}
	
	public void initEvent() {
		// 匹配布局文件中的ListView控件
		listView = (ListView) findViewById(R.id.lvMsgFriend);
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		list = new ArrayList<RecentMsgEntity>();
		recentMsgAdapter = new RecentMsgAdapter(RecentMsgListActivity.this, list);
		listView.setAdapter(recentMsgAdapter);
		defHandler();
		openHepler=new DBopenHelper(getApplicationContext());
		myApp=(MyApplication) getApplicationContext();
		uid=myApp.getUid();
	}
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.v(TAG,"onResume() called!");
		getRecentMsg();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(recentMsgReceiver);
	}
	
	/**
	 * register receiver for listening msg receive
	 */
	public void registerRecentMsgReceiver() {
		// registerReceiver
		recentMsgReceiver = new RecentMsgReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("recentMsgReceiver");
		registerReceiver(recentMsgReceiver, filter);
	}
	
	/**
	 * listening a new msg received
	 * @author 王琳—PC
	 */
	public class RecentMsgReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("Receiver", "RecentMsgReceiver is called!");
			SmsgEntity sEntity = (SmsgEntity) SmsgEntity.ByteToObject(intent.getByteArrayExtra("message"));
			handleMsgReceive(sEntity);
		}
	}
	public void handleMsgReceive(SmsgEntity sEntity) {
		// show msg
		Toast.makeText(RecentMsgListActivity.this, "you have new messages!", Toast.LENGTH_SHORT).show();
		MsgEntity msgEntity = new MsgEntity();
		msgEntity.setIsRead(MsgEntity.UNREAD);
		msgEntity.setType(MsgEntity.TYPE_RECEIVED);
		msgEntity.setSendToUid(sEntity.getFromUid());
		msgEntity.setContent(sEntity.getContent());
		msgEntity.setTime(CommonUtil.getCurrentTime());
		openHepler.addMsgHistory(uid, msgEntity);
		getRecentMsg();
	}
	public void getRecentMsg()
	{
		Log.v("getRecentMsg","getRecentMsg is called!");
		openHepler.getAllRecentMsgList(uid,list);
		CommonUtil.sortListByTime(list);
		recentMsgAdapter.notifyDataSetChanged();
		if(list.size()==0)
		{
			Toast toast=Toast.makeText(RecentMsgListActivity.this, "find friends to chat!", Toast.LENGTH_SHORT);
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

	// (5)事件处理监听器方法
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 获取点击ListView item中的内容信息
		RecentMsgEntity rEntity = (RecentMsgEntity) listView.getItemAtPosition(position);
		final String cuid = rEntity.getUid();
		// 弹出Toast信息显示点击位置和内容

		// update listview:clear badge
		rEntity.setRead(RecentMsgEntity.READ);
		recentMsgAdapter.notifyDataSetChanged();
		//update table msgHistory,change UNREAD to ISREAD
		new Thread(new Runnable() {
			@Override
			public void run() {
				openHepler.updateMsgHistory(uid, cuid);
			}
		}).start();

		// 弹出聊天窗口
		Bundle bundle = new Bundle();
		bundle.putString("cuid", cuid);
		bundle.putString("cnick", rEntity.getNick());
		bundle.putByteArray("cavatar", CommonUtil.Bitmap2Bytes(rEntity.getAvatar()));
		Intent intent = new Intent();
		intent.setClass(this, ChatActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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
}