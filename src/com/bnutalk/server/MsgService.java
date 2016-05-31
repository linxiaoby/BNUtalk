package com.bnutalk.server;

import java.io.IOException;
import java.io.InputStream;

import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MsgEntity;
import com.bnutalk.util.MyApplication;
import com.bnutalk.util.SmsgEntity;

import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.SyncStateContract.Helpers;
import android.util.Log;

public class MsgService extends Service {
	private DBopenHelper helper;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.v("MsgService", "MsgService is created!");
		helper=new DBopenHelper(getApplicationContext());
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			public void run() {
				MyApplication myApp = (MyApplication) getApplicationContext();
				String content = null;
				SmsgEntity sEntity = new SmsgEntity();
				try {
					if (myApp.getSocket() != null) {
						byte[] b = new byte[1024];
						while (true) {// loop
							// get from uid
							InputStream isAll=myApp.getSocket().getInputStream();
							isAll.read(b);
							if (b != null) {
								Log.v("msg listening", "a new msg received!");
								sEntity = (SmsgEntity) SmsgEntity.ByteToObject(b);
								handleMsgRecive(sEntity, b);
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}
	public  void handleMsgRecive(final SmsgEntity sEntity, final byte[] b) {
		final MyApplication myApp = (MyApplication) getApplicationContext();
		final Intent intent = new Intent();
		// check FromUid
	
		if ((sEntity!=null)&&sEntity.getFromUid().equals(myApp.getChatUid()))// user is chating
		{
			// send message
			intent.putExtra("message", b);
			intent.setAction("chatReceiver");
		}

		else// user is not chating with sender
		{
			// save msg to histoty,set type as "unread"
//			new Thread(new Runnable() {
//				public void run() {
//					MsgEntity msgEntity=new MsgEntity();
//					msgEntity.setIsRead(MsgEntity.UNREAD);
//					msgEntity.setType(msgEntity.TYPE_RECEIVED);
//					msgEntity.setSendToUid(msgEntity.getFromUid());
//					msgEntity.setContent(msgEntity.getContent());
//					msgEntity.setTime(msgEntity.getTime());
//					helper.addMsgHistory(myApp.getUid(), msgEntity);
//					
//					intent.putExtra("message", b);
//					intent.setAction("recentMsgReceiver");// this can be receive by Main
//				}
//			}).start();
			intent.putExtra("message", b);
			intent.setAction("recentMsgReceiver");// this can be receive by Main
		}
		sendBroadcast(intent);
	}

}
