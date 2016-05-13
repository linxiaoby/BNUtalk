package com.bnutalk.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.bnutalk.ui.MsgFriendListActivity;

import android.os.Handler;
import android.os.Message;

/*
 * Author:linxiaobai 2016/04/30
 * ���ܣ�ѭ����ȡ��������������Ϣ��ͨ��handler֪ͨUI��ʾ  
 */
public class ReadFromServThread implements Runnable {
	private Handler handler;
	private BufferedReader br = null;// ���ڶ�ȡ��Ϣ��ͷ
	private InputStream isAll;// ���ڶ�ȡ��Ϣ����

	public ReadFromServThread(Socket socket, Handler handler) throws IOException {
		this.handler = handler;
		// br = new BufferedReader(new
		// InputStreamReader(socket.getInputStream()));
		br = new BufferedReader(new InputStreamReader(MsgFriendListActivity.socket.getInputStream()));
		isAll = MsgFriendListActivity.socket.getInputStream();
	}

	@Override
	public void run() {
		try {
			String content = null;
			byte[] b = new byte[1000];
			while (true) {
				isAll.read(b);
				if (b != null) {
					content = new String(b);
					Message msg = new Message();
					msg.what = 0x234;
					msg.obj = content;
					handler.sendMessage(msg);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}