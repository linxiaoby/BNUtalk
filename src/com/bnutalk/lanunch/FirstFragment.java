package com.bnutalk.lanunch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.bnutalk.ui.R;

public class FirstFragment extends LauncherBaseFragment{

	private boolean started;//闁哄嫷鍨伴幆浣割嚕閿熶粙宕ラ姘楅柣顫嫹(ViewPage婵犲﹥鍨垫慨鈺呭籍鐠虹尨鎷峰▎鎴犺埗閺夆晜鐟ら柌婊堝矗濮楋拷閸ｈ櫣鎸х�ｎ亷鎷烽敓锟�)
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rooView=inflater.inflate(R.layout.first_ui, null);
	
		startAnimation();
		return rooView;
	}
	
	public void startAnimation(){
		
	}
	
	@Override
	public void stopAnimation(){
		
	}
}
