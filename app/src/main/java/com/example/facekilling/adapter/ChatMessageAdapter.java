package com.example.facekilling.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.facekilling.R;
import com.example.facekilling.javabean.ChatMessage;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {//适配器
    private List<ChatMessage> list;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView leftimg, rightimg;
        TextView lefttext, righttext;
        LinearLayout leftLingerLayout,rightLingerLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            leftimg = (ImageView) itemView.findViewById(R.id.chat_left_img);
            lefttext = (TextView) itemView.findViewById(R.id.chat_left_text);
            rightimg = (ImageView) itemView.findViewById(R.id.chat_right_img);
            righttext = (TextView) itemView.findViewById(R.id.chat_right_text);
            leftLingerLayout = (LinearLayout) itemView.findViewById(R.id.chat_msg_left);
            rightLingerLayout =  (LinearLayout) itemView.findViewById(R.id.chat_msg_right);
        }
    }

    public ChatMessageAdapter(List<ChatMessage> list) {
        this.list = list;
    }

    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//滑动RecyclerView出发的事件
        ChatMessage message = list.get(position);
        if (message.getType() == ChatMessage.TYPE_RECEIVE) {//判断该信息该信息是显示在左边还是右边，如果要在左边显示则把右边的部分隐藏
            holder.leftLingerLayout.setVisibility(View.VISIBLE);
            holder.rightLingerLayout.setVisibility(View.GONE);//把右边的隐藏
            holder.leftimg.setImageBitmap(message.getImg());
            holder.lefttext.setText(message.getContent());
        }else{
            holder.rightLingerLayout.setVisibility(View.VISIBLE);
            holder.leftLingerLayout.setVisibility(View.GONE);//把左边的隐藏
            holder.rightimg.setImageBitmap(message.getImg());
            holder.righttext.setText(message.getContent());
        }
    }

    @Override
    public int getItemCount() {//这里要重写一下 不然不会显示任何信息
        return list.size();
    }
}
