package com.bnutalk.http;

import java.io.File;
import java.io.FileOutputStream;
import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

/*
 * Author:linxiaobai 2016/05/02
 */
public class AHttpMsgFriendDload {
	private Handler handler;
	private List<Map<String, Object>> list;
	private String strJson;
	private Bitmap bmPhoto;
	private Message msg;
	private Drawable drawbPhoto;
	
	private String strUid;//send uid to server

	public AHttpMsgFriendDload(String uid,Handler handler, List<Map<String, Object>> list) {
		this.strUid=uid;
		this.handler = handler;
		this.list = list;
		this.strJson = null;
	}

	// send a doget to the server
	public void msgFriDloadRequest() {
		String ip = new GetServerIp().getServerIp();
		String url = "http://" + ip + ":8080/web/MsgFriendDwnloadServlet?&strUid="+strUid;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int status, Header[] header, byte[] response) {
				// Json解析
				strJson = new String(response);
				parseJson(strJson);
				msg = new Message();
				msg.what = 0x001;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

			}
		});
	}

	public void parseJson(String strJson) {
		try {
			JSONArray jsonArray = new JSONArray(strJson);
			for(int i=0;i<jsonArray.length();i++)
			{
				JSONObject user = jsonArray.getJSONObject(i);
				String strUid = user.getString("strUid");
				String strNickname = user.getString("strNickname");
				String strPhoto = user.getString("strPhoto");
				// 图片string转换成png
				imgStrToDrwble(strPhoto);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("nickname", strNickname);
				map.put("image", bmPhoto);
				map.put("info", "to be a better girl");
				list.add(map);
			}
			Log.v("parseJson", "parseJson success");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void imgStrToDrwble(String strPhoto) {
		byte[] photoimg = Base64.decode(strPhoto, 0);
		for (int i = 0; i < photoimg.length; ++i) {
			if (photoimg[i] < 0) {
				// 调整异常数据
				photoimg[i] += 256;
			}
		}
		bmPhoto = BitmapFactory.decodeByteArray(photoimg, 0, photoimg.length);
//		drawbPhoto = new BitmapDrawable(bmPhoto);
	}
}
