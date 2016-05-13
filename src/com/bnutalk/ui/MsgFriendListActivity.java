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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.CursorJoiner.Result;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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

import com.bnutalk.http.AHttpMsgFriendDload;
import com.bnutalk.http.GetServerIp;
import com.bnutalk.socket.ReadFromServThread;
import com.bnutalk.ui.LoginActivity;
import com.bnutalk.ui.R;
import com.bnutalk.ui.SignUpPersInfoActivity;
import com.google.gson.Gson;

public class MsgFriendListActivity extends Activity implements OnItemClickListener, OnScrollListener {
	private ListView listView;
	private SimpleAdapter simple_adapter;
	private List<Map<String, Object>> list;
	private int i = 0;
	private Handler handler;
	
	// server operation：用于socket的成员变量
	public static OutputStream os;
	public static Socket socket;
	public String strUid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msgfriend_list);
		// 匹配布局文件中的ListView控件
		listView = (ListView) findViewById(R.id.lvMsgFriend);
		
		
		/*server operation:get msgfriend data,and update ui*/
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.v("handler", "handler called");
			System.out.println("msgwhat"+msg.what);
				if (msg.what == 0x001) {
					simple_adapter = new SimpleAdapter(MsgFriendListActivity.this, list, R.layout.item_megfriend_list,
							new String[] { "image", "nickname","info"}, new int[] { R.id.image, R.id.nickname,R.id.info});
					simple_adapter.setViewBinder(binder);
					listView.setAdapter(simple_adapter);
					
				}
			}
		};
		list = new ArrayList<Map<String, Object>>();
		
		
		/*download msgfriends from server*/
		strUid="201211011063";
		new AHttpMsgFriendDload(strUid,handler, list).msgFriDloadRequest();
		
		
		// 设置SimpleAdapter监听器
//		simple_adapter = new SimpleAdapter(MsgFriendListActivity.this, list, R.layout.item_megfriend_list,
//				new String[] { "image", "text" }, new int[] { R.id.image, R.id.text });
//		listView.setAdapter(simple_adapter);
//		listView.setOnScrollListener(this);

		// 服务器操作：创建一个thread，用于和服务器建立socket连接
//		serverConn();
//		// 服务器操作：创建一个thread，用于和服务器建立socket连接
	
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(this);
		serverConn();
	}
	
	ViewBinder binder=new ViewBinder() {
		@Override
		public boolean setViewValue(View view, Object data, String textRepresentation) {
			if((view instanceof ImageView)&(data instanceof Bitmap))
	        {
	            ImageView iv = (ImageView)view;
	            iv.setAdjustViewBounds(true);  
	            Bitmap bmp = (Bitmap)data;
	            iv.setImageBitmap(bmp);
	            return true;
	        }
			return false;
		}
	};
	
	// 加载SimpleAdapter数据集
	private List<Map<String, Object>> getData() {
		list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("text", "java");
		map.put("image", R.drawable.ic_launcher);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("text", "C++");
		map2.put("image", R.drawable.ic_launcher);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("text", "JavaScript");
		map3.put("image", R.drawable.ic_launcher);
		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("text", "Php");
		map4.put("image", R.drawable.ic_launcher);
		Map<String, Object> map5 = new HashMap<String, Object>();
		map5.put("text", "Python2");
		map5.put("image", R.drawable.ic_launcher);
		list.add(map);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		list.add(map5);
		Log.i("Main", list.size() + "");
		
		return list;
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
		// TODO Auto-generated method stub
		// 获取点击ListView item中的内容信息
		// String text = listView.getItemAtPosition(position) + "";
		// // 弹出Toast信息显示点击位置和内容
		// Toast.makeText(MsgFriendListActivity.this,
		// "position=" + position + " content=" + text, 0).show();
		// 弹出聊天窗口
		Bundle bundle = new Bundle();
		bundle.putString("uid", "201211011063");
		Intent intent = new Intent();
		// �O����һ��Actitity
		intent.setClass(this, SendMsgActivity.class);
		intent.putExtras(bundle);
		// �_��Activity
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
	
	/*服务器操作：建立和服务器的socket连接
	 *功能：创建一个线程，用来建立socket==>每次加载聊天消息界面都会重新建立socket
	 */
	public void serverConn() {
		final String uid = "201211011063";
		new Thread(new Runnable() {
			public void run() {
				try {
					//check network state
					boolean flag=new GetServerIp().checkNetworkState(MsgFriendListActivity.this);
					if(flag)
					{
						Log.v("network state", "network is  available");
					}
					else 
						Log.v("network state", "network is not available");
					String servIp = new GetServerIp().getServerIp();
					int servPort = new GetServerIp().getServScoketPrt();
					socket = new Socket(servIp, servPort);
					Log.v("Socket线程", "socket建立成功");
					os = socket.getOutputStream();
					// 在创建socket的时候发送uid
					os.write((uid + "\r\n").getBytes());
					os.flush();
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
