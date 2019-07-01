package com.example.facekilling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facekilling.R;
import com.example.facekilling.javabean.Friend;

import java.util.List;

public class FriendAdapter extends ArrayAdapter<Friend> {

    private Context context;
    private List<Friend> friends;
    private int longPos = -1;

    public FriendAdapter(Context context,List<Friend> objects) {
        super(context, R.layout.friend_item, objects);
        this.context = context;
        this.friends = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Friend friend = getItem(position);

        ViewHolder viewHolder;

        View view;

        if(convertView == null){
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false);

            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.touxiang_image);
            viewHolder.name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.message = (TextView) convertView.findViewById(R.id.user_message);

            view = convertView;

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
            view = convertView;
        }

        viewHolder.avatar.setImageBitmap(friend.getImageBitMap());
        viewHolder.name.setText(friend.getUser_name());

        return convertView;
    }



    @Override
    public int getCount() {
        return friends.size();
    }

    private static class ViewHolder {
        TextView name;
        TextView message;
        ImageView avatar;
    }

    public int getLongPos(){
        return longPos;
    }
}
