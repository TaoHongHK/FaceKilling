package com.example.facekilling.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.facekilling.R;

import java.util.ArrayList;
import java.util.List;

public class CofAdapter extends RecyclerView.Adapter<CofAdapter.ViewHolder>{

    private Context mContext;
    private List<Cof> mCofList;
    private Context mcontext;


    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView headimage;
        TextView head_name;
        TextView content;
        ImageView images;
        TextView like_num;
        ListView listView;

        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            headimage = (ImageView) view.findViewById(R.id.cof_item_layout_headimage);
            head_name = (TextView) view.findViewById(R.id.cof_item_layout_head);
            content = (TextView) view.findViewById(R.id.cof_item_layout_content);
            images = (ImageView) view.findViewById(R.id.cof_item_layout_image);
            like_num = (TextView) view.findViewById(R.id.cof_item_layout_like_num);
            listView = (ListView) view.findViewById(R.id.review_recycler_view);
        }
    }

    public CofAdapter(Context context,List<Cof> cofList) {
        mCofList = cofList;
        mcontext = context;
    }


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
        String string = cof.getPraise_num() + "  人点赞";
        holder.like_num.setText(string);

        List<Review> reviewList = new ArrayList<>();
        reviewList.addAll(cof.getReviewList());

        ReviewAdapter reviewAdapter = new ReviewAdapter(mContext,R.layout.review_item,reviewList);
        ListView listView = holder.listView;


        int totalHeight = 0;
        for (int i = 0, len = reviewAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = reviewAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (reviewAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);

        listView.setAdapter(reviewAdapter);

    }


    public int getItemCount() {
        return mCofList.size();
    }
}
