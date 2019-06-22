package com.example.facekilling.activities;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.adapter.PictureAdapater;
import com.example.facekilling.customviews.TopBar;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.javabean.Picture;




import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CofActivity extends AppCompatActivity {


    private View mView;
    private TopBar topBar;
    private DrawerLayout drawerLayout;
    private Context mContext;


    private List<Picture> picturesList = new ArrayList<>();
    PictureAdapater adapter;

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

    private Context getContent(){
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cof);
        mContext = getApplicationContext();

        topBar = (TopBar) findViewById(R.id.cofActivityTopBar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        ImageView head_img = (ImageView) findViewById(R.id.cof_activity_head_img);
        head_img.setImageResource(MainUser.getInstance().getImageId());
        TextView head_name = (TextView) findViewById(R.id.cof_activity_head_name);
        head_name.setText(MainUser.getInstance().getUser_name());
        ImageView head_photo = (ImageView) findViewById(R.id.cof_activity_head_photo);
        head_photo.setImageResource(R.drawable.cof_photo);

        initPictures();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cof_activity_imgs);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

        //设置高度自适应
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PictureAdapater(picturesList);
        int num = adapter.getItemCount();
        int height;
        if (num==0){
            height=0;
        }
        else if(num > 9){
            height = 300 *3;
        }
        else{
            height = ((num-1)/3 + 1) * 300;
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.cof_activity_linearLayout) ;
        ViewGroup.LayoutParams lp=linearLayout.getLayoutParams();
        lp.height=height;
        linearLayout.setLayoutParams(lp);
        recyclerView.setAdapter(adapter);

        //各种监控事件
        monitor();

    }

    private void monitor(){
        topBar = (TopBar) findViewById(R.id.cofActivityTopBar);
        ImageView head_photo = (ImageView) findViewById(R.id.cof_activity_head_photo);

        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            //返回按钮
            public void leftClicked() {
                finish();
            }

            @Override
            //发表朋友圈
            public void rightClicked() {
                Toast.makeText(getContent(),"发表朋友圈",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //监听添加图片按钮
        head_photo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getContent(),"添加图片",Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void initPictures(){
        picturesList.clear();
        for(int i=0;i<3;i++){
            Random random = new Random();
            int index = random.nextInt(pictures.length);
            picturesList.add(pictures[index]);
        }
    }

}
