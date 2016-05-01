package com.bnutalk.ui;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bnutalk.ui.R;
public class LoginActivity extends Activity {
	private Button btLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_login);
	}
		TextView forget = (TextView)findViewById(R.id.forget_key);
		Button signIn = (Button)findViewById(R.id.login);
		TextView signUp = (TextView)findViewById(R.id.sign_up);

		signIn.setOnClickListener(signInClick);
		forget.setOnClickListener(forgetClick);
		signUp.setOnClickListener(signUpClick);
	}
	View.OnClickListener forgetClick=new View.OnClickListener(){
		@Override
		public void onClick(View v){
		//闇�琛ュ厖forgetPassword娲诲姩鐨勮浆鎹�
		}
	};
	View.OnClickListener signUpClick=new View.OnClickListener(){
		@Override
		public void onClick(View v){
			Intent it =new Intent();
			it.setClass(LoginActivity.this,SignUpAcitivity.class);
			startActivity(it);
		}
	};
	View.OnClickListener signInClick=new View.OnClickListener(){
		@Override
		public void onClick(View v){
			//姝ゅ寤虹珛涓庢湇鍔″櫒鐨勮繛鎺�
		}
	};
}
