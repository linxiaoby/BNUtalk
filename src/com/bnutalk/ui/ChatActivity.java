package com.bnutalk.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DefaultDatabaseErrorHandler;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bnutalk.server.ReadFromServThread;
import com.bnutalk.ui.R;
import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MsgAdapter;
import com.bnutalk.util.MsgEntity;
import com.bnutalk.util.MyApplication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatActivity extends Activity {
	private ListView msgListView;
	private EditText inputText;
	private Button send;
	private MsgAdapter adapter;
	private TextView userName;
	private Handler handler;
	private List<MsgEntity> msgList = new ArrayList<MsgEntity>();
	private String uid, cuid,cnick;
	private Bitmap cavatar,avatar;//contact avatar;current user avatar
	private DBopenHelper helper;
	private MyApplication myApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sendmsg);
		initMsgs();
		initView();
		getUserInfo();
		getMsgHistory();
	
		try {
			new Thread(new ReadFromServThread(handler)).start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	public void initView() {
		inputText = (EditText) findViewById(R.id.input_text);
		send = (Button) findViewById(R.id.send);
		userName=(TextView) findViewById(R.id.user_name);
		msgListView = (ListView) findViewById(R.id.msg_list_view);
		myApp=(MyApplication) getApplicationContext();
		adapter = new MsgAdapter(ChatActivity.this, R.layout.item_message, msgList);
		msgListView.setAdapter(adapter);
		
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 handleSendMsg();
			}
		});
		helper=new DBopenHelper(ChatActivity.this);
		defHandler();
	}
	public void getUserInfo()
	{
		uid=myApp.getSelfInfoList().get(0).getUid();
		Bundle bundle = this.getIntent().getExtras();
		cuid = bundle.getString("cuid");
		cnick=bundle.getString("cnick");
		cavatar=CommonUtil.Bytes2Bimap(bundle.getByteArray("cavatar"));
		avatar=myApp.getSelfInfoList().get(0).getAvatar();
		userName.setText(cnick);
	}
	public void getMsgHistory()
	{
		helper.getAllMsgHistory(uid, cuid, msgList,avatar,cavatar);
		adapter.notifyDataSetChanged();
		msgListView.setSelection(msgList.size());
	}
	public void defHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.v("handler", "handler");
				if (msg.what == 0x002) {
					String content = msg.obj.toString();
					if (!"".equals(content)) {
						MsgEntity rmsg = new MsgEntity(content, MsgEntity.TYPE_RECEIVED);
						msgList.add(rmsg);
						adapter.notifyDataSetChanged();
						msgListView.setSelection(msgList.size());
					}
				}
			}
		};
	}
	public void handleSendMsg()
	{
		String content = inputText.getText().toString();
		if (!"".equals(content)) {
			String time=CommonUtil.getCurrentTime();
			MsgEntity smsg = new MsgEntity(content, time,MsgEntity.TYPE_SENT);
			smsg.setAvatar(avatar);
			smsg.setCavatar(cavatar);
			smsg.setSendToUid(cuid);
			msgList.add(smsg);
			adapter.notifyDataSetChanged();
			inputText.setText("");
			msgListView.setSelection(msgList.size());
			
			sendMessage(smsg);//send msg to server
			smsg.setIsRead(1);
			saveMsgHistory(uid,smsg);
			
		}
	}
	/**
	 * send msgentity to server
	 * @param content
	 */
	public void sendMessage(MsgEntity msgEntity) {
		try {
			if (RecentMsgListActivity.os != null) {
				msgEntity.setFromUid(uid);
				msgEntity.setSendToUid(cuid);
			
				byte[] msg=MsgEntity.ObjectToByte(msgEntity);
				RecentMsgListActivity.os.write(msg);
				RecentMsgListActivity.os.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void saveMsgHistory(final String uid,final MsgEntity msg)
	{
		new Thread(new  Runnable() {
			public void run() {
				helper.addMsgHistory(uid,msg);//save message history to the local bd
			}
		}).start();
	}
	public void initMsgs() {
		MsgEntity msg1 = new MsgEntity("Hello guy.", MsgEntity.TYPE_RECEIVED);
		msgList.add(msg1);
		MsgEntity msg2 = new MsgEntity("Hello. Who is that?", MsgEntity.TYPE_SENT);
		msgList.add(msg2);
		MsgEntity msg3 = new MsgEntity("This is Richard.", MsgEntity.TYPE_RECEIVED);
		msgList.add(msg3);
	}

}
