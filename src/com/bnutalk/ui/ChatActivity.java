package com.bnutalk.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bnutalk.ui.R;
import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MsgAdapter;
import com.bnutalk.util.MsgEntity;
import com.bnutalk.util.MyApplication;
import com.bnutalk.util.SmsgEntity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatActivity extends Activity {
	private ListView msgListView;
	private EditText inputText;
	private Button send,back;
	private MsgAdapter adapter;
	private TextView userName;
	private Handler handler;
	private List<MsgEntity> msgList = new ArrayList<MsgEntity>();
	private String uid, cuid, cnick;
	private Bitmap cavatar, avatar;// contact avatar;current user avatar
	private DBopenHelper helper;
	private MyApplication myApp;
	private ChatReceiver chatReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sendmsg);
		initView();
		registerChatReceiver();// register receiver for listening msg receive
		getUserInfo();
		getMsgHistory();
	}

	public void initView() {
		inputText = (EditText) findViewById(R.id.input_text);
		send = (Button) findViewById(R.id.send);
		userName = (TextView) findViewById(R.id.user_name);
		msgListView = (ListView) findViewById(R.id.msg_list_view);
		back=(Button) findViewById(R.id.bt_chat_back);
		myApp = (MyApplication) getApplicationContext();
		adapter = new MsgAdapter(ChatActivity.this, R.layout.item_message, msgList);
		msgListView.setAdapter(adapter);

		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleSendMsg();
			}
		});
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChatActivity.this.finish();
			}
		});
		
		helper = new DBopenHelper(ChatActivity.this);
		defHandler();
	}

	public void registerChatReceiver() {
		// registerReceiver
		chatReceiver = new ChatReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("chatReceiver");
		registerReceiver(chatReceiver, filter);
	}

	/**
	 * listening if there is a new msg received
	 * 
	 * @author 王琳—PC
	 */
	public class ChatReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("Receiver", "ChatReceiver is called!");
			SmsgEntity sEntity = (SmsgEntity) SmsgEntity.ByteToObject(intent.getByteArrayExtra("message"));
			handleMsgReceive(sEntity);
		}

		public void handleMsgReceive(SmsgEntity sEntity) {
			// show msg
			MsgEntity msgEntity = new MsgEntity();
			msgEntity.setIsRead(MsgEntity.ISREAD);
			msgEntity.setType(MsgEntity.TYPE_RECEIVED);
			msgEntity.setSendToUid(sEntity.getFromUid());
			msgEntity.setContent(sEntity.getContent());
			msgEntity.setTime(CommonUtil.getCurrentTime());
			msgEntity.setCavatar(cavatar);
			msgList.add(msgEntity);
			adapter.notifyDataSetChanged();
//			adapter.notifyDataSetInvalidated();
			msgListView.setSelection(msgList.size());
			saveMsgHistory(uid,msgEntity);
		}
	}

	public void getUserInfo() {
		uid = myApp.getSelfInfoList().get(0).getUid();
		Bundle bundle = this.getIntent().getExtras();
		cuid = bundle.getString("cuid");
		cnick = bundle.getString("cnick");
		cavatar = CommonUtil.Bytes2Bimap(bundle.getByteArray("cavatar"));
		avatar = myApp.getSelfInfoList().get(0).getAvatar();
		userName.setText(cnick);

		myApp.setChatUid(cuid);// set the current chat contact uid
	}

	@Override
	protected void onPause() {
		super.onPause();
		myApp.setChatUid("null");// unset the current chat contact uid
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(chatReceiver);
	}

	public void getMsgHistory() {
		helper.getAllMsgHistory(uid, cuid, msgList, avatar, cavatar);
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

	public void handleSendMsg() {
		String content = inputText.getText().toString();
		if (!"".equals(content)) {
			inputText.setText("");
			String time = CommonUtil.getCurrentTime();
			MsgEntity smsg = new MsgEntity(content, time, MsgEntity.TYPE_SENT);
			smsg.setAvatar(avatar);
			smsg.setSendToUid(cuid);
			msgList.add(smsg);
			adapter.notifyDataSetChanged();
			msgListView.setSelection(msgList.size());

			sendMessage(smsg);// send msg to server
			smsg.setIsRead(MsgEntity.ISREAD);// save msg history
			saveMsgHistory(uid, smsg);
		}
	}

	/**
	 * send msgentity to server
	 * 
	 * @param content
	 */
	public void sendMessage(final MsgEntity mEntity) {
		new Thread(new Runnable() {
			public void run() {
				try {
					if (myApp.getOs() != null) {
						SmsgEntity sEntity = new SmsgEntity();
						sEntity.setFromUid(uid);
						sEntity.setSendToUid(cuid);
						sEntity.setContent(mEntity.getContent());
						sEntity.setTime(mEntity.getTime());
						byte[] msg = MsgEntity.ObjectToByte(sEntity);
						myApp.getOs().write(msg);
						myApp.getOs().flush();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void saveMsgHistory(final String uid, final MsgEntity msg) {
		new Thread(new Runnable() {
			public void run() {
				helper.addMsgHistory(uid, msg);// save message history to the
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
