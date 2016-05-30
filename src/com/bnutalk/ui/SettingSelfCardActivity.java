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

public class SettingSelfCardActivity extends Activity {
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
		setContentView(R.layout.item_personal_info);
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
		
		myApp=(MyApplication) getApplicationContext();
		Bundle bundle = this.getIntent().getExtras();
		uEntity=myApp.getSelfInfoList().get(0);
	}

	public void getInfoCard()
	{
		if(uEntity.getUid()==null)
		{
			
		}
		else 
		{
			showInfoCard();
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
}
	
