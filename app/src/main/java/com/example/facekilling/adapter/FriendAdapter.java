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
    private int resourceId;

    public FriendAdapter(Context context, int resource, List<Friend> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Friend user = getItem(position);
        View view;
        view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        ImageView touxiang = (ImageView) view.findViewById(R.id.touxiang_image);
        TextView userName = (TextView) view.findViewById(R.id.user_name);
        TextView message = (TextView) view.findViewById(R.id.user_message);
        touxiang.setImageResource(user.getImageId());
        userName.setText(user.getUser_name());
        message.setText(user.getMessage());
        return view;
    }
}
