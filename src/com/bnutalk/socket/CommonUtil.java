package com.bnutalk.socket;

import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.bnutalk.ui.RecentMsgEntity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.StaticLayout;
import android.util.Log;

public class CommonUtil {
	
	List<RecentMsgEntity> list=new ArrayList<RecentMsgEntity>();
	public CommonUtil() {
	}
	public static String getCurrentTime()
	{
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		String time = sDateFormat.format(new java.util.Date());
		return time;
	}
	/**
	 * compare time
	 * @param s1
	 * @param s2
	 * @return int 0:equal 1:s1>s2 2:s1<s2
	 */
	public static int compareTime(String s1, String s2) {
		java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(df.parse(s1));
			c2.setTime(df.parse(s2));
		} catch (java.text.ParseException e) {
			System.err.println("格式不正确");
		}
		
		int result = c1.compareTo(c2);
		if (result == 0)
			return 0;
		else if (result < 0)
			return -1;
		else
			return 1;
	}
	public static void sortListByTime(List<RecentMsgEntity> list)
	{
		Collections.sort(list);
	}
}
