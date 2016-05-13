package com.bnutalk.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.CursorJoiner.Result;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;

import com.bnutalk.http.AHttpLoginCheck;
import com.bnutalk.http.GetServerIp;
import com.bnutalk.socket.Msg;
import com.bnutalk.ui.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class LoginActivity extends Activity {
	private Button btLogin;
	private String uid, passwd;
	private EditText etUid, etPasswd;
	private TextView tvForget, tvSignUp;
	private Button btSignIn;

	public static String result;// 登录验证结果
	private Handler handler;// 用于显示登录成功或失败信息

	private SharedPreferences pref;
	private Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_login);
		findView();
		readUidFromCache();
		handlerDef();
		
		
		writeUidToCache();
	}

	/**
	 * find view
	 */
	public void findView() {
		etUid = (EditText) findViewById(R.id.etUid);
		etPasswd = (EditText) findViewById(R.id.etPasswd);
		btSignIn = (Button) findViewById(R.id.login);
		tvForget = (TextView) findViewById(R.id.forget_key);
		tvSignUp = (TextView) findViewById(R.id.sign_up);
	}
	/**
	 * read uid from local cache and set EditText etUid
	 */
	public void readUidFromCache()
	{
		pref = getSharedPreferences("user_login", 0);
		editor = pref.edit();
		String cacheUid=pref.getString("uid","");
		if(cacheUid!=null)
		{
			etUid.setText(cacheUid);
		}
	}
	/**
	 * write uid to local cache:file user_login
	 */
	public void writeUidToCache()
	{
		editor.putString("uid", uid);
		editor.commit();
	}

	/**
	 * 定义hander事件
	 */
	public void handlerDef() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Log.v("handler", "handler called");
				System.out.println("msgwhat" + msg.what);

				if (msg.what == 1) {
					Toast.makeText(LoginActivity.this, "login success!", Toast.LENGTH_LONG).show();

					// info right,save uid into user_login
					writeUidToCache();

					//jump into MsgFriendListActivity
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MsgFriendListActivity.class);
					startActivity(intent);

				} else {
					Toast.makeText(LoginActivity.this, "usename or password error!", Toast.LENGTH_SHORT).show();
				}
			}
		};

	}

	public void forgetClick(View v) {
	}

	public void signUpClick(View v) {
		Intent it = new Intent();
		it.setClass(LoginActivity.this, SignUpAcitivity.class);
		startActivity(it);
	}

	public void signInClick(View v) {
		uid = etUid.getText().toString();
		passwd = etPasswd.getText().toString();

		// 服务器操作：用户合法性验证+其他（待写）
		doLogin(uid, passwd);
	}

	/**
	 * server operation:upload uid and password to server
	 * 
	 * @param strUid
	 * @param strPasswd
	 */
	public void doLogin(String strUid, String strPasswd) {
		new AHttpLoginCheck(handler, strUid, strPasswd).doLoginCheck();

	}
}
