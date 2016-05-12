package com.bnutalk.ui;

import java.util.ArrayList;
import java.util.List;

import com.bnutalk.lanunch.BaseFragmentAdapter;
import com.bnutalk.lanunch.FirstFragment;
import com.bnutalk.lanunch.FourFragment;
import com.bnutalk.lanunch.GuideViewPager;
import com.bnutalk.lanunch.LauncherBaseFragment;
import com.bnutalk.lanunch.SecondFragment;
import com.bnutalk.lanunch.ThirdFragment;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;



@SuppressLint("NewApi")
public class LanunchActivity extends FragmentActivity {
	private GuideViewPager vPager;
	private List<LauncherBaseFragment> list = new ArrayList<LauncherBaseFragment>();
	private BaseFragmentAdapter adapter;

	private ImageView[] tips;
	private int currentSelect; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luancher_main);
		
	
		ViewGroup group = (ViewGroup)findViewById(R.id.viewGroup);
		tips = new ImageView[4];
		for (int i = 0; i < tips.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(new LayoutParams(10, 10));
			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageView.setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			tips[i]=imageView;

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 20;
			layoutParams.rightMargin = 20;
			group.addView(imageView,layoutParams);
			
		}
		
		
		vPager = (GuideViewPager) findViewById(R.id.viewpager_launcher);

		FirstFragment firstFragment = new FirstFragment();
		SecondFragment secondFragment = new SecondFragment();
		ThirdFragment thirdFragment = new ThirdFragment();
		FourFragment fourfragmet = new FourFragment();
		list.add(firstFragment);
		list.add(secondFragment);
		list.add(thirdFragment);
		list.add(fourfragmet);

		adapter = new BaseFragmentAdapter(getSupportFragmentManager(),list);
		vPager.setAdapter(adapter);
		vPager.setOffscreenPageLimit(2);
		vPager.setCurrentItem(0);
		vPager.setOnPageChangeListener(changeListener);
	}
	
	
	OnPageChangeListener changeListener=new OnPageChangeListener() {
		@Override
		public void onPageSelected(int index) {
			setImageBackground(index);
			LauncherBaseFragment fragment=list.get(index);
			
			list.get(currentSelect).stopAnimation();
			fragment.startAnimation();
			
			currentSelect=index;
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
		@Override
		public void onPageScrollStateChanged(int arg0) {}
	};
	

	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}
}
