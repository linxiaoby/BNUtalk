package com.bnutalk.ui;
import java.util.List;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bnutalk.ui.R;
import com.bnutalk.util.RecentMsgEntity;
import com.bnutalk.util.UserEntity;
/**
 * created by linxiaobai 2016-05-21
 * @author 王琳—PC
 *
 */
public class ContactAdapter extends BaseAdapter{
	private Context context;
	private List<UserEntity> list;
	LayoutInflater inflater;
	
	public ContactAdapter(Context context,List<UserEntity> list){
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
/**
 * simplew getView,still needs  fine tuning
 */
	public View getView(int position, View convertView, ViewGroup root) {
		convertView = inflater.inflate(R.layout.item_msgfriend_list, null);
		
		ImageView avatar=(ImageView) convertView.findViewById(R.id.ivAvatar);
		TextView nick=(TextView) convertView.findViewById(R.id.tvNick);
		TextView nationality=(TextView) convertView.findViewById(R.id.tvContent);

		UserEntity re=list.get(position);
		
		avatar.setImageBitmap(re.getAvatar());
		nick.setText(re.getNick());
		nationality.setText(re.getNationality());

		return convertView;
	}
	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
}
