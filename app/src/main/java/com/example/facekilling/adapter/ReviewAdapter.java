package com.example.facekilling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facekilling.R;
import com.example.facekilling.javabean.Review;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review>{
    private int resourceId;

    public ReviewAdapter(Context context,int textViewResourceId,List<Review> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    public View getView(int position,View convertView,ViewGroup parent){
        Review review = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView reviewImage = (ImageView) view.findViewById(R.id.review_img);
        TextView reviewContent = (TextView) view.findViewById(R.id.review_content);
        reviewImage.setImageResource(review.getUser().getImageId());
        reviewContent.setText(review.getContent());
        return view;
    }
}
