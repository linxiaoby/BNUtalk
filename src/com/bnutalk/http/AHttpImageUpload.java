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
 * AsyncHttpClient�����ʵ���˿����ڷ�UIִ�У���˲���Ҫ�Լ�newһ��thread��ֱ��д�ɷ������ü���
 */
public class AHttpImageUpload {
	public static final String url = "http://172.31.105.199:8080/web/ImageUploadServlet";
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

			// ��bitmapһ�ֽ������ Bitmap.CompressFormat.PNG ѹ����ʽ��100��ѹ���ʣ�baos���ֽ���
			bmPhoto.compress(Bitmap.CompressFormat.PNG, 100, baos);
			baos.close();
			byte[] buffer = baos.toByteArray();
			System.out.println("ͼƬ�Ĵ�С��" + buffer.length);

			// ��ͼƬ���ֽ������ݼ��ܳ�base64�ַ����
			String strPhoto = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);

			// photo=URLEncoder.encode(photo,"UTF-8");
			RequestParams params = new RequestParams();
			params.put("strPhoto", strPhoto);
			params.put("strUid", strUid);// ������ַ�����
			AsyncHttpClient client = new AsyncHttpClient();
			client.post(url, params, new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
					// TODO Auto-generated method stub

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
