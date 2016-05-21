package com.bnutalk.ui;

/**
 * Create by linxiaobai on 2016-05-21
 */
import com.bnutalk.ui.R;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initEvent();

		// tabhost.addTab(tabhost.newTabSpec("tag2").setIndicator("1").setContent(new
		// Intent(this,Activity2.class)));
		// tabhost.addTab(tabhost.newTabSpec("tag3").setIndicator("2").setContent(new
		// Intent(this,Activity3.class)));
		checkListener checkradio = new checkListener();
		main_radiogroup.setOnCheckedChangeListener(checkradio);
	}

	public void initEvent() {
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
	
		addFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MainActivity.this, AddContactsActivity.class);
				startActivity(intent);
			}
		});
	}

	// ������
	public class checkListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.tab_icon_chats:
				tabhost.setCurrentTab(0);
				// ��
				// tabhost.setCurrentTabByTag("tag1");
				break;
			case R.id.tab_icon_contacts:
				tabhost.setCurrentTab(1);
				break;
			case R.id.tab_icon_settings:
				tabhost.setCurrentTab(3);
				break;
			}

		}
	}

}
