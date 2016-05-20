package com.bnutalk.ui;

import android.app.Activity;
import android.content.Context;
import android.database.DefaultDatabaseErrorHandler;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video;
import android.provider.SyncStateContract.Helpers;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bnutalk.server.AHttpGetAllUser;
import com.bnutalk.ui.R;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.FlingAdapterView;
import com.bnutalk.util.UserEntity;

import java.security.PublicKey;
import java.util.ArrayList;
//import java.util.List;
import java.util.List;

import org.apache.commons.logging.Log;

public class AddFriendActivity extends Activity {

	// private ArrayList<CardMode> al;
	private List<UserEntity> al;

	// private ArrayList<ImageView> iv;
	// 定义一个cardmode的数组al
	private CardAdapter adapter;
	// 定义一个card的适配器
	private int i;
	// 定义滑动卡片的容器
	private FlingAdapterView flingContainer;
	// 定义一个string类型的数组
	// private List<List<String>> list = new ArrayList<>();
	// 定义下面的左右喜欢喝不喜欢的图片
	private ImageView left, right, music;
	private Handler handler;
	private String uid;
	private DBopenHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my);
		initEvent();
		
		uid = "201211011063";
		new AHttpGetAllUser(al, uid, handler,helper).getAllUser();
	}
	/**
	 * init
	 */
	public void initEvent() {
		// 定义左边和右边的图片，和监听
		left = (ImageView) findViewById(R.id.left);
		right = (ImageView) findViewById(R.id.right);
		music = (ImageView) findViewById(R.id.iv_card_flag6);
		al = new ArrayList<UserEntity>();
		adapter = new CardAdapter(AddFriendActivity.this, al);
		
		
		helper=new DBopenHelper(getApplicationContext());
		
		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				left();
			}
		});
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				right();
			}
		});
		
		defHandler();
		defFling();
		
		//read user cards from local cache to show first
		helper.getUserCard(al);
		adapter.notifyDataSetChanged();
	}
	/**
	 * define handler operation
	 */
	public void defHandler()
	{
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case AHttpGetAllUser.SUCCESS:
					android.util.Log.v("msg.what", "AHttpGetAllUser.SUCCESS");
					// show listview
					adapter.notifyDataSetChanged();
					break;
				case AHttpGetAllUser.SERVEREXCEPTION:
					// unable to access server
					Toast.makeText(AddFriendActivity.this, "unable to access server!", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
	}
	
	public void defFling()
	{
		flingContainer = (FlingAdapterView) findViewById(R.id.frame);
		flingContainer.setAdapter(adapter);

		flingContainer.setFlingListener(new FlingAdapterView.onFlingListener() {
			@Override
			public void removeFirstObjectInAdapter() {
				al.remove(0);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onLeftCardExit(Object dataObject) {
				makeToast(AddFriendActivity.this, "不喜欢");
			}

			@Override
			public void onRightCardExit(Object dataObject) {
				makeToast(AddFriendActivity.this, "喜欢");
			}

			@Override
			public void onAdapterAboutToEmpty(int itemsInAdapter) {
				// al.add(new CardMode("胡欣语", 21, R.drawable.picture_fisrt,
				// "信息科学与技术", "中文", "英文", "男", "学五食堂"));

				adapter.notifyDataSetChanged();
				i++;
			}

			@Override
			public void onScroll(float scrollProgressPercent) {
				try {
					View view = flingContainer.getSelectedView();
					view.findViewById(R.id.item_swipe_right_indicator)
							.setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
					view.findViewById(R.id.item_swipe_left_indicator)
							.setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		flingContainer.setOnItemClickListener(new FlingAdapterView.OnItemClickListener() {
			@Override
			public void onItemClicked(int itemPosition, Object dataObject) {
				makeToast(AddFriendActivity.this, "点击图片");
			}
		});
	}
	static void makeToast(Context ctx, String s) {
		Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
	}

	public void right() {
		flingContainer.getTopCardListener().selectRight();
	}

	public void left() {
		flingContainer.getTopCardListener().selectLeft();
	}

	public void playmusic() {

	}

}
