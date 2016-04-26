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
		
		private int i=inputLimitation(mailAdress,passwd);//判断是否按要求填写
		if(i==1)
		{
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
		else{
			if(i==2)
			Toast.makeText(SignUpAcitivity.this,"注册邮箱需要BNU邮箱",Toast.LENGTH_SHORT)
					.show();
			else if(i==0)
				Toast.makeText(SignUpAcitivity.this,"密码需同时包含数字和字母",Toast.LENGTH_SHORT)
						.show();
			else if(i==3)
				Toast.makeText(SignUpAcitivity.this,"密码需大于或等于6位",Toast.LENGTH_SHORT)
						.show();
			else
				Toast.makeText(SignUpAcitivity.this,"未知错误",Toast.LENGTH_SHORT)
						.show();
		}
		
	}
	private int inputLimitation(String mail,String pas) {
		int re=-1;
		String psw = "^[a-zA-Z0-9]+$";
		String addr="^([a-zA-Z0-9_\\-\\.]+)@mail.bnu.edu.cn)$";//这个正则式不知道够不够

		boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
		boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
		if(pas.length()<6)
			return 3;
		//以下为检查密码格式
		for(int i=0 ; i < pas.length(); i++) {
			if (Character.isDigit(pas.charAt(i))) {//用char包装类中的判断数字的方法判断每一个字符
				isDigit = true;
			}
			if (Character.isLetter(pas.charAt(i))) {//用char包装类中的判断字母的方法判断每一个字符
				isLetter = true;
			}
		}
		boolean isRight = isDigit && isLetter &&pas.matches(psw);
		if(isRight){
			re=1;
		}else{
			return 0;
		}
		//以下为检查邮箱地址格式
		if (null==mail || "".equals(mail)) return 2;//邮箱未填写

		Pattern p =  Pattern.compile(addr);
		Matcher m = p.matcher(mail);
		if(m.matches()) re=1;
		else re=2;
		return re;
	}
}
