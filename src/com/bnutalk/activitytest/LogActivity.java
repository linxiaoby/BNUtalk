package com.bnutalk.activitytest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LogActivity extends Activity {
	private EditText username;
	private EditText password;
	private Button btlogin;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_layout);
		username=(EditText) findViewById(R.id.etAccount);
		password=(EditText) findViewById(R.id.etPassword);
		btlogin=(Button) findViewById(R.id.btnLogin);
		
	}
	/*·¢ÆðHTTPÇëÇó*/
	public void onLogin(View v)
	{
		String url="http://172.16.115.85:8080/web/LogServlet";
		new HttpThread(url, username.getText().toString(), password.getText().toString()).start();
	}

	public void onSignup(View v)
	{
		finish();
		Intent i=new Intent(this, SignActivity.class);
		startActivity(i);
	}
}
