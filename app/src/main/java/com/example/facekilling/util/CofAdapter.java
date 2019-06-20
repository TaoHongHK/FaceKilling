package com.example.facekilling.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.facekilling.R;

import java.util.List;

public class CofAdapter extends RecyclerView.Adapter<CofAdapter.ViewHolder> {

    private Context mContext;
    private List<Cof> mCofList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView headimage;
        TextView head_name;
        TextView content;
        ImageView images;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            headimage = (ImageView) view.findViewById(R.id.cof_item_layout_headimage);
            head_name = (TextView) view.findViewById(R.id.cof_item_layout_head);
            content = (TextView) view.findViewById(R.id.cof_item_layout_content);
            images = (ImageView) view.findViewById(R.id.cof_item_layout_image);
        }
    }

    public CofAdapter(List<Cof> cofList) {
        mCofList = cofList;
    }


    @NonNull
    @Override
    public CofAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.cof_item,parent,false);
        return new CofAdapter.ViewHolder(view);
    }


    public void onBindViewHolder(@NonNull CofAdapter.ViewHolder holder, int position) {
        Cof cof = mCofList.get(position);
        Glide.with(mContext).load(cof.getUser_image()).into(holder.headimage);
        holder.head_name.setText(cof.getUser_name());
        holder.content.setText(cof.getContent());
        Glide.with(mContext).load(cof.getImage()).into(holder.images);
    }


    public int getItemCount() {
        return mCofList.size();
    }
}
