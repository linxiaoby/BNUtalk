package com.bnutalk.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.DefaultDatabaseErrorHandler;
import android.graphics.Bitmap;
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
import com.bnutalk.util.CommonUtil;
import com.bnutalk.util.DBopenHelper;
import com.bnutalk.util.FlingAdapterView;
import com.bnutalk.util.MyApplication;
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
	private UserEntity uEntity;
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
	private MyApplication myApp;
	private Bitmap avatar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		android.util.Log.v(TAG, "onCreate() called!");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addfriend_main);
		initView();
	}

	public void getSelfInfo() {
		helper.getSelfInfo(uid, myApp.getSelfInfoList());
		avatar = myApp.getSelfInfoList().get(0).getAvatar();
	}

	@Override
	protected void onResume() {
		super.onResume();
		android.util.Log.v(TAG, "onResume() called!");
		getSelfInfo();
		getUserCard();
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
	public void initView() {
		// 定义左边和右边的图片，和监听
		left = (ImageView) findViewById(R.id.left);
		right = (ImageView) findViewById(R.id.right);
		music = (ImageView) findViewById(R.id.iv_card_flag6);
		back = (Button) findViewById(R.id.btAddConBack);
		list = new ArrayList<UserEntity>();
		adapter = new CardAdapter(AddContactsActivity.this, list);

		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (list.size() >0)
					left();
			}
		});
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (list.size() >0)
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
		myApp = (MyApplication) getApplicationContext();
		uid = myApp.getUid();
		helper = new DBopenHelper(getApplicationContext());
	}

	public void getUserCard() {
		helper.getUserCard(uid, list);
		if (list.size() == 0)// read from server
		{
			new AHttpAddContacts(list, uid, handler, helper).getAllUser();
			Toast.makeText(AddContactsActivity.this, "Loading, please wait!", Toast.LENGTH_LONG).show();
		} else
			adapter.notifyDataSetChanged();
	}

	public void saveUserCard() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<UserEntity> tlist=new ArrayList<UserEntity>(); 
				tlist.addAll(list);
				helper.addUserCard(uid, tlist);
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
					if(list.size()!=0)
					{
					android.util.Log.v("msg.what", "AHttpGetAllUser.GET_USER_SUCCESS");
					adapter.notifyDataSetChanged();
					saveUserCard();
					}
					else 
					Toast.makeText(AddContactsActivity.this, "no more users!", Toast.LENGTH_LONG).show();	
					break;
				case AHttpAddContacts.GET_USER_FAILED:
					showToast("unable to access server!");
					break;
				case AHttpAddContacts.NOT_BEFRIEND:
					break;
				case AHttpAddContacts.BEFRIEND:
					saveContact();
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
		ImageView user1 = (ImageView) popupWindow_view.findViewById(R.id.user1);
		ImageView user2 = (ImageView) popupWindow_view.findViewById(R.id.user2);
		// read self info from local,else it can be nullpoint
		user1.setImageBitmap(avatar);
		user2.setImageBitmap(uEntity.getAvatar());
		Button back = (Button) popupWindow_view.findViewById(R.id.back);
		Button sendMsg = (Button) popupWindow_view.findViewById(R.id.send_message);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				popupWindow = null;
			}
		});
		sendMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				popupWindow = null;
				
				UserEntity tEntity=new UserEntity();
				tEntity=uEntity;
				tEntity.setUid(uEntity.getUid());
				tEntity.setNick(uEntity.getNick());
				tEntity.setAvatar(uEntity.getAvatar());
				android.util.Log.v("uEntity3", uEntity.getNationality());
				Bundle bundle = new Bundle();
				bundle.putString("cuid", tEntity.getUid());
				bundle.putString("cnick", tEntity.getNick());
				bundle.putByteArray("cavatar", CommonUtil.Bitmap2Bytes(tEntity.getAvatar()));
				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(AddContactsActivity.this, ChatActivity.class);
				startActivity(intent);
			}
		});
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

	public void saveContact() {
		new Thread(new Runnable() {
			public void run() {
				android.util.Log.v("uEntity2", uEntity.getNationality());
				helper.addContact(uid, uEntity);
			}
		}).start();
	}

	public void showToast(String text) {
		Toast toast = Toast.makeText(AddContactsActivity.this, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void defFling() {
		flingContainer = (FlingAdapterView) findViewById(R.id.frame);
		flingContainer.setAdapter(adapter);

		flingContainer.setFlingListener(new FlingAdapterView.onFlingListener() {
			@Override
			public void removeFirstObjectInAdapter() {
				uEntity = list.get(0);
				android.util.Log.v("uEntity1", uEntity.getNationality());
				cuid = uEntity.getUid();
				nick = uEntity.getNick();
				list.remove(0);
				adapter.notifyDataSetChanged();
				if(list.size()==0)
					Toast.makeText(AddContactsActivity.this, "no more users!", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onLeftCardExit(Object dataObject) {
				if (list.size() <0)
				Toast.makeText(AddContactsActivity.this, "no more users!", Toast.LENGTH_LONG).show();
				else
					makeToast(AddContactsActivity.this, "dislike");
			}

			@Override
			public void onRightCardExit(Object dataObject) {
				if (list.size() < 0)
					Toast.makeText(AddContactsActivity.this, "no more users!", Toast.LENGTH_LONG).show();
				else {
					makeToast(AddContactsActivity.this, "like");
					// server operation
					new AHttpAddContacts(list, uid, handler, helper).rightOperation(cuid);
				}
			}

			@Override
			public void onAdapterAboutToEmpty(int itemsInAdapter) {
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
				// makeToast(AddContactsActivity.this, "clike image");
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