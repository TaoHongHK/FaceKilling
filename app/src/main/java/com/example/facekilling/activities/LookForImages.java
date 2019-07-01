package com.example.facekilling.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.adapter.PictureAdapater;
import com.example.facekilling.customviews.TopBar;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.javabean.Picture;
import com.example.facekilling.util.BitMap2Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.example.facekilling.util.GetBitmap.getAllPicFromUser;
import static com.example.facekilling.util.StaticConstant.GETBITMAP_FROM_APP;
import static com.example.facekilling.util.StaticConstant.TMP_IMG_FILE;

public class LookForImages extends AppCompatActivity {


    private List<Picture> picturesList = new ArrayList<>();
    private boolean multiChoose = false;

    private TopBar topBar;
    private static Context context;
    PictureAdapater adapter = new PictureAdapater(picturesList);

    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;


    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_for_images);
        context = getApplicationContext();
        initPictures();
        monitor();
        initView();

    }
    public void initView(){
        topBar = (TopBar) findViewById(R.id.lookForPicturesTopBar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lookForPictures_recycler_view);
        GridLayoutManager layoutManger = new GridLayoutManager(this,2);
        adapter.setCheckBoxVisiable(true);
        recyclerView.setLayoutManager(layoutManger);
        recyclerView.setAdapter(adapter);

    }
    private void monitor(){
        topBar = (TopBar) findViewById(R.id.lookForPicturesTopBar);
        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            public void leftClicked() {
                List<String> picPathList = new ArrayList<>();
                Intent intent = new Intent();
                intent.putExtra("picPathList", (Serializable) picPathList);
                setResult(GETBITMAP_FROM_APP,intent);
                finish();
            }

            @Override
            public void rightClicked() {
                List<String> picPathList = new ArrayList<>();
                for(Picture p:picturesList){
                    if(p.isChecked()){
                        picPathList.add(p.getImagePath());
                    }
                }
                //如果选择了图片
                if(picPathList.size() != 0){
                    Intent intent = new Intent();
                    intent.putExtra("picPathList", (Serializable) picPathList);
                    setResult(GETBITMAP_FROM_APP,intent);
                    finish();
                }
                else{
                    Toast.makeText(getContext(), "请选择图片进行分享", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void initPictures(){
        picturesList.clear();
        picturesList.addAll(getAllPicFromUser(MainUser.getInstance().getUser_id()));
    }

    public void refresh() {
        onCreate(null);
        new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable(){
                    public void run(){
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
