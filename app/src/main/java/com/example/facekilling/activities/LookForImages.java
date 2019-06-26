package com.example.facekilling.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
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
import com.example.facekilling.javabean.Picture;
import com.example.facekilling.util.BitMap2Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class LookForImages extends AppCompatActivity {

    private TopBar topBar;

    private boolean multiChoose = false;

    protected DrawerLayout drawerLayout;

    private DrawerLayout mDrawerLayout;

    private static Context context;



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

    private List<Picture> picturesList = new ArrayList<>();

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
        PictureAdapater adapter = new PictureAdapater(picturesList);
        adapter.setCheckBoxVisiable(true);
        recyclerView.setLayoutManager(layoutManger);
        recyclerView.setAdapter(adapter);

    }
    private void monitor(){
        topBar = (TopBar) findViewById(R.id.lookForPicturesTopBar);
        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            public void leftClicked() {
                finish();
            }

            @Override
            public void rightClicked() {
                List<Picture> mPictureList = new ArrayList<>();
                for(Picture p:picturesList){
                    if(p.isChecked()){
                        mPictureList.add((Picture)p.clone());
                    }
                }
                //如果选择了图片
                if(mPictureList.size() != 0){
                    Intent intent = new Intent();
                    intent.putExtra("LookForPictureList",(Serializable)mPictureList);
                    setResult(1,intent);
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
        for(int i=0;i<50;i++){
            Random random = new Random();
            int index = random.nextInt(pictures.length);
            picturesList.add((Picture)pictures[index].clone());
        }
    }

    public void refresh() {
        onCreate(null);
    }
}
