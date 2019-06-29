package com.example.facekilling.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.example.facekilling.R;
import com.example.facekilling.adapter.CofAdapter;
import com.example.facekilling.customviews.TopBar;
import com.example.facekilling.javabean.Cof;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.javabean.Picture;
import com.example.facekilling.javabean.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyCofActivity extends AppCompatActivity {


    private TopBar topBar;
    private DrawerLayout drawerLayout;

    //存储我的朋友圈的cof信息
    private List<Cof> myCofList = new ArrayList<>();

    private CofAdapter cofadapter;

    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cof);

        //接受数据
        initCofList();
        initView();
        //各种监控事件
        monitor();
    }

    public void initCofList(){
        //TODO:从服务器段获取登陆用户自己的cof
    }

    public void initView(){
        topBar = (TopBar) findViewById(R.id.myCofTopBar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);


        //背景
        ImageView imageview = findViewById(R.id.myCof_background);
        /*imageview.setImageBitmap(MainUser.getInstance().getImageBitMap());*/
        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.myCof_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCofs();
            }
        });

        if(myCofList.size() == 0) return;
        //卡片式显示朋友圈
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.myCof_recycler_view);
        GridLayoutManager layoutManger = new GridLayoutManager(MyCofActivity.this,1);
        layoutManger.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManger);
        cofadapter = new CofAdapter(MyCofActivity.this,myCofList);
        recyclerView.setAdapter(cofadapter);

    }

    private void monitor(){
        topBar = (TopBar) findViewById(R.id.myCofTopBar);
        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            //侧边栏
            public void leftClicked() {
                finish();
            }

            @Override
            //发表朋友圈
            public void rightClicked() {
                Intent intent = new Intent(MyCofActivity.this, CofActivity.class);
                List<Picture> mPictureList = new ArrayList<>();
                intent.putExtra("PictureList",(Serializable)mPictureList);
                startActivity(intent);
            }
        });




    }

    private void refreshCofs(){
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable(){
                    public void run(){
                        cofadapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
