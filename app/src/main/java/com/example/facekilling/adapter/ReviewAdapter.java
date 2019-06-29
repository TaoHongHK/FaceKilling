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

import org.json.JSONException;

import java.io.IOException;
import java.util.List;


import static com.example.facekilling.util.OkHttpUtils.getUserInfo;

public class ReviewAdapter extends ArrayAdapter<Review>{
    private int resourceId;

    public ReviewAdapter(Context context,int textViewResourceId,List<Review> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    public View getView(int position,View convertView,ViewGroup parent){
        final Review review = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView reviewName = (TextView) view.findViewById(R.id.review_name);
        TextView reviewContent = (TextView) view.findViewById(R.id.review_content);
        int id = review.getUserId();
        final String[] names = {null};

        Thread athread = new Thread(new Runnable() {
            @Override
            public void run() {
                names[0] = getUserInfo(review.getUserId()).getUser_name();
            }
        });
        athread.start();
        try
        {
            athread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        reviewName.setText(names[0]);
        reviewContent.setText(review.getContent());
        return view;
    }
}
