package com.example.facekilling.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.facekilling.R;
import com.example.facekilling.activities.CofActivity;
import com.example.facekilling.activities.PicturesActivity;
import com.example.facekilling.javabean.Picture;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PictureAdapater extends RecyclerView.Adapter<PictureAdapater.ViewHolder> {

    private Context mContext;
    private List<Picture> mPictureList;
    private boolean checkBoxVisiable = false;   //标志是否有选择框
    private boolean isLongStatus = false;    //标志是否可以长按选择
    private int longPosition = -1;


    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView pictureImage;
        CheckBox checkBox;
        FrameLayout frameLayout;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            pictureImage = (ImageView) view.findViewById(R.id.pictures_image);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            frameLayout = (FrameLayout) view.findViewById(R.id.picture_item_layout);
        }
    }

    public PictureAdapater(List<Picture> pictureList) {
        mPictureList = pictureList;
    }

    public void addPicture(Picture picture){
        mPictureList.add(picture);
        this.notifyItemInserted(mPictureList.size()-1);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.pictures_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picture picture = mPictureList.get(position);
        Glide.with(mContext).load(picture.getImageId()).into(holder.pictureImage);

        //长按事件
        holder.pictureImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(isLongStatus){
                    longPosition = position;
                }
                return false;
            }
        });


        //检测是否有checkbox
        if(checkBoxVisiable){
            holder.checkBox.setVisibility(View.VISIBLE);
            //点击checkbox所在item改变其状态
            final ViewHolder vv = holder;
            final int index = position;
            holder.frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(vv.checkBox.isChecked()){
//                        vv.checkBox.setChecked(false);
                        mPictureList.get(index).setChecked(true);
                    }
                    else{
//                        vv.checkBox.setChecked(true);
                        mPictureList.get(index).setChecked(false);
                    }
                }
            });
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        mPictureList.get(index).setChecked(true);
                    }else{
                        mPictureList.get(index).setChecked(false);
                    }
                }
            });
            if(mPictureList.get(position).isChecked()){
                holder.checkBox.setChecked(true);
            }else{
                holder.checkBox.setChecked(false);
            }
            //长按监听



        }
        else{
            holder.checkBox.setVisibility(View.INVISIBLE);
        }


    }


    @Override
    public int getItemCount() {
        return mPictureList.size();
    }

    public void setCheckBoxVisiable(boolean checkBoxVisiable) {
        this.checkBoxVisiable = checkBoxVisiable;
    }

    public int getLongPosition(){
        return longPosition;
    }

    public void setLongStatus(boolean longStatus) {
        isLongStatus = longStatus;
    }
}
