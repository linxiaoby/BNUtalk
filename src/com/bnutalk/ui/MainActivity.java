package com.bnutalk.ui;

import java.io.OutputStream;
import java.net.Socket;

import com.bnutalk.server.MsgService;
import com.bnutalk.server.ServerConn;
/**
 * Create by linxiaobai on 2016-05-21
 */
import com.bnutalk.ui.R;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MyApplication;

import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract.Helpers;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
	private TabHost tabhost;
	private RadioGroup main_radiogroup;
	private RadioButton tab_icon_chats, tab_icon_contacs, tab_icon_settings;
	private Button addFriend;
	public String uid;
	private Handler handler;
	private MyApplication myApp;
	private DBopenHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		getCurUid();

	}

	public void getCurUid() {
		SharedPreferences pref = getSharedPreferences("user_login", 0);
		uid = pref.getString("uid", "");
	}

	public void initView() {
		main_radiogroup = (RadioGroup) findViewById(R.id.main_radiogroup);
		tab_icon_chats = (RadioButton) findViewById(R.id.tab_icon_chats);
		tab_icon_contacs = (RadioButton) findViewById(R.id.tab_icon_contacts);
		tab_icon_settings = (RadioButton) findViewById(R.id.tab_icon_settings);
		addFriend = (Button) findViewById(R.id.bt_main_add);

		tabhost = getTabHost();
		tabhost.addTab(
				tabhost.newTabSpec("tag1").setIndicator("0").setContent(new Intent(this, RecentMsgListActivity.class)));
		tabhost.addTab(
				tabhost.newTabSpec("tag2").setIndicator("1").setContent(new Intent(this, ContactActivity.class)));
		tabhost.addTab(
				tabhost.newTabSpec("tag3").setIndicator("2").setContent(new Intent(this, SettingsAcitivity.class)));

		addFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AddContactsActivity.class);
				startActivity(intent);
			}
		});
		checkListener checkradio = new checkListener();
		main_radiogroup.setOnCheckedChangeListener(checkradio);
		myApp = (MyApplication) getApplicationContext();

		helper = new DBopenHelper(MainActivity.this);
		startService();
	}

	public void startService() {
		Intent intent = new Intent(this, MsgService.class);
		startService(intent);
	}

	public class checkListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.tab_icon_chats:
				tabhost.setCurrentTab(0);
				break;
			case R.id.tab_icon_contacts:
				tabhost.setCurrentTab(1);
				break;
			case R.id.tab_icon_settings:
				tabhost.setCurrentTab(2);
				break;
			}
		}
	}

}
