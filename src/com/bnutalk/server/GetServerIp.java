package com.bnutalk.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class GetServerIp {
	public static final String serverIp = "123.206.59.147";
	public static final int servScoketPrt = 7777;

	public int getServScoketPrt() {
		return servScoketPrt;
	}

	public String getServerIp() {
		return serverIp;
	}

	/**
	 * get network state
	 * 
	 * @param context
	 * @return true for available ,false for not available
	 */

	public boolean checkNetworkState(Context context) {
		boolean flag = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			flag = cm.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}
}
