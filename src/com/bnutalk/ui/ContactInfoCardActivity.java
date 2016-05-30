package com.bnutalk.ui;

import org.apache.http.Header;

import com.bnutalk.server.ServerConn;
import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MyApplication;
import com.bnutalk.util.UserEntity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactInfoCardActivity extends Activity {
	private DBopenHelper helper;
	private String uid,cuid,nick;
	private ImageView ivAvatar,ivSex;
	private TextView tvNick,tvAge,tvMother,tvLike,tvPlace,tvFaculty;
	private Button btChat;
	private Handler handler;
	private UserEntity uEntity=new UserEntity();
	private MyApplication myApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.item_other_personal_info);
		initEvent();
		getInfoCard();
	}

	public void initEvent() {
		ivAvatar = (ImageView) findViewById(R.id.helloText);
		tvNick = (TextView) findViewById(R.id.card_name);
		tvFaculty = (TextView) findViewById(R.id.card_college);
		ivSex = (ImageView) findViewById(R.id.iv_card_gender);
		tvAge = (TextView) findViewById(R.id.card_year);
		tvPlace = (TextView) findViewById(R.id.card_address);
		tvLike = (TextView) findViewById(R.id.card_want_lanuage);
		tvMother = (TextView) findViewById(R.id.card_mother_lanuage);
		btChat=(Button) findViewById(R.id.bt_chat);
		
		
		myApp=(MyApplication) getApplicationContext();
		Bundle bundle = this.getIntent().getExtras();
		int index = bundle.getInt("index");
		uEntity=myApp.getConList().get(index);
		
		uid=myApp.getUid();
		cuid=uEntity.getUid();
		btChat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doChat();
			}
		});
	}

	public void getInfoCard()
	{
		if(uEntity.getUid()==null)
		{
			
		}
		else 
		{
			showInfoCard();
			saveInfoCard();
		}
	}
	public void showInfoCard()
	{
		ivAvatar.setImageBitmap(uEntity.getAvatar());
		tvNick.setText(uEntity.getNick());
		tvFaculty.setText(uEntity.getFaculty());
		tvAge.setText(String.valueOf(uEntity.getAge()));
		tvPlace.setText(uEntity.getPlace());
		tvMother.setText(uEntity.getMotherTone());
		tvLike.setText(uEntity.getLikeLanguage());
	        if(uEntity.getSex()==0)
	        	ivSex.setImageResource(R.drawable.man);
	        else
	        	ivSex.setImageResource(R.drawable.woman);
	}
	public void saveInfoCard()
	{
		
		
	}
	/**
	 * jump to chat ui
	 */
	public void doChat()
	{
		Bundle bundle=new Bundle();
		bundle.putString("cuid", cuid);
		bundle.putString("cnick", uEntity.getNick());
		bundle.putByteArray("cavatar", CommonUtil.Bitmap2Bytes(uEntity.getAvatar()));
		Intent intent = new Intent();
		intent.setClass(this, ChatActivity.class);
		intent.putExtras(bundle);
		this.finish();
		startActivity(intent);
	}
}
	
