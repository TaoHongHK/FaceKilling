package com.example.facekilling.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.activities.CofActivity;
import com.example.facekilling.activities.IndexActivity;
import com.example.facekilling.activities.MyCofActivity;
import com.example.facekilling.customviews.TopBar;
import com.example.facekilling.javabean.Cof;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.javabean.Picture;
import com.example.facekilling.adapter.CofAdapter;
import com.example.facekilling.javabean.Review;
import com.example.facekilling.javabean.User;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Intent.getIntent;
import static com.example.facekilling.util.GetBitmap.getBaseFilePath;
import static com.example.facekilling.util.GetBitmap.getBitmapFromSD;

public class Index_ThreeFragment extends Fragment {

    private View mView;
    private TopBar topBar;
    private DrawerLayout drawerLayout;


    private List<Cof> myCofList = new ArrayList<>();



//    private Cof[] cofs = {
//            new Cof(MainUser.getInstance().getUser_id(),"这是一个测试1"),
//            new Cof(MainUser.getInstance().getUser_id(),"这是一个测试2"),
//            new Cof(MainUser.getInstance().getUser_id(),"这是一个测试3"),
//            new Cof(MainUser.getInstance().getUser_id(),"这是一个测试4"),
//            new Cof(MainUser.getInstance().getUser_id(),"这是一个测试5"),
//            new Cof(MainUser.getInstance().getUser_id(),"这是一个测试6"),
//    };
    private Cof[] cofs = {
            new Cof(2,"这是一个测试1"),
            new Cof(2,"这是一个测试2"),
            new Cof(2,"这是一个测试3"),
            new Cof(2,"这是一个测试4"),
            new Cof(2,"这是一个测试5"),
            new Cof(2,"这是一个测试6"),
    };



    private List<Cof> cofList = new ArrayList<>();
    private CofAdapter cofadapter = new CofAdapter(getActivity(),cofList);

    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;

    private Cof newCof;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_index_three,container,false);
        initCofs();
        initView();
        //各种监控事件
        monitor();
        return mView;
    }

    public void initView(){
        topBar = (TopBar) mView.findViewById(R.id.cofTopBar);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);


        ImageView imageview = mView.findViewById(R.id.cof_background);
        imageview.setImageResource(R.drawable.picture_01);


        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.cof_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCofs();
            }
        });


        if(cofList.size() == 0) return;
        //卡片式显示朋友圈
        onAttach(getActivity());
        if(newCof != null){
            cofList.add(0,newCof);
        }
        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.cof_recycler_view);
        GridLayoutManager layoutManger = new GridLayoutManager(getActivity(),1);
        layoutManger.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManger);
        recyclerView.setAdapter(cofadapter);


    }
    private void monitor(){
        topBar = (TopBar) mView.findViewById(R.id.cofTopBar);
        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            //侧边栏
            public void leftClicked() {
                drawerLayout.openDrawer(Gravity.START);
            }

            @Override
            //发表朋友圈
            public void rightClicked() {
                Intent intent = new Intent(getActivity(), CofActivity.class);
                startActivity(intent);
            }
        });
        //监控悬浮按钮
        FloatingActionButton head_img = (FloatingActionButton) mView.findViewById(R.id.cof_head_img);
        head_img.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getActivity(),MyCofActivity.class);
                startActivity(intent);
            }
        });




    }
    private void initCofs(){
        //TODO:写一个从服务器端获取所有cof的函数
        cofList.clear();
        //测试
        Review review = new Review(MainUser.getInstance().getUser_id(),"评论测试");
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review);
        String path = getBaseFilePath("5") + File.separator + "20190628021926.jpg";
        Bitmap bitmap = getBitmapFromSD(path);
        Picture picture = new Picture(bitmap,path);
        List<Picture> pictureList = new ArrayList<>();
        pictureList.add(picture);

        for(int i=0;i<10;i++){
            Random random = new Random();
            int index =  random.nextInt(cofs.length);
            cofs[index].setImagesList(pictureList);
            cofs[index].setReviewList(reviewList);
            cofList.add(cofs[index]);
        }
    }

    private void refreshCofs(){
        new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable(){
                    public void run(){
                        initCofs();
                        cofadapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        newCof = ((IndexActivity) activity).getCof();
    }
}
