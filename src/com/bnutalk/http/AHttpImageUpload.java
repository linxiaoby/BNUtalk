package com.bnutalk.http;

import java.io.ByteArrayOutputStream;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

/*
 * AsyncHttpClient本身就实现了可以在非UI执行，因此不需要自己new一个thread，直接写成方法调用即可
 */
public class AHttpImageUpload {
	
	private String strUid;
	private Bitmap bmPhoto;
	public AHttpImageUpload(String uid,Bitmap bitmap) {
		Log.i("imageuploadThread", "construct");
		this.strUid=uid;
		this.bmPhoto = bitmap;
	}
	public void sendImage() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// 将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
			bmPhoto.compress(Bitmap.CompressFormat.PNG, 100, baos);
			baos.close();
			byte[] buffer = baos.toByteArray();
			System.out.println("图片的大小：" + buffer.length);

			// 将图片的字节流数据加密成base64字符输出
			String strPhoto = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);

			// photo=URLEncoder.encode(photo,"UTF-8");
			RequestParams params = new RequestParams();
			params.put("strPhoto", strPhoto);
			params.put("strUid", strUid);// 传输的字符数据
			
			String ip = new GetServerIp().getServerIp();
			String url = ip+"/web/ImageUploadServlet";
			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					arg3.printStackTrace();
				}
				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				}
				
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
