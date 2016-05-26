package com.bnutalk.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class ItemSelfInfoActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.item_personal_info);
	}
}
