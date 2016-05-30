package com.bnutalk.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.bnutalk.ui.LoginActivity;
import com.bnutalk.ui.MainActivity;
import com.bnutalk.ui.RecentMsgListActivity;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MsgEntity;
import com.bnutalk.util.MyApplication;
import com.bnutalk.util.SmsgEntity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ServerConn {
	public static final String serverIp = "123.206.59.147";
	public static final int servScoketPrt = 7777;

	public int getServScoketPrt() {
		return servScoketPrt;
	}

	public String getServerIp() {
		return serverIp;
	}

	/**
	 * get network state
	 * 
	 * @param context
	 * @return true for available ,false for not available
	 */

	public boolean checkNetworkState(Context context) {
		boolean flag = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			flag = cm.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}

	/**
	 * build socket conn with server
	 * 
	 * @param uid
	 * @param context
	 */
	public static void serverConn(final String uid, final Context context,final DBopenHelper helper) {
		final Socket socket = new Socket();
		new Thread(new Runnable() {
			public void run() {
				try {
					MyApplication myApp = (MyApplication) context.getApplicationContext();
					myApp.setSocket(new Socket(new ServerConn().getServerIp(), new ServerConn().getServScoketPrt()));
					Log.v("Socket线程", "socket建立成功");
					myApp.setOs(myApp.getSocket().getOutputStream());
					myApp.setIs(myApp.getSocket().getInputStream());

					myApp.getOs().write((uid + "\r\n").getBytes());
					myApp.getOs().flush();
//					ReadFromServ(context, helper);
				} catch (UnknownHostException e) {
					System.out.println("socket conn failed!");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("socket conn failed!");
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * listening msg receive,and send broadcast
	 * 
	 * @param context
	 */
	public static void ReadFromServ(final Context context, final DBopenHelper helper) {
		new Thread(new Runnable() {
			public void run() {
				MyApplication myApp = (MyApplication) context.getApplicationContext();
				String content = null;
				SmsgEntity sEntity = new SmsgEntity();
				try {
					if (myApp.getSocket() != null) {
						byte[] b = new byte[1024];
						while (true) {// loop
							// get from uid
							myApp.getIs().read(b);
							if (b != null) {
								Log.v("msg listening", "a new msg received!");
								sEntity = (SmsgEntity) SmsgEntity.ByteToObject(b);
								handleMsgRecive(sEntity, b, context, helper);
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	public static void handleMsgRecive(final SmsgEntity sEntity, byte[] b, Context context,
			final DBopenHelper helper) {
		final MyApplication myApp = (MyApplication) context.getApplicationContext();
		final Intent intent = new Intent();
		// check FromUid
		if (sEntity.getFromUid().equals(myApp.getChatUid()))// user is chating
		{
			// send message
			intent.putExtra("message", b);
			intent.setAction("chatReceiver");
		}

		else// user is not chating with sender
		{
			// save msg to histoty,set type as "unread"
			new Thread(new Runnable() {
				public void run() {
					MsgEntity msgEntity=new MsgEntity();
					msgEntity.setIsRead(MsgEntity.UNREAD);
					msgEntity.setType(msgEntity.TYPE_RECEIVED);
					msgEntity.setSendToUid(msgEntity.getFromUid());
					msgEntity.setContent(msgEntity.getContent());
					msgEntity.setTime(msgEntity.getTime());
					helper.addMsgHistory(myApp.getUid(), msgEntity);
				}
			}).start();
			intent.setAction("recentMsgReceiver");// this can be receive by Main
		}
		context.sendBroadcast(intent);
	}
}
