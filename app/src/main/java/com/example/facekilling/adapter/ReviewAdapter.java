package com.example.facekilling.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facekilling.R;
import com.example.facekilling.activities.MyCofActivity;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.javabean.Review;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;


import static com.example.facekilling.util.OkHttpUtils.getEmailByIdY;
import static com.example.facekilling.util.OkHttpUtils.getUserInfoY;
import static com.example.facekilling.util.OkHttpUtils.postAddFriendY;
import static com.example.facekilling.util.OkHttpUtils.postDisReviewCof;

public class ReviewAdapter extends ArrayAdapter<Review>{
    private int resourceId;
    private Context mContext;
    private int userId;
    private int cofId;
    private List<Review> reviewList;
    //TODO 帖子与评论不匹配
    public ReviewAdapter(Context context,int textViewResourceId,List<Review> objects,int user_id,int cof_id){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        mContext = context;
        this.userId = user_id;
        this.cofId = cof_id;
        this.reviewList = objects;
    }

    public View getView(int position,View convertView,ViewGroup parent){
        final Review review = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView reviewName = (TextView) view.findViewById(R.id.review_name);
        TextView reviewContent = (TextView) view.findViewById(R.id.review_content);
        int id = review.getUserId();
        String name;

        name = getUserInfoY(review.getUserId()).getUser_name();
        reviewName.setText(name);
        reviewContent.setText(review.getContent());

        reviewName.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                //    指定下拉列表的显示数
                final String[] cities = {"添加好友","进入这位朋友的社区"};
                //    设置一个下拉的列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch(which){
                            case 0:
                                //添加好友
                                final int user_id = review.getUserId();
                                String email = getEmailByIdY(user_id);
                                int error = postAddFriendY(MainUser.getInstance(),email);

                                AlertDialog.Builder errorBuider = new AlertDialog.Builder(mContext);
                                errorBuider.setTitle("     温馨提示");
                                switch (error){
                                    case 0:
                                        errorBuider.setMessage("好友添加成功");
                                        break;
                                    case 3:
                                        errorBuider.setMessage("你们已经好友了");
                                        break;
                                    default:
                                        errorBuider.setMessage("好友添加失败");
                                        break;
                                }
                                errorBuider.show();
                                break;
                            case 1:
                                Intent intent = new Intent(mContext, MyCofActivity.class);
                                intent.putExtra("cofUserId",review.getUserId());
                                mContext.startActivity(intent);
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        //如果是该用户的评论或者该用户的帖子，可以删除评论
        reviewContent.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if((review.getUserId() != MainUser.getInstance().getUser_id()) &&
                        (userId != MainUser.getInstance().getUser_id())) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                //    指定下拉列表的显示数
                final String[] cities = {"删除评论"};
                //    设置一个下拉的列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch(which){
                            case 0:
                                //删除评论
                                postDisReviewCof(cofId,review.getReviewId());
                                for(int i=0;i<reviewList.size();i++){
                                    if(reviewList.get(i).getReviewId() == review.getReviewId()){
                                        reviewList.remove(i);
                                        break;
                                    }
                                }
                                notifyDataSetChanged();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        return view;
    }
}
