package com.bnutalk.ui;

import android.app.Activity;
import android.database.DefaultDatabaseErrorHandler;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bnutalk.socket.MsgEntity;
import com.bnutalk.socket.MsgAdapter;
import com.bnutalk.socket.MsgEntity;
import com.bnutalk.socket.ReadFromServThread;
import com.bnutalk.ui.R;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {

	private ListView msgListView;
	private EditText inputText;
	private Button send;
	private MsgAdapter adapter;
	private Handler handler;
	private List<MsgEntity> msgList = new ArrayList<MsgEntity>();
	private String uid, sendToUid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sendmsg);

		initEvent();

		try {
			new Thread(new ReadFromServThread(handler)).start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = inputText.getText().toString();
				if (!"".equals(content)) {
					MsgEntity smsg = new MsgEntity(content, MsgEntity.TYPE_SENT);
					msgList.add(smsg);
					adapter.notifyDataSetChanged();
					msgListView.setSelection(msgList.size());
					inputText.setText("");
					
					
					sendMessage(content);
				}
			}
		});
	}

	public void initEvent() {
		initMsgs();
		inputText = (EditText) findViewById(R.id.input_text);
		send = (Button) findViewById(R.id.send);
		msgListView = (ListView) findViewById(R.id.msg_list_view);

		Bundle bundle = this.getIntent().getExtras();
		uid = bundle.getString("uid");
		sendToUid = bundle.getString("fuid");

		adapter = new MsgAdapter(ChatActivity.this, R.layout.item_message, msgList);
		msgListView.setAdapter(adapter);
		defHandler();
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
	/**
	 * send msgentity to server
	 * @param content
	 */
	public void sendMessage(String content) {
		try {
			if (RecentMsgListActivity.os != null) {
				MsgEntity msgEntity=new MsgEntity();
				msgEntity.setFromUid(uid);
				msgEntity.setSendToUid(sendToUid);
				
				// get the current date
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = sDateFormat.format(new java.util.Date());
				msgEntity.setTime(time);
				msgEntity.setContent(content);
				byte[] msg=MsgEntity.ObjectToByte(msgEntity);
				
				RecentMsgListActivity.os.write(msg);
				RecentMsgListActivity.os.flush();
				/*
				//message header:fromUid+sendToUid+date
				String fromUid=uid;
				RecentMsgListActivity.os.write((fromUid+"\r\n").getBytes());
				RecentMsgListActivity.os.write((sendToUid+"\r\n").getBytes());
				// get the current date
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
				String date = sDateFormat.format(new java.util.Date());
				RecentMsgListActivity.os.write((date + "\r\n").getBytes());
				
				//message body
				RecentMsgListActivity.os.write((content + "\r\n").getBytes());
				RecentMsgListActivity.os.flush();
				*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initMsgs() {
		MsgEntity msg1 = new MsgEntity("Hello guy.", MsgEntity.TYPE_RECEIVED);
		msgList.add(msg1);
		MsgEntity msg2 = new MsgEntity("Hello. Who is that?", MsgEntity.TYPE_SENT);
		msgList.add(msg2);
	}

}
