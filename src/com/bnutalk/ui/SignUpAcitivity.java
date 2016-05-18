package com.bnutalk.ui;

import com.bnutalk.server.SignUpThread;
import com.bnutalk.ui.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpAcitivity extends Activity {
	private String uid,passwd,rePasswd;
	private EditText etUid,etPasswd,etRePasswd;
	private Button btSignUp;
	
	private SharedPreferences pref;
	private Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signup);
		findView();
		
		pref = getSharedPreferences("user_login", 0);
		editor = pref.edit();
	}
	public void findView()
	{
		etUid=(EditText) findViewById(R.id.mail_signup_text);
		etPasswd=(EditText) findViewById(R.id.key_signup_text);
		etRePasswd=(EditText) findViewById(R.id.key_ensure_signup_text);
		btSignUp=(Button) findViewById(R.id.signup);
	}
	public void onSignUp(View v)
	{
		uid=etUid.getText().toString();
		passwd=etPasswd.getText().toString();
		rePasswd=etRePasswd.getText().toString();
		//自力：客户端的输入限制处理大致写在这里
		//全部符合了才能进入else条件，else用来跳转到“个人信息”界面
		
		int i=inputLimitation(uid,passwd);//鍒ゆ柇鏄惁鎸夎姹傚～鍐�
		if(i==1)
		{
			if(!passwd.equals(rePasswd))//涓ゆ瀵嗙爜涓嶄竴鑷�
			{
				Toast.makeText(SignUpAcitivity.this, "Passwords you typed do not match", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(SignUpAcitivity.this,"sign up success!", Toast.LENGTH_SHORT).show();
				//sava uid to local cache,filename:user_login
				editor.putString("uid", uid);
				editor.commit();
				
				Bundle bundle = new Bundle();
				bundle.putString("mailAdress", uid);
				bundle.putString("passwd",passwd);
				
				Intent intent = new Intent();
				intent.setClass(this, SignUpPersInfoActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}
		else{
			if(i==2)
			Toast.makeText(SignUpAcitivity.this,"A BNU ID is required in the first column",Toast.LENGTH_SHORT)
					.show();
			else if(i==0)
				Toast.makeText(SignUpAcitivity.this,"Password should include at least a number and a letter",Toast.LENGTH_SHORT)
						.show();
			else if(i==3)
				Toast.makeText(SignUpAcitivity.this,"Password should consist at least 6 digits",Toast.LENGTH_SHORT)
						.show();
			else
				Toast.makeText(SignUpAcitivity.this,"Mysterious mistakes",Toast.LENGTH_SHORT)
						.show();
		}
		
	}
	private int inputLimitation(String mail,String pas) {
		int re=-1;
		String psw = "^[a-zA-Z0-9]+$";
		String addr="^[0-9]+$";

		boolean isDigit = false;//瀹氫箟涓�涓猙oolean鍊硷紝鐢ㄦ潵琛ㄧず鏄惁鍖呭惈鏁板瓧
		boolean isLetter = false;//瀹氫箟涓�涓猙oolean鍊硷紝鐢ㄦ潵琛ㄧず鏄惁鍖呭惈瀛楁瘝
		if(mail.length()!=12)
			return 2;
		if(pas.length()<6)
			return 3;
		//浠ヤ笅涓烘鏌ュ瘑鐮佹牸寮�
		for(int i=0 ; i < pas.length(); i++) {
			if (Character.isDigit(pas.charAt(i))) {//鐢╟har鍖呰绫讳腑鐨勫垽鏂暟瀛楃殑鏂规硶鍒ゆ柇姣忎竴涓瓧绗�
				isDigit = true;
			}
			if (Character.isLetter(pas.charAt(i))) {//鐢╟har鍖呰绫讳腑鐨勫垽鏂瓧姣嶇殑鏂规硶鍒ゆ柇姣忎竴涓瓧绗�
				isLetter = true;
			}
		}
		boolean isRight = isDigit && isLetter &&pas.matches(psw);
		if(isRight){
			re=1;
		}else{
			return 0;
		}
		//浠ヤ笅涓烘鏌ュ鍙锋牸寮�
		if (null==mail || "".equals(mail)) return 2;//瀛﹀彿鏈～鍐�

		Pattern p =  Pattern.compile(addr);
		Matcher m = p.matcher(mail);
		if(m.matches()) re=1;
		else re=2;
		return re;
	}
}
