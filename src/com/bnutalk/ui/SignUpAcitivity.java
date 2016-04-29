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
	}
	public void onSignUp(View v)
	{
		mailAdress=etMailAdress.getText().toString();
		passwd=etPasswd.getText().toString();
		rePasswd=etRePasswd.getText().toString();
		//�������ͻ��˵��������ƴ�������д������
		//ȫ�������˲��ܽ���else������else������ת����������Ϣ������
		
		int i=inputLimitation(mailAdress,passwd);//判断是否按要求填写
		if(i==1)
		{
			if(!passwd.equals(rePasswd))//两次密码不一致
			{
				Toast.makeText(SignUpAcitivity.this, "Passwords you typed do not match", Toast.LENGTH_SHORT).show();
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

		boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
		boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
		if(mail.length()!=12)
			return 2;
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
		//以下为检查学号格式
		if (null==mail || "".equals(mail)) return 2;//学号未填写

		Pattern p =  Pattern.compile(addr);
		Matcher m = p.matcher(mail);
		if(m.matches()) re=1;
		else re=2;
		return re;
	}
}
