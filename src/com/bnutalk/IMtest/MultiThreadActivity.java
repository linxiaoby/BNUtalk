package com.bnutalk.IMtest;

import java.io.IOException;

import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.bnutalk.ui.R;
import com.bnutalk.IMtest.ClientThread;
import com.bnutalk.http.GetServerIp;
import com.bnutalk.socket.SocketTread;

public class MultiThreadActivity extends Activity {
	private EditText input, show;
	private Button sendBtn;
	private OutputStream os;
	private Handler handler;
	private Socket socket;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imtest);
		input = (EditText) findViewById(R.id.main_et_input);
		show = (EditText) findViewById(R.id.main_et_show);
		sendBtn = (Button) findViewById(R.id.main_btn_send);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.v("handler", "����handler");
				// �����Ϣ�������߳�
				 if (msg.what == 0x234) {
				 // ����ȡ������׷����ʾ���ı�����
				 show.append("\n" + msg.obj.toString());
				 }
			}
		};
		new Thread(new Runnable() {
			public void run() {
				try {
					String servIp = new GetServerIp().getServerIp();
					int servPort = new GetServerIp().getServScoketPrt();
					socket = new Socket(servIp, servPort);
					Log.v("Socket�߳�","socket�����ɹ�");
					new Thread(new ClientThread(socket, handler)).start();
					os = socket.getOutputStream();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// ���û����ı��������������д������
					String strText = input.getText().toString();
					os.write((strText + "\r\n").getBytes());
					// ���input�ı�������
					input.setText("");
					os.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
