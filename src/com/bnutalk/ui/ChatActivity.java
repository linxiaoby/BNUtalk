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

import com.bnutalk.server.ReadFromServThread;
import com.bnutalk.ui.R;
import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MsgAdapter;
import com.bnutalk.util.MsgEntity;

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
	private String uid, fuid;
	
	private DBopenHelper dbOpenHelper;
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
					String time=CommonUtil.getCurrentTime();
					MsgEntity smsg = new MsgEntity(content, time,MsgEntity.TYPE_SENT);
					smsg.setSendToUid(fuid);
					msgList.add(smsg);
					adapter.notifyDataSetChanged();
					inputText.setText("");
					msgListView.setSelection(msgList.size());
					
					sendMessage(smsg);
					dbOpenHelper.addMsgHistory(smsg);//save message history to the local bd
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
		fuid = bundle.getString("fuid");
		
		dbOpenHelper=new DBopenHelper(ChatActivity.this);
		dbOpenHelper.getAllMsgHistory(fuid, msgList);//get all history message from the local db
		
		adapter = new MsgAdapter(ChatActivity.this, R.layout.item_message, msgList);
		msgListView.setAdapter(adapter);
		msgListView.setSelection(msgList.size());
		
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
	public void sendMessage(MsgEntity msgEntity) {
		try {
			if (RecentMsgListActivity.os != null) {
				msgEntity.setFromUid(uid);
				msgEntity.setSendToUid(fuid);
			
				byte[] msg=MsgEntity.ObjectToByte(msgEntity);
				RecentMsgListActivity.os.write(msg);
				RecentMsgListActivity.os.flush();
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
