package com.bnutalk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bnutalk.Socket.Msg;
import com.bnutalk.Socket.MsgAdapter;
import com.bnutalk.Socket.ReadFromServThread;
import com.bnutalk.ui.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SendMsgActivity extends Activity {

	private ListView msgListView;
	private EditText inputText;
	private Button send;
	private MsgAdapter adapter;
	private Handler handler;
	private List<Msg> msgList = new ArrayList<Msg>();

	// private String[] data={"Jhon","XiaoBai"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sendmsg);
		initMsgs();
		// �������������Դ
		adapter = new MsgAdapter(SendMsgActivity.this, R.layout.item_message, msgList);
		inputText = (EditText) findViewById(R.id.input_text);
		send = (Button) findViewById(R.id.send);

		msgListView = (ListView) findViewById(R.id.msg_list_view);
		// ��ͼ����������
		msgListView.setAdapter(adapter);

		//
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.v("handler", "����handler");
				// �����Ϣ�������߳�
				if (msg.what == 0x234) {
					String content = msg.obj.toString();
					if (!"".equals(content)) {// �����Ϣ��Ϊ��
						Msg rmsg = new Msg(content, Msg.TYPE_RECEIVED);
						msgList.add(rmsg);
						adapter.notifyDataSetChanged();
						msgListView.setSelection(msgList.size());
					}
				}
			}
		};

		// ����һ���̣߳����϶�ȡ���Է���������Ϣ
		try {
			new Thread(new ReadFromServThread(MsgFriendListActivity.socket, handler)).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final String sendToUid = "201211011064";
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = inputText.getText().toString();
				if (!"".equals(content)) {// �����Ϣ��Ϊ��
					Msg smsg = new Msg(content, Msg.TYPE_SENT);
					msgList.add(smsg);
					adapter.notifyDataSetChanged();
					msgListView.setSelection(msgList.size());
					inputText.setText("");
					
					//����Ϣ����д��socket��������ڷ������˽��գ������͸�Է�����
					try {
						MsgFriendListActivity.os.write("sendToUid".getBytes());
						MsgFriendListActivity.os.write((sendToUid + "\r\n").getBytes());
						MsgFriendListActivity.os.write((content + "\r\n").getBytes());
						MsgFriendListActivity.os.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

	}

	private void initMsgs() {
		Msg msg1 = new Msg("Hello guy.", Msg.TYPE_RECEIVED);
		msgList.add(msg1);
		Msg msg2 = new Msg("Hello. Who is that?", Msg.TYPE_SENT);
		msgList.add(msg2);
	}
}
