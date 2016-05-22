package com.bnutalk.ui;
/*
 * 在cardmode的基础上定义一个card的适配器，并用viewholder讲card集合存储起来
 * 并用glide的方式展示出来
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.bnutalk.ui.R;
import com.bnutalk.util.UserEntity;
import com.bumptech.glide.Glide;

public class CardAdapter extends BaseAdapter {
    //定义一个场景
    private Context mContext;
    //cardmode类型的数组
    private List<UserEntity> mCardList;

    public CardAdapter(Context mContext, List<UserEntity> mCardList) {
        this.mContext = mContext;
        this.mCardList = mCardList;
    }
    //类定义的一些函数接口
    @Override
    public int getCount() {
        return mCardList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            //实例化LayoutInflater
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_add_contact, parent, false);
            //对holder赋值
            holder = new ViewHolder();
//            holder.mCardImageView = (RotateTextImageView) convertView.findViewById(R.id.helloText);
            
            holder.mCardImageView = (ImageView) convertView.findViewById(R.id.helloText);
            holder.mCardName = (TextView) convertView.findViewById(R.id.card_name);
            holder.mCardCollege = (TextView) convertView.findViewById(R.id.card_college);
            holder.mCardGender =(ImageView) convertView.findViewById(R.id.iv_card_gender);
            holder.mCardYear = (TextView) convertView.findViewById(R.id.card_year);
            holder.mCardAddress = (TextView) convertView.findViewById(R.id.card_address);
            holder.mCardWantLanuage = (TextView) convertView.findViewById(R.id.card_want_lanuage);
            holder.mCardMotherLanuage = (TextView) convertView.findViewById(R.id.card_mother_lanuage);
            //设置标签
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //用一个Glide的方法
       
//        Glide.with(mContext)
//                .load(mCardList.get(position).getImages())
//                .into(holder.mCardImageView);
        holder.mCardImageView.setImageBitmap(mCardList.get(position).getAvatar());
        holder.mCardName.setText(mCardList.get(position).getNick());
        holder.mCardCollege.setText(mCardList.get(position).getFaculty());
        holder.mCardWantLanuage.setText(mCardList.get(position).getLikeLanguage());
        holder.mCardMotherLanuage.setText(mCardList.get(position).getMotherTone());
        holder.mCardAddress.setText(mCardList.get(position).getPlace());
        holder.mCardYear.setText(String.valueOf(mCardList.get(position).getAge()));
        if(mCardList.get(position).getSex()==0)
        	holder.mCardGender.setImageResource(R.drawable.man);
        else
        	holder.mCardGender.setImageResource(R.drawable.woman);
        return convertView;
    }
//定义viewHolder
    class ViewHolder {
        TextView mCardName;
        TextView mCardYear;
        TextView mCardCollege;
        ImageView mCardGender;
        TextView mCardWantLanuage;
        TextView mCardMotherLanuage;
        TextView mCardAddress;
//        RotateTextImageView mCardImageView;
        ImageView mCardImageView;
    }
}
