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
import android.provider.SyncStateContract.Helpers;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import org.apache.http.Header;

import com.bnutalk.server.AHttpLoginCheck;
import com.bnutalk.server.MsgService;
import com.bnutalk.server.ServerConn;
import com.bnutalk.ui.R;
import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MyApplication;
import com.bnutalk.util.UserEntity;
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
	private MyApplication myApp;
	private DBopenHelper helper;
	private MsgService msgService;
	// server operation：用于socket的成员变量
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.acitivity_login);
		findView();
		readUidFromCache();
		handlerDef();
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
		tvSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(LoginActivity.this, SignUpAcitivity.class);
				startActivity(intent1);
			}
		});
		myApp = (MyApplication) getApplicationContext();
		helper = new DBopenHelper(LoginActivity.this);
//		helper.updateDb();//need to be deleteds
	}

	/**
	 * read uid from local cache and set EditText etUid
	 */
	public void readUidFromCache() {
		pref = getSharedPreferences("user_login", 0);
		editor = pref.edit();
		String cacheUid = pref.getString("uid", "");
		if (cacheUid != null) {
			etUid.setText(cacheUid);
		}
	}

	/**
	 * write uid to local cache:file user_login
	 */
	public void writeUidToCache() {
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
				switch (msg.what) {
				case 1:
					handlerLogSuccess();
					break;
				case 2:
					Toast.makeText(LoginActivity.this, "usename or password error!", Toast.LENGTH_LONG).show();
					break;
				case -1:
					Toast.makeText(LoginActivity.this, "Can't connect to server!", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
			}
		};

	}

	public void handlerLogSuccess() {
		getSocketConn(this);
		Toast.makeText(LoginActivity.this, "login success!", Toast.LENGTH_LONG).show();
		// info right,save uid into user_login,set final varable uid
		MyApplication myApp = (MyApplication) getApplicationContext();
		myApp.setUid(uid);

		writeUidToCache();
		getSelfInfo(uid, myApp.getSelfInfoList());
		// jump into MainActivity
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, MainActivity.class);
		startActivity(intent);
	}
	
	public void getSocketConn(Context context) {
//		if (myApp.getSocket() == null) {
			ServerConn.serverConn(uid, context,helper);
//			ServerConn.ReadFromServ(LoginActivity.this, helper);
//		}
	}

	public void getSelfInfo(String uid, List<UserEntity> list) {
		helper.getSelfInfo(uid, myApp.getSelfInfoList());
		if (myApp.getSelfInfoList().size() == 0) {
			getServerInfo(uid);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (myApp.getSocket() != null)
			try {
				myApp.getSocket().close();
			} catch (IOException e) {
				Log.v("socket", "socket close error!");
				e.printStackTrace();
			}
	}

	public void getServerInfo(final String uid) {
		String ip = ServerConn.serverIp;
		String url = "http://" + ip + ":8080/web/GetSelfInfoServlet?&uid=" + uid;
		final Message tmsg = new Message();
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int status, Header[] header, byte[] response) {
				String strJson = new String(response);
				CommonUtil.parseJsonUser(strJson, myApp.getSelfInfoList());
				helper.addSelfInfo(uid, myApp.getSelfInfoList());// save self
			}

			@Override
			public void onFailure(int status, Header[] header, byte[] response, Throwable error) {
			}
		});
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
