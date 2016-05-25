package com.bnutalk.server;

import java.io.File;
import java.io.FileOutputStream;
import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.ContactEntity;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MyApplication;
import com.bnutalk.util.RecentMsgEntity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

/*
 * Author:linxiaobai 2016/05/21
 */
public class AHttpGetContacts{
	private Handler handler;
	// private List<Map<String, Object>> list;
	private List<ContactEntity> list;
	private String strJson;
	private Bitmap bmPhoto;
	private Message msg=new Message();
	private Drawable drawbPhoto;

	private String uid;// send uid to server
	private SharedPreferences pref = null;
	private DBopenHelper openHelper;
	private MyApplication myApp;
	public AHttpGetContacts() {
	}
	public AHttpGetContacts(String uid,List<ContactEntity> list) {
		this.uid=uid;
		this.list=list;
	}
	
	// send a doget to the server
	public void getContactsRequest() {
		list.clear();
		String ip = new GetServerIp().getServerIp();
		String url = "http://" + ip + ":8080/web/getContactServlet?&uid=" + uid;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int status, Header[] header, byte[] response) {
				// Json解析
				strJson = new String(response);
				CommonUtil.parseJsonContact(strJson,list);
//				msg.what = 0x001;
//				handler.sendMessage(msg);
//				openHelper.addContacts(uid, list);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//				msg.what = 0x002;
//				handler.sendMessage(msg);
			}
		});
	}
}
