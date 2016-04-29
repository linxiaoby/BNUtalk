package com.bnutalk.ui;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bnutalk.socket.SocketTread;
import com.bnutalk.ui.R;
public class LoginActivity extends Activity {
	private Button btLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_login);
		btLogin=(Button) findViewById(R.id.login);
		new SocketTread().start();
	}
//	public void doSocket(View v)
//	{
//		new SocketTread().start();
//	}
}
