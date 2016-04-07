package com.bnutalk.activitytest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.R.string;

public class HttpThread extends Thread {
	String url;
	String username;
	String password;

	public HttpThread(String url, String username, String password) {
		// TODO Auto-generated constructor stub
		this.url = url;
		this.username = username;
		this.password = password;
	}

	private void doGet() throws IOException {
		url=url+"?username="+username+"&password="+password;
		try {
			URL httpUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String str;
			StringBuffer sb=new StringBuffer();
			//读取服务器返回的信息
			while((str=reader.readLine())!=null)
			{
				sb.append(str);
			}
			//把服务端返回的数据打印出来
			System.out.println("result"+sb.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*在run中调用doGet*/
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
