package com.bnutalk.server;

import java.util.List;

import org.apache.http.Header;

import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.MsgEntity;
import com.bnutalk.util.UserEntity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Handler;
import android.os.Message;
import android.text.BoringLayout;

/**
 * Creaded by linxiaobai 2016-05-18
 *
 */
public class AHttpAddContacts {
	public static final int GET_USER_FAILED = 0X002;
	public static final int GET_USER_SUCCESS = 0X001;
	public static final int SAVE_LIKE_SUCCESS = 0X003;
	public static final int SAVE_LIKE_FAILED = 0X004;
	public static final int BEFRIEND = 0X005;
	public static final int NOT_BEFRIEND = 0X006;
	public static final int SERVEREXCEPTION=0X007;

	private List<UserEntity> list;
	private String uid;
	private String strJson;
	private Handler handler;
	private Message msg = new Message();
	private DBopenHelper helper;
	private Boolean isExit;

	public AHttpAddContacts() {

	}

	public AHttpAddContacts(List<UserEntity> list) {
		this.list = list;
	}

	public AHttpAddContacts(List<UserEntity> list, String uid) {
		this.list = list;
		this.uid = uid;
		this.strJson = null;
	}

	public AHttpAddContacts(String uid, Handler handler) {
		this.uid = uid;
		this.strJson = null;
		this.handler = handler;
	}

	public AHttpAddContacts(List<UserEntity> list, String uid, Handler handler, DBopenHelper helper) {
		this.list = list;
		this.uid = uid;
		this.strJson = null;
		this.handler = handler;
		this.helper = helper;
	}

	public void getAllUser() {
		String ip = ServerConn.serverIp;
		String url = "http://" + ip + ":8080/web/GetAllUserServlet?&uid=" + uid;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int status, Header[] header, byte[] response) {
				strJson = new String(response);
				CommonUtil.parseJsonUser(strJson, list);
				msg.what = GET_USER_SUCCESS;
				msg.obj = list;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(int status, Header[] header, byte[] response, Throwable error) {
				// msg.what=SERVEREXCEPTION;
				// handler.sendMessage(msg);
			}
		});

	}

	/**
	 * 
	 * @param cuid
	 */
	public void rightOperation(String cuid) {
		String ip = ServerConn.serverIp;
		String url = "http://" + ip + ":8080/web/AddConRightServlet?&uid=" + uid + "&cuid=" + cuid;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int status, Header[] head, byte[] response) {
				String flag = new String(response);
				if (flag.equals("1")) {
					msg.what = BEFRIEND;
				} else
					msg.what = NOT_BEFRIEND;
					handler.sendMessage(msg);
			}

			@Override
			public void onFailure(int status, Header[] head, byte[] response, Throwable error) {
			}
		});
	}

	/**
	 * check if (uid,cuid) exits in like_table
	 * 
	 * @param cuid
	 * @return true if exits
	 */
	public Boolean checkLike(String cuid) {
		String ip = ServerConn.serverIp;
		String url = "http://" + ip + ":8080/web/CheckLikeServlet?&uid=" + uid + "&cuid=" + cuid;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int status, Header[] head, byte[] response) {
				String res = new String(response);
				if (res.equals("true")) {
					isExit = true;
				} else
					isExit = false;
			}

			@Override
			public void onFailure(int status, Header[] head, byte[] response, Throwable error) {
			}
		});
		return isExit;
	}

	/**
	 * delete (cuid,uid) from like_table
	 * 
	 * @param cuid
	 */
	public void deleteLike(String cuid) {
		String ip = ServerConn.serverIp;
		String url = "http://" + ip + ":8080/web/DeleteLikeServlet?&uid=" + uid + "&cuid=" + cuid;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int status, Header[] head, byte[] response) {
			}

			@Override
			public void onFailure(int status, Header[] head, byte[] response, Throwable error) {
			}
		});

	}

	/**
	 * 
	 * @param cuid
	 */
	public void saveLikeUid(String cuid) {
		String ip = ServerConn.serverIp;
		String url = "http://" + ip + ":8080/web/SaveLikeServerlet";
		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("cuid", cuid);
		client.post(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int status, Header[] head, byte[] response) {
				msg.what = SAVE_LIKE_SUCCESS;
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(int status, Header[] head, byte[] response, Throwable error) {
				msg.what = SAVE_LIKE_FAILED;
				handler.sendMessage(msg);
			}
		});

	}

}
