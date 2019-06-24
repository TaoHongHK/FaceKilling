package com.example.facekilling.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.adapter.PictureAdapater;
import com.example.facekilling.customviews.TopBar;
import com.example.facekilling.fragments.Index_ThreeFragment;
import com.example.facekilling.javabean.Cof;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.javabean.Picture;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CofActivity extends AppCompatActivity {


    private View mView;
    private TopBar topBar;
    private DrawerLayout drawerLayout;
    private Context mContext;

    private ImageView head_img;
    private TextView head_name;
    private ImageView head_photo;
    private EditText editText;


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
        picturesList.clear();


        //接受数据
        Intent intent = getIntent();
        List<Picture> listfromPicture = (List<Picture>) intent.getSerializableExtra("PictureList");
        if(listfromPicture.size() != 0){
            picturesList.addAll(listfromPicture);
        }


        topBar = (TopBar) findViewById(R.id.cofActivityTopBar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        head_img = (ImageView) findViewById(R.id.cof_activity_head_img);
        head_img.setImageResource(MainUser.getInstance().getImageId());
        head_name = (TextView) findViewById(R.id.cof_activity_head_name);
        head_name.setText(MainUser.getInstance().getUser_name());
        head_photo = (ImageView) findViewById(R.id.cof_activity_head_photo);
        head_photo.setImageResource(R.drawable.cof_photo);
        editText = (EditText) findViewById(R.id.cof_activity_head_content);

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
                String content = editText.getText().toString().trim();
                if(!content.equals("") || picturesList.size() != 0){
                    Cof cof = new Cof(MainUser.getInstance(),content,picturesList);
                    Intent intent = new Intent(CofActivity.this, IndexActivity.class);
                    intent.putExtra("id",3);
                    intent.putExtra("cof",cof);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContent(),"请写下你的心情日志 或 添加图片",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //监听添加图片按钮
        head_photo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(CofActivity.this);
                //    指定下拉列表的显示数据
                final String[] cities = {"拍摄照片", "从应用图库中寻找", "从本地图库寻找"};
                //    设置一个下拉的列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch(which){
                            case 0:
                                //拍摄照片
                                Toast.makeText(getContent(),"拍摄照片",Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                //从应用图库中寻找
                                Toast.makeText(getContent(),"从应用图库中寻找",Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                //从本地图库寻找
                                Toast.makeText(getContent(),"从本地图库中寻找",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();

            }
        });
    }


    private void initPictures(){
        for(int i=0;i<0;i++){
            Random random = new Random();
            int index = random.nextInt(pictures.length);
            picturesList.add(pictures[index]);
        }
    }

}
