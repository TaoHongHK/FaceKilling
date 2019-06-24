package com.example.facekilling.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facekilling.R;
import com.example.facekilling.javabean.ChatMessage;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {//适配器
    private List<ChatMessage> list;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView leftimg, rightimg;
        TextView lefttext, righttext;
        ViewGroup leftlin, rightlin;

        public ViewHolder(View itemView) {
            super(itemView);
            leftlin = itemView.findViewById(R.id.left);
            leftimg = itemView.findViewById(R.id.leftimg);
            lefttext = itemView.findViewById(R.id.lefttext);
            rightlin = itemView.findViewById(R.id.right);
            rightimg = itemView.findViewById(R.id.rightimg);
            righttext = itemView.findViewById(R.id.righttext);
        }
    }

    public ChatMessageAdapter(List l) {
        list = l;
    }

    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_show, parent, false);
        ViewHolder h = new ViewHolder(v);
        Log.d("MainActivity", "onCreate");
        return h;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//滑动RecyclerView出发的事件
        ChatMessage x = list.get(position);
        if (x.left) {//判断该信息该信息是显示在左边还是右边，如果要在左边显示则把右边的部分隐藏
            holder.leftlin.setVisibility(View.VISIBLE);
            holder.rightlin.setVisibility(View.GONE);//把右边的隐藏
            holder.leftimg.setImageResource(x.img);
            holder.lefttext.setText(x.text);
        }else{
            holder.rightlin.setVisibility(View.VISIBLE);
            holder.leftlin.setVisibility(View.GONE);//把左边的隐藏
            holder.leftimg.setImageResource(x.img);
            holder.lefttext.setText(x.text);
        }
        Log.d("MainActivity", "onBind");
    }

    @Override
    public int getItemCount() {//这里要重写一下 不然不会显示任何信息
        Log.d("asdasd", "" + list.size());
        return list.size();
    }
}
