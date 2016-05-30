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
	private int sex;
	private String strNickName;
	private int age;
	private int faculty;
	private String strNationality;
	private String strMother;
	private String strLike;
	
	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

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

	public String getStrNickName() {
		return strNickName;
	}

	public void setStrNickName(String strNickName) {
		this.strNickName = strNickName;
	}


	public int getFaculty() {
		return faculty;
	}

	public void setFaculty(int faculty) {
		this.faculty = faculty;
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
		String ip = new ServerConn().getServerIp();
		String url = "http://" + ip + ":8080/web/PersInfoUploadServlet";
		RequestParams params = new RequestParams();
		params.put("strUid", strUid);
		params.put("strPasswd", strPasswd);
		params.put("sex", sex);
		params.put("strNickName", strNickName);
		params.put("age", age);
		params.put("faculty", faculty);
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
