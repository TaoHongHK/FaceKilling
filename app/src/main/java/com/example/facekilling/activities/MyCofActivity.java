package com.example.facekilling.activities;

import android.content.Intent;
import android.graphics.Bitmap;
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

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.facekilling.util.GetBitmap.getBitmapFromSD;
import static com.example.facekilling.util.OkHttpUtils.getCofList;
import static com.example.facekilling.util.StaticConstant.TACK_DEFAULT;
import static com.example.facekilling.util.StaticConstant.TACK_PICTURE_TO_COF;

public class MyCofActivity extends AppCompatActivity {


    private TopBar topBar;
    private DrawerLayout drawerLayout;

    //存储我的朋友圈的cof信息
    private List<Cof> myCofList = new ArrayList<>();
    private int nowPosition;

    private RecyclerView mRecyclerView;

    private CofAdapter cofadapter = new CofAdapter(MyCofActivity.this,myCofList);

    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;

    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cof);

        Intent intent = getIntent();
        id = intent.getIntExtra("cofUserId",0);
        //各种监控事件
        monitorMain();
        //接受数据
        initCofList(id);
        initView();
        if(myCofList.size() == 0) return;

        monitor();

    }

    public void initCofList(int id){
        if(id == 0){
            return;
        }
        myCofList.clear();
        List<Cof> newCofList = new ArrayList<>();
        try {
            List<Cof> cofList = getCofList(id,0);
            newCofList.addAll(cofList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        myCofList.addAll(newCofList);
        cofadapter.notifyDataSetChanged();
        nowPosition = myCofList.size();
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
        mRecyclerView = (RecyclerView) findViewById(R.id.myCof_recycler_view);
        GridLayoutManager layoutManger = new GridLayoutManager(MyCofActivity.this,1);
        layoutManger.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManger);

        mRecyclerView.setAdapter(cofadapter);

    }
    private  void monitorMain(){
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

    private void monitor(){

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //判断是不是往上拖动
            public boolean isLastReflash;


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*
                 * 滑动停止之后检测是否滑动到底部
                 * */
                if(newState == RecyclerView.SCROLL_STATE_IDLE &&isLastReflash){
                    if(mRecyclerView.computeVerticalScrollExtent()+recyclerView.computeVerticalScrollOffset()>=recyclerView.computeVerticalScrollRange()){
                        // Toast.makeText(getContext(),"滑动到底部",Toast.LENGTH_SHORT).show();
                        //滑动到底部的时候一般要做加载更多的数据的操作...
                        loadMoreCof(nowPosition);
                        cofadapter.notifyDataSetChanged();
                    }
                }
            }
            //根据dy，dx可以判断是往哪个方向滑动
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    isLastReflash = true;
                }else{
                    isLastReflash = false;
                }
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

    public void loadMoreCof(int index){
        List<Cof> newCofList = new ArrayList<>();
        try {
            List<Cof> cofList = getCofList(-1,index);
            newCofList.addAll(cofList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        myCofList.addAll(newCofList);
        cofadapter.notifyDataSetChanged();
        nowPosition = myCofList.size();
    }
}
