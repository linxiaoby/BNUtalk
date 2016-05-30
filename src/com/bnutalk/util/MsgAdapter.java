package com.bnutalk.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import com.bnutalk.ui.R;

/**
 * Created by huangtianyous on 2016/4/9.
 */
public class MsgAdapter extends ArrayAdapter<MsgEntity> {
	private int resourceId;
	private String receiver_name;

	public MsgAdapter(Context context, int textViewResourceId, List<MsgEntity> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	public String getReceiver_name() {// get receiver's name from server here
		receiver_name = "Richard";
		return receiver_name;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MsgEntity msg = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.photo_receiver = (ImageView) view.findViewById(R.id.photo_receiver);
			viewHolder.photo_sender = (ImageView) view.findViewById(R.id.photo_sender);
			viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
			viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
			viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
			viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}

		if (msg.getType() == MsgEntity.TYPE_RECEIVED) {
			viewHolder.leftLayout.setVisibility(View.VISIBLE);
			viewHolder.rightLayout.setVisibility(View.GONE);
			// set face image
			// viewHolder.photo_receiver.setImageBitmap(msg.getReceiver());
			viewHolder.photo_receiver.setVisibility(View.VISIBLE);
			viewHolder.photo_sender.setVisibility(View.GONE);
			viewHolder.leftMsg.setText(msg.getContent());
			viewHolder.photo_receiver.setImageBitmap(msg.getCavatar());
		} else if (msg.getType() == MsgEntity.TYPE_SENT) {
			viewHolder.rightLayout.setVisibility(View.VISIBLE);
			viewHolder.leftLayout.setVisibility(View.GONE);
			// set face image
			// viewHolder.photo_sender.setImageBitmap(msg.getSender());
			viewHolder.photo_sender.setVisibility(View.VISIBLE);
			viewHolder.photo_receiver.setVisibility(View.GONE);
			viewHolder.rightMsg.setText(msg.getContent());
			viewHolder.photo_sender.setImageBitmap(msg.getAvatar());
		}
		return view;
	}

	class ViewHolder {
		LinearLayout leftLayout;
		LinearLayout rightLayout;
		TextView leftMsg;
		TextView rightMsg;
		ImageView photo_receiver;
		ImageView photo_sender;
	}

}
