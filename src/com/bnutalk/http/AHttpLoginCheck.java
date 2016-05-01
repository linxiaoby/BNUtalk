package com.bnutalk.http;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AHttpLoginCheck {
	private String uid;
	private String passwd;

	public AHttpLoginCheck(String uid, String passwd) {
		this.uid = uid;
		this.passwd = passwd;
	}
	public void doLoginCheck()
	{
		String ip = new GetServerIp().getServerIp();
		String url = ip+"/web/LogServlet";
		
		RequestParams params=new RequestParams();
		params.put("uid",uid);
		params.put("passwd",passwd);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int status, Header[] headers, byte[] responseBody) {
				// called when response HTTP status is "200 OK"
				System.out.println(responseBody);
			}
			
			@Override
			public void onFailure(int status, Header[] header, byte[] eResponseBody, Throwable error) {
				  // called when response HTTP status is "4XX" (eg. 401, 403, 404)
			error.printStackTrace();	
			}
		});
	}
}
