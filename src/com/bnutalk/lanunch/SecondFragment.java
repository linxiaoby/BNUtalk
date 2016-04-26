package com.bnutalk.lanunch;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.bnutalk.ui.R;



public class SecondFragment extends LauncherBaseFragment{

	private boolean started;//閺勵垰鎯佸锟介崥顖氬З閻拷
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rooView=inflater.inflate(R.layout.second_ui, null);
		return rooView;
	}
	
	public void stopAnimation(){
		
		started=false;
	}
	
	
	public void startAnimation(){
		started=true;
		
		
		
	}
	
}