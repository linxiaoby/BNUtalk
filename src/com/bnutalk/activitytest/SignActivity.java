package com.bnutalk.activitytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignActivity extends Activity {
	private EditText username;
	private EditText password;
	private Button signup;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_layout);
		username=(EditText) findViewById(R.id.etSgAccount);
		password=(EditText) findViewById(R.id.etSgPassword);
		signup=(Button) findViewById(R.id.btnSign);
	}
	/*·¢ÆðHTTPÇëÇó*/
	public void onLogin(View v)
	{
		String url="http://172.16.115.85:8080/web/MyServlet";
		new HttpThread(url, username.getText().toString(), password.getText().toString()).start();
	}

}
