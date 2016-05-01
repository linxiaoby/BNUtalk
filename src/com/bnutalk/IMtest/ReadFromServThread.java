package com.bnutalk.IMtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.bnutalk.IMtest.MsgFriendListActivity;

import android.os.Handler;
import android.os.Message;

/*
 * Author:linxiaobai 2016/04/30
 * 功能：循环读取服务器发来的消息，通过handler通知UI显示  
 */
public class ReadFromServThread implements Runnable {
	private Handler handler;
	// 锟斤拷锟竭筹拷锟斤拷锟斤拷锟絊ocket锟斤拷锟接︼拷锟斤拷锟斤拷锟斤拷锟?
	private BufferedReader br = null;// 用于读取消息报头
	private InputStream isAll;// 用于读取消息正文

	public ReadFromServThread(Socket socket, Handler handler) throws IOException {
		this.handler = handler;
		// br = new BufferedReader(new
		// InputStreamReader(socket.getInputStream()));
		br = new BufferedReader(new InputStreamReader(MsgFriendListActivity.socket.getInputStream()));
		isAll = MsgFriendListActivity.socket.getInputStream();
	}

	// @Override
	// public void run() {
	// try {
	// String content = null;
	// // 锟斤拷锟较讹拷取Socket锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	// while ((content = br.readLine()) != null) {
	// // 每锟斤拷锟斤拷锟斤拷锟斤拷锟皆凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟街拷螅锟斤拷锟斤拷锟较⑼ㄖ拷锟斤拷锟斤拷锟斤拷锟斤拷示锟斤拷锟斤拷锟?
	// Message msg = new Message();
	// msg.what = 0x234;
	// msg.obj = content;
	// handler.sendMessage(msg);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	@Override
	public void run() {
		try {
			String content = null;
			byte[] b = new byte[1000];// 一条消息的正文超过1000字节要出事儿，记得回来改
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}