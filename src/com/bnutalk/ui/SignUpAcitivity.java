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
	//2�����ݳ�Ա
	private String mailAdress;
	private String passwd;
	private String rePasswd;
	//3��EditText
	private EditText etMailAdress;
	private EditText etPasswd;
	private EditText etRePasswd;
	//1��button
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
		//�ڿͻ����ж����������Ƿ��Ǻ�
	}
	public void onSignUp(View v)
	{
		mailAdress=etMailAdress.getText().toString();
		passwd=etPasswd.getText().toString();
		rePasswd=etRePasswd.getText().toString();
		
		if(!passwd.equals(rePasswd))//�������벻һ��
		{
			Toast.makeText(SignUpAcitivity.this, "�������벻һ��", Toast.LENGTH_SHORT).show();
		}
		else
		{
//			String url="http://172.31.105.199:8080/web/SignUpServlet";
//			new SignUpThread(url,mailAdress,passwd).start();
			/*Я��Mail������ת��������Ϣ����*/
			Bundle bundle = new Bundle();
			bundle.putString("mailAdress", mailAdress);
			bundle.putString("passwd",passwd);
			Intent intent = new Intent();
			//�O����һ��Actitity
			intent.setClass(this, SignUpPersInfoActivity.class);
			intent.putExtras(bundle);
			//�_��Activity
			startActivity(intent);
		}
		
	}
}
