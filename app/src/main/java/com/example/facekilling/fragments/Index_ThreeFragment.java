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
import android.util.Log;
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


import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Intent.getIntent;
import static com.example.facekilling.util.GetBitmap.getBaseFilePath;
import static com.example.facekilling.util.GetBitmap.getBitmapFromSD;
import static com.example.facekilling.util.OkHttpUtils.getCofList;

public class Index_ThreeFragment extends Fragment {

    private View mView;
    private TopBar topBar;
    private DrawerLayout drawerLayout;
    private RecyclerView mRecyclerView;

    private int nowPosition;


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
        //各种监控事件
        monitorMain();
        initView();
        if(cofList.size() == 0) return mView;

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
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.cof_recycler_view);
        GridLayoutManager layoutManger = new GridLayoutManager(getActivity(),1);
        layoutManger.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManger);
        mRecyclerView.setAdapter(cofadapter);


    }
    private void monitorMain(){
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
                intent.putExtra("cofUserId",MainUser.getInstance().getUser_id());
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
                        Log.d("OKHTTP", "onScrollStateChanged: "+nowPosition);
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
    private void initCofs(){
        cofList.clear();
        List<Cof> newCofList = new ArrayList<>();
        try {
            List<Cof> cofList = getCofList(-1,0);
            newCofList.addAll(cofList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cofList.addAll(newCofList);
        cofadapter.notifyDataSetChanged();
        nowPosition = cofList.size();

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
        cofList.addAll(newCofList);
        cofadapter.notifyDataSetChanged();
        nowPosition = cofList.size();
    }
}
