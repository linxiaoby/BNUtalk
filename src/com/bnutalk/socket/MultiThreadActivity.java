//package com.bnutalk.socket;
//
//import java.io.IOException;
//
//import java.io.OutputStream;
//import java.net.Socket;
//import java.net.UnknownHostException;
//
//import android.R.string;
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import com.bnutalk.ui.R;
//import com.bnutalk.http.GetServerIp;
//import com.bnutalk.socket.ReadFromServThread;
//
//public class MultiThreadActivity extends Activity {
//	private EditText input, show;
//	private Button sendBtn;
//	private OutputStream os;
//	private Handler handler;
//	private Socket socket;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_sendmsg);
//		input = (EditText) findViewById(R.id.main_et_input);
//		show = (EditText) findViewById(R.id.main_et_show);
//		sendBtn = (Button) findViewById(R.id.main_btn_send);
//
//		handler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				Log.v("handler", "调用handler");
//				// 如果消息来自子线程
//				 if (msg.what == 0x234) {
//				 // 将读取的内容追加显示在文本框中
//				 show.append("\n" + msg.obj.toString());
//				 }
//			}
//		};
//		final String  uid="201211011064";
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					String servIp = new GetServerIp().getServerIp();
//					int servPort = new GetServerIp().getServScoketPrt();
//					socket = new Socket(servIp, servPort);
//					Log.v("Socket线程","socket建立成功");
//					new Thread(new ReadFromServThread(socket, handler)).start();
//					os = socket.getOutputStream();
//					//在创建socket的时候发送uid
//					os.write((uid+"\r\n").getBytes());
//					os.flush();
//				} catch (UnknownHostException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}).start();
//		final String sendToUid="201211011063";
//		sendBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				try {
//					// 将用户在文本框内输入的内容写入网络
//					String strText = input.getText().toString();
//					os.write("sendToUid".getBytes());
//					os.write((sendToUid+"\r\n").getBytes());
//					os.write((strText + "\r\n").getBytes());
//					// 清空input文本框数据
//					input.setText("");
//					os.flush();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//}
