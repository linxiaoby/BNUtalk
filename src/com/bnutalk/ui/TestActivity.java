package com.bnutalk.ui;

import java.util.Date;

import com.bnutalk.server.UpdateContactService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class TestActivity extends Activity {
	private AlarmReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(this, UpdateContactService.class);
		startService(intent);
		
		 receiver=new AlarmReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.test");
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x01:
				Log.d("handler", new Date().toString());
				break;
			default:
				break;
			}
		};
	};

	public class AlarmReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Intent i = new Intent(context, UpdateContactService.class);
			context.startService(i);
			Log.v("contact changed", new Date().toString());
			Toast.makeText(TestActivity.this, "contact changed", Toast.LENGTH_SHORT).show();
		}
	}
}
