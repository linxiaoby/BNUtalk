package com.bnutalk.util;
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

public class RecentMsgAdapter extends BaseAdapter{
	private Context context;
	private List<RecentMsgEntity> list;
	LayoutInflater inflater;
	
	public RecentMsgAdapter(Context context,List<RecentMsgEntity> list){
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
		TextView content=(TextView) convertView.findViewById(R.id.tvContent);
		ImageView isRead=(ImageView) convertView.findViewById(R.id.ivTipMsg);
		TextView time=(TextView) convertView.findViewById(R.id.tvTime);

		RecentMsgEntity re=list.get(position);
		avatar.setImageBitmap(re.getAvatar());
		nick.setText(re.getNick());
		content.setText(re.getMsgContent());
		isRead.setImageResource(R.drawable.tips_message);
		time.setText(re.getTime());
		
		if(re.isRead()==1)
			isRead.setVisibility(convertView.GONE);

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
