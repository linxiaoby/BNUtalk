package com.bnutalk.ui;

import com.bnutalk.ui.R;
import com.bnutalk.http.SignUpThread;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpAcitivity extends Activity {
	//2个数据成员
	private String mailAdress;
	private String passwd;
	private String rePasswd;
	//3个EditText
	private EditText etMailAdress;
	private EditText etPasswd;
	private EditText etRePasswd;
	//1个button
	private Button btSignUp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		etMailAdress=(EditText) findViewById(R.id.mail_signup_text);
		etPasswd=(EditText) findViewById(R.id.key_signup_text);
		etRePasswd=(EditText) findViewById(R.id.key_ensure_signup_text);
		btSignUp=(Button) findViewById(R.id.signup);
		//在客户端判断两次密码是否吻合
	}
	public void onSignUp(View v)
	{
		mailAdress=etMailAdress.getText().toString();
		passwd=etPasswd.getText().toString();
		rePasswd=etRePasswd.getText().toString();
		
		if(!passwd.equals(rePasswd))//两次密码不一致
		{
			Toast.makeText(SignUpAcitivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
		}
		else
		{
//			String url="http://172.31.105.199:8080/web/SignUpServlet";
//			new SignUpThread(url,mailAdress,passwd).start();
			/*携带Mail数据跳转到个人信息界面*/
			Bundle bundle = new Bundle();
			bundle.putString("mailAdress", mailAdress);
			bundle.putString("passwd",passwd);
			Intent intent = new Intent();
			//設定下一個Actitity
			intent.setClass(this, SignUpPersInfoActivity.class);
			intent.putExtras(bundle);
			//開啟Activity
			startActivity(intent);
		}
		
	}
}
