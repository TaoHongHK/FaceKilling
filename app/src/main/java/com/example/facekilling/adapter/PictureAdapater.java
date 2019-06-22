package com.example.facekilling.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.facekilling.R;
import com.example.facekilling.javabean.Picture;

import java.util.List;

public class PictureAdapater extends RecyclerView.Adapter<PictureAdapater.ViewHolder> {

    private Context mContext;
    private List<Picture> mPictureList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView pictureImage;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            pictureImage = (ImageView) view.findViewById(R.id.pictures_image);
        }
    }

    public PictureAdapater(List<Picture> pictureList) {
        mPictureList = pictureList;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picture picture = mPictureList.get(position);
        Glide.with(mContext).load(picture.getImageId()).into(holder.pictureImage);
    }

    @Override
    public int getItemCount() {
        return mPictureList.size();
    }


}
