package com.bnutalk.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.DefaultDatabaseErrorHandler;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video;
import android.provider.SyncStateContract.Helpers;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bnutalk.server.AHttpAddContacts;
import com.bnutalk.ui.R;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.FlingAdapterView;
import com.bnutalk.util.UserEntity;

import java.security.PublicKey;
import java.util.ArrayList;
//import java.util.List;
import java.util.List;

import org.apache.commons.logging.Log;

public class AddContactsActivity extends Activity {
	private static final String TAG = "AddContactsActivity";
	// private ArrayList<CardMode> al;
	private List<UserEntity> list;

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
	private String uid, cuid, nick;
	private Button back;
	private DBopenHelper helper;
	private SharedPreferences pref;
	private Editor editor;
	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		android.util.Log.v(TAG, "onCreate() called!");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addfriend_main);
		initEvent();

	}

	@Override
	protected void onResume() {
		super.onResume();
		android.util.Log.v(TAG, "onResume() called!");
		getUserCard();
		helper.getUserCard(uid, list);
	}

	@Override
	protected void onPause() {
		super.onPause();
		android.util.Log.v(TAG, "onPause() called!");
		saveUserCard();
	}

	/**
	 * init
	 */
	public void initEvent() {
		uid = "201211011063";
		// 定义左边和右边的图片，和监听
		left = (ImageView) findViewById(R.id.left);
		right = (ImageView) findViewById(R.id.right);
		music = (ImageView) findViewById(R.id.iv_card_flag6);
		back = (Button) findViewById(R.id.btAddConBack);
		list = new ArrayList<UserEntity>();
		adapter = new CardAdapter(AddContactsActivity.this, list);
		helper = new DBopenHelper(getApplicationContext());

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
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AddContactsActivity.this.finish();
			}
		});
		defHandler();
		defFling();

		// read user cards from local cache to show first
		helper.getUserCard(uid, list);
		adapter.notifyDataSetChanged();

		// get the current uid
		pref = getSharedPreferences("user_login", 0);
		editor = pref.edit();
		String cacheUid = pref.getString("uid", "");
		if (cacheUid != null) {
			uid = cacheUid;
		}
		if (list.size() == 0) {
			Toast toast = Toast.makeText(AddContactsActivity.this, "正在加载数据，请耐心等待！(产品组帮我翻译成英文！)", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	public void getUserCard() {
		helper.getUserCard(uid, list);
		if (list.size() == 0)// read from server
		{
			new AHttpAddContacts(list, uid, handler, helper).getAllUser();
		} else
			adapter.notifyDataSetChanged();

	}

	public void saveUserCard() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				helper.addUserCard(uid, list);
			}
		}).start();
	}

	/**
	 * define handler operation
	 */
	public void defHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case AHttpAddContacts.GET_USER_SUCCESS:
					android.util.Log.v("msg.what", "AHttpGetAllUser.GET_USER_SUCCESS");
					adapter.notifyDataSetChanged();
					if (list.size() != 0)
						saveUserCard();
					break;
				case AHttpAddContacts.GET_USER_FAILED:
					showToast("unable to access server!");
					break;
				case AHttpAddContacts.NOT_BEFRIEND:
					break;
				case AHttpAddContacts.BEFRIEND:
					showMatchTip();
					break;
				default:
					break;
				}
			}
		};
	}

	public void showMatchTip() {
		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopupWindow();
		}
		popupWindow.showAtLocation(findViewById(android.R.id.content).getRootView(), Gravity.CENTER, 0, 0);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.87f;
		getWindow().setAttributes(lp);
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
	}

	protected void initPopupWindow() {
		View popupWindow_view = getLayoutInflater().inflate(R.layout.item_match, null, false);
		popupWindow = new PopupWindow(popupWindow_view, 800, 800, true);
		popupWindow.setAnimationStyle(R.style.match);
		popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
	}
	public void showToast(String text)
	{
		Toast toast = Toast.makeText(AddContactsActivity.this, text,Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	public void defFling() {
		flingContainer = (FlingAdapterView) findViewById(R.id.frame);
		flingContainer.setAdapter(adapter);

		flingContainer.setFlingListener(new FlingAdapterView.onFlingListener() {
			@Override
			public void removeFirstObjectInAdapter() {
				cuid = list.get(0).getUid();
				nick = list.get(0).getNick();
				list.remove(0);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onLeftCardExit(Object dataObject) {
				makeToast(AddContactsActivity.this, "不喜欢");
			}

			@Override
			public void onRightCardExit(Object dataObject) {
				makeToast(AddContactsActivity.this, "喜欢");

				// server operation
				new AHttpAddContacts(list, uid, handler, helper).rightOperation(cuid);
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
				makeToast(AddContactsActivity.this, "点击图片");
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