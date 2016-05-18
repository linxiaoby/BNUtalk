package com.bnutalk.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.R.string;
/**
 * 
 * @author linxiaobai
 *	upload user infomation to server ,don't delete just because no ahttp using
 */
public class SignUpThread extends Thread {
	private String url;
	private String mailAdress;
	private String passwd;
	
	public SignUpThread(String url, String mailAdress, String password) {
		// TODO Auto-generated constructor stub
		this.url = url;
		this.mailAdress = mailAdress;
		this.passwd = password;
	}

	private void doGet() throws IOException {
		System.out.print("signUpHttp�߳̿�ʼ");
		url=url+"?mailAdress="+mailAdress+"&passwd="+passwd;
		try {
			//����ݴ��͸�Server
			URL httpUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			
			BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String str;
			StringBuffer sb=new StringBuffer();
			//��ȡ���������ص���Ϣ
			while((str=reader.readLine())!=null)
			{
				sb.append(str);
			}
			//�ѷ���˷��ص���ݴ�ӡ����
			System.out.println("result"+sb.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*��run�е���doGet*/
	@Override
	public void run() {
		try {
			doGet();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
