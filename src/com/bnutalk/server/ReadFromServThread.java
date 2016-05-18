package com.bnutalk.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import com.bnutalk.ui.RecentMsgListActivity;
import com.bnutalk.util.MsgEntity;

import android.os.Handler;
import android.os.Message;

/*
 * Author:linxiaobai 2016/04/30
 */
public class ReadFromServThread implements Runnable {
	private Handler handler;
	private BufferedReader br = null;
	private InputStream isAll = null;
	private MsgEntity msgEntity;

	public ReadFromServThread(Handler handler) throws IOException {
		this.handler = handler;
	}

	@Override
	public void run() {
		try {
			if (RecentMsgListActivity.socket != null) {
				br = new BufferedReader(new InputStreamReader(RecentMsgListActivity.socket.getInputStream()));
				isAll = RecentMsgListActivity.socket.getInputStream();
				String content = null;
				byte[] b = new byte[1000];
				while (true) {
					//get from uid
					isAll.read(b);
					if (b != null) {
						msgEntity=(MsgEntity) MsgEntity.ByteToObject(b);
//						content = new String(b);
						Message msg = new Message();
						msg.what = 0x002;
						msg.obj = msgEntity;
						handler.sendMessage(msg);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}