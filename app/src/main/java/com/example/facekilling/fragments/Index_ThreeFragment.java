package com.example.facekilling.fragments;

import android.app.Activity;
import android.content.Intent;
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
import com.example.facekilling.javabean.User;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Intent.getIntent;

public class Index_ThreeFragment extends Fragment {

    private View mView;
    private TopBar topBar;
    private DrawerLayout drawerLayout;


    private List<Cof> myCofList = new ArrayList<>();

    //测试
    //显示信息
    private List<Picture> images= new ArrayList<>();
    private List<Picture> imagesList = new ArrayList<>();

    private User[] users = {
            new User("测试1",R.drawable.picture_01,MainUser.getInstance().getEmail()),
            new User("测试2",R.drawable.picture_02,"12345478@163.com"),
            new User("测试3",R.drawable.picture_03,"12345578@163.com"),
            new User("测试4",R.drawable.picture_04,"12345778@163.com"),
            new User("测试5",R.drawable.picture_05,"123451378@163.com"),
    };

    private Cof[] cofs = {
            new Cof(users[0],"这是一个测试1"),
            new Cof(users[1],"这是一个测试2"),
            new Cof(users[2],"这是一个测试3"),
            new Cof(users[3],"这是一个测试4"),
            new Cof(users[4],"这是一个测试5"),
            new Cof(users[0],"这是一个测试6"),
            new Cof(users[0],"这是一个测试7"),
    };
    //用于测试显示图片
    private Picture[] pictures = {
            new Picture(R.drawable.picture_01),
            new Picture(R.drawable.picture_02),
            new Picture(R.drawable.picture_03),
            new Picture(R.drawable.picture_04),
            new Picture(R.drawable.picture_05),
            new Picture(R.drawable.picture_06),
            new Picture(R.drawable.picture_07),
            new Picture(R.drawable.picture_08),
            new Picture(R.drawable.picture_09),
            new Picture(R.drawable.picture_10),
            new Picture(R.drawable.picture_11),
            new Picture(R.drawable.picture_12),
            new Picture(R.drawable.picture_13),
            new Picture(R.drawable.picture_14),
    };

    private List<Cof> cofList = new ArrayList<>();
    private CofAdapter cofadapter;

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
        initView();

        return mView;
    }

    public void initView(){
        topBar = (TopBar) mView.findViewById(R.id.cofTopBar);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);




        ImageView imageview = mView.findViewById(R.id.cof_background);
        imageview.setImageResource(R.drawable.picture_01);
        //卡片式显示朋友圈
        initCofs();
        onAttach(getActivity());
        if(newCof != null){
            cofList.add(0,newCof);
        }
        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.cof_recycler_view);
        GridLayoutManager layoutManger = new GridLayoutManager(getActivity(),1);
        layoutManger.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManger);
        cofadapter = new CofAdapter(getActivity(),cofList);
        recyclerView.setAdapter(cofadapter);

        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.cof_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCofs();
            }
        });


        //各种监控事件
        monitor();


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

                List<Picture> mPictureList = new ArrayList<>();   //发送一个空的默认值
                intent.putExtra("PictureList",(Serializable)mPictureList);
                startActivity(intent);
            }
        });
        //监控悬浮按钮
        FloatingActionButton head_img = (FloatingActionButton) mView.findViewById(R.id.cof_head_img);
        head_img.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                myCofList.clear();
                for(Cof cof:cofList){
                    if(cof.getUser().equals(MainUser.getInstance())){
                        myCofList.add(cof);
                    }
                }
                Intent intent = new Intent(getActivity(), MyCofActivity.class);
                intent.putExtra("cofList",(Serializable)myCofList);
                startActivity(intent);
            }
        });




    }
    private void initCofs(){
        images.add(new Picture(R.drawable.picture_01));
        images.add(new Picture(R.drawable.picture_02));
        images.add(new Picture(R.drawable.picture_03));
        images.add(new Picture(R.drawable.picture_04));
        cofList.clear();
        for(int i=0;i<10;i++){
            Random random = new Random();
            int index =  random.nextInt(cofs.length);
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

    private void initPictures(){
        imagesList.clear();
        for(int i=0;i<4;i++){
            Random random = new Random();
            int index = random.nextInt(pictures.length);
            imagesList.add(pictures[index]);
        }
    }
}
