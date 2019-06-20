package com.example.facekilling.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.example.facekilling.customviews.TopBar;
import com.example.facekilling.util.Cof;
import com.example.facekilling.util.CofAdapter;
import com.example.facekilling.util.PictureAdapater;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Index_ThreeFragment extends Fragment {

    private View mView;
    private TopBar topBar;
    private DrawerLayout drawerLayout;

    //显示信息
    private List<Integer> images= new ArrayList<>();
    private Cof[] cofs = {new Cof(R.drawable.picture_01,"测试1","这是一个测试1",images),
            new Cof(R.drawable.picture_02,"测试2","这是一个测试2",images),
            new Cof(R.drawable.picture_03,"测试3","这是一个测试3",images),
            new Cof(R.drawable.picture_04,"测试4","这是一个测试4",images),
            new Cof(R.drawable.picture_05,"测试5","这是一个测试5",images),
            new Cof(R.drawable.picture_06,"测试6","这是一个测试6",images),
            new Cof(R.drawable.picture_07,"测试7","这是一个测试7",images),
    };
    private List<Cof> cofList = new ArrayList<>();
    private CofAdapter cofadapter;

    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;


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
        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            public void leftClicked() {
                Toast.makeText(getContext(),"FaceKilling",Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(Gravity.START);
            }

            @Override
            public void rightClicked() {
            }
        });

        ImageView imageview = mView.findViewById(R.id.cof_background);
        imageview.setImageResource(R.drawable.picture_01);
        //卡片式显示朋友圈
        initCofs();
        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.cof_recycler_view);
        GridLayoutManager layoutManger = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManger);
        cofadapter = new CofAdapter(cofList);
        recyclerView.setAdapter(cofadapter);

        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.cof_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCofs();
            }
        });


    }
    private void initCofs(){
        images.add(R.drawable.picture_01);
        images.add(R.drawable.picture_02);
        images.add(R.drawable.picture_03);
        images.add(R.drawable.picture_04);
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
                        initCofs();;
                        cofadapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

}
