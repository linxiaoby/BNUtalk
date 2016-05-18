package com.bnutalk.server;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
*�ϴ��û�ע��ʱ�ĸ�����Ϣ
*/
public class AHttpPersInfoUpload {
	private String strUid;
	private String strPasswd;
	private String strSex;
	private String strNickName;
	private String strAge;
	private String strFaculty;
	private String strNationality;
	private String strMother;
	private String strLike;

	public String getStrUid() {
		return strUid;
	}

	public void setStrUid(String strUid) {
		this.strUid = strUid;
	}

	public String getStrPasswd() {
		return strPasswd;
	}

	public void setStrPasswd(String strPasswd) {
		this.strPasswd = strPasswd;
	}

	public String getStrSex() {
		return strSex;
	}

	public void setStrSex(String strSex) {
		this.strSex = strSex;
	}

	public String getStrNickName() {
		return strNickName;
	}

	public void setStrNickName(String strNickName) {
		this.strNickName = strNickName;
	}

	public String getStrAge() {
		return strAge;
	}

	public void setStrAge(String strAge) {
		this.strAge = strAge;
	}

	public String getStrFaculty() {
		return strFaculty;
	}

	public void setStrFaculty(String strFaculty) {
		this.strFaculty = strFaculty;
	}

	public String getStrNationality() {
		return strNationality;
	}

	public void setStrNationality(String strNationality) {
		this.strNationality = strNationality;
	}

	public String getStrMother() {
		return strMother;
	}

	public void setStrMother(String strMother) {
		this.strMother = strMother;
	}

	public String getStrLike() {
		return strLike;
	}

	public void setStrLike(String strLike) {
		this.strLike = strLike;
	}
	// �ϴ���ݵ�������
	public void sendPersInfo() {
		String ip = new GetServerIp().getServerIp();
		String url = ip + "/web/PersInfoUploadServlet";
		RequestParams params = new RequestParams();
		params.put("strUid", strUid);
		params.put("strPasswd", strPasswd);
		params.put("strSex", strSex); 
		params.put("strNickName", strNickName);
		params.put("strAge", strAge);
		params.put("strFaculty", strFaculty);
		params.put("strNationality", strNationality);
		params.put("strMother", strMother);
		params.put("strLike", strLike);
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				// TODO Auto-generated method stub
			}
		});
	}
}
