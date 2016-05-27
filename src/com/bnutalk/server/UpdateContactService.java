package com.bnutalk.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.bnutalk.ui.ContactActivity;
import com.bnutalk.ui.TestActivity;
import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.MyApplication;
import com.bnutalk.util.UserEntity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class UpdateContactService extends Service {
	private List<UserEntity> list=new ArrayList<UserEntity>();
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	private String uid;
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
////		List<ContactEntity> list;
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				MyApplication myApp=(MyApplication) getApplicationContext();
//				String ip = new GetServerIp().getServerIp();
//				String url = "http://" + ip + ":8080/web/getContactServlet?&uid=" + myApp.getUid();
//				Log.d("UpdateContactService", "executed at "+new Date().toString()+myApp.getUid());
//				try {
//					URL httpUrl = new URL(url);
//					HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
//					conn.setRequestMethod("GET");
//					conn.setReadTimeout(5000);
//					
//					BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
//					String strJson=changeInputString(conn.getInputStream());  
//					CommonUtil.parseJsonContact(strJson,list);
////					myApp.setConList(list);
//					
//					Log.v("myApp.conList", "size is"+myApp.getConList().size());
//					myApp.getConList().clear();
//					Log.v("myApp.conList", "size is"+myApp.getConList().size());
//					myApp.getConList().addAll(list);
//					Collections.sort(list);
//					Log.v("myApp.conList", "size is"+myApp.getConList().size());
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//		
//		/*start a thread*/
//		AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
//		int anHour=1000*60;//1min
//		long triggerAtTime=SystemClock.elapsedRealtime()+anHour;
////		Intent i=new Intent(this, );
//		Intent in=new Intent("android.intent.action.contact");
////		in.putExtra("list", )
//		PendingIntent pi=PendingIntent.getBroadcast(this, 0,in, 0);
//		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime,pi);
////		return super.onStartCommand(intent, flags, startId);
//		return super.onStartCommand(intent, Service.START_REDELIVER_INTENT, startId);
//
//	}
	
	private String changeInputString(InputStream inputStream) {  
        
        String jsonString="";  
        ByteArrayOutputStream outPutStream=new ByteArrayOutputStream();  
        byte[] data=new byte[1024];  
        int len=0;  
        try {  
            while((len=inputStream.read(data))!=-1){  
                outPutStream.write(data, 0, len);  
            }  
            jsonString=new String(outPutStream.toByteArray());  
          
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return jsonString;  
    }  
}
