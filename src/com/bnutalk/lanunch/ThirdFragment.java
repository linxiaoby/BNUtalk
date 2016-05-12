package com.bnutalk.lanunch;

import com.bnutalk.ui.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;


public class ThirdFragment extends LauncherBaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rooView=inflater.inflate(R.layout.third_ui, null);
		return rooView;
	}
	
	@Override
	public void startAnimation() {
		
	}

	@Override
	public void stopAnimation() {
		
	}
}
