package com.bnutalk.ui;
import android.app.Activity;
import android.os.Bundle;
import com.bnutalk.ui.R;
public class LoginActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_login);
		
		forget=(TextView)findViewById(R.id.forget_key);
		signIn=(Button)findViewById(R.id.login);
		signUp=(TextView)findViewById(R.id.sign_up);

		signIn.setOnClickListener(signInClick);
		forget.setOnClickListener(forgetClick);
		signUp.setOnClickListener(signUpClick);
	}
	View.OnClickListener forgetClick=new View.OnClickListener(){
		@Override
		public void onClick(View v){
		//需补充forgetPassword活动的转换
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
			//此处建立与服务器的连接
		}
	};
}
