package com.example.facekilling.activities;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.customviews.TopBar;
import com.example.facekilling.javabean.Picture;
import com.example.facekilling.adapter.PictureAdapater;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu.MenuStateChangeListener;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class PicturesActivity extends AppCompatActivity  {

    private TopBar topBar;

    private boolean multiChoose = false;

    protected  DrawerLayout drawerLayout;

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

    private PictureAdapater adapter = new PictureAdapater(picturesList);

    public static Context getContext() {
        return context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pictures);
        context = getApplicationContext();
        initPictures();
        monitor();
        initView();
        rightLowerButton();
    }

    public void initView(){
        topBar = (TopBar) findViewById(R.id.menuPicturesTopBar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pictures_recycler_view);
        GridLayoutManager layoutManger = new GridLayoutManager(this,2);
        adapter.setLongStatus(true);
        recyclerView.setLayoutManager(layoutManger);
        recyclerView.setAdapter(adapter);


        registerForContextMenu(recyclerView);



    }
    private void monitor(){
        topBar = (TopBar) findViewById(R.id.menuPicturesTopBar);
        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            public void leftClicked() {
                Intent intent = new Intent(getContext(),IndexActivity.class);
                startActivity(intent);
            }

            @Override
            public void rightClicked() {
//                createPopMenu();
            }
        });
    }

//    public boolean createPopMenu() {
//        PopupMenu popupMenu = new PopupMenu(getContext(),topBar.getRightButt());
//        popupMenu.getMenuInflater().inflate(R.menu.picture_menu,popupMenu.getMenu());
//        popupMenu.show();
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                onPopItemSelected(item);
//                return false;
//            }
//        });
//        return true;
//    }



//    public void onPopItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.picture_choose:
//                //多选
//                if(!multiChoose){
//                    adapter.setCheckBoxVisiable(true);
//                    multiChoose = true;
//                }
//                else{
//                    adapter.setCheckBoxVisiable(false);
//                    multiChoose = false;
//                }
//                initView();
//                break;
//            case R.id.picture_delete:
//                //删除
//                Iterator<Picture> iter = picturesList.iterator();
//                while(iter.hasNext()){
//                    Picture it= iter.next();
//                    if(it.isChecked()){
//                        iter.remove();
//                    }
//                }
//                initView();
//                break;
//            case R.id.picture_export:
//                //分享
//                List<Picture> mPictureList = new ArrayList<>();
//                for(Picture p:picturesList){
//                    if(p.isChecked()){
//                        mPictureList.add((Picture)p.clone());
//                    }
//                }
//                //如果选择了图片
//                if(mPictureList.size() != 0){
//                    Intent intent = new Intent(PicturesActivity.this,CofActivity.class);
//                    intent.putExtra("PictureList",(Serializable)mPictureList);
//                    startActivity(intent);
//                }
//                else{
//                    Toast.makeText(getContext(), "请选择图片进行分享", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            default:
//                break;
//        }
//    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu,menu);
    }


    public boolean onContextItemSelected(MenuItem item) {
        int position = adapter.getLongPosition();
        switch (item.getItemId()) {
            case R.id.item_delete:
                picturesList.remove(position);
                initView();
                break;
            case R.id.item_export:
                List<Picture> mPictureList = new ArrayList<>();
                mPictureList.add((Picture)picturesList.get(position).clone());
                Intent intent = new Intent(PicturesActivity.this,CofActivity.class);
                intent.putExtra("PictureList",(Serializable)mPictureList);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
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

    // 右下角的菜单
    private void rightLowerButton() {
        final ImageView fabIconNew = new ImageView(this);
        // 设置菜单按钮Button的图标
        fabIconNew.setImageResource(R.drawable.settings_menu);
        final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(PicturesActivity.this).setContentView(fabIconNew).build();

        //设置悬浮按钮的参数
        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        FloatingActionButton.LayoutParams pParams = new FloatingActionButton.LayoutParams(
                200, 200);
        pParams.setMargins(30,
                30, 30,
                30);
        rLSubBuilder.setLayoutParams(pParams);

        ImageView rlIcon1 = new ImageView(this);
        ImageView rlIcon2 = new ImageView(this);
        ImageView rlIcon3 = new ImageView(this);
        // 设置弹出菜单的图标

        rlIcon1.setImageResource(R.drawable.multichoose);
        rlIcon2.setImageResource(R.drawable.delete);
        rlIcon3.setImageResource(R.drawable.share);

//        // 设置菜单按钮Button的宽、高，边距
        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(
                100, 100);
        starParams.setMargins(50,
                50,0,0);


        final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(
                this)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1,starParams).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2,starParams).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3,starParams).build())
                .attachTo(rightLowerButton).build();

        rightLowerMenu.setStateChangeListener(new MenuStateChangeListener() {

            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // 逆时针旋转90°
                fabIconNew.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(
                        View.ROTATION, -90);

                ObjectAnimator animation = ObjectAnimator
                        .ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // 顺时针旋转90°
                fabIconNew.setRotation(-90);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(
                        View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator
                        .ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();

            }
        });

        //各种响应
        //多选
        rlIcon1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //多选
                if(!multiChoose){
                    adapter.setCheckBoxVisiable(true);
                    multiChoose = true;
                }
                else{
                    adapter.setCheckBoxVisiable(false);
                    multiChoose = false;
                }
                initView();
            }
        });
        //删除
        rlIcon2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                rightLowerMenu.close(true);
                Iterator<Picture> iter = picturesList.iterator();
                while(iter.hasNext()){
                    Picture it= iter.next();
                    if(it.isChecked()){
                        iter.remove();
                    }
                }
                initView();
            }
        });
        //分享
        rlIcon3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                rightLowerMenu.close(true);
                List<Picture> mPictureList = new ArrayList<>();
                for(Picture p:picturesList){
                    if(p.isChecked()){
                        mPictureList.add((Picture)p.clone());
                    }
                }
                //如果选择了图片
                if(mPictureList.size() != 0){
                    Intent intent = new Intent(PicturesActivity.this,CofActivity.class);
                    intent.putExtra("PictureList",(Serializable)mPictureList);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(), "请选择图片进行分享", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
