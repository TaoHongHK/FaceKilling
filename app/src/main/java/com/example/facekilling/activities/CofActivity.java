package com.example.facekilling.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.example.facekilling.util.GetBitmap;
import com.example.facekilling.util.StaticConstant;


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

    private RecyclerView recyclerView;


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
        Intent intent = getIntent();
        List<Picture> mPictureList=  (List<Picture>)intent.getSerializableExtra("PictureList");
        if(mPictureList != null){
            picturesList.addAll(mPictureList);
        }

        initView();
        //各种监控事件
        monitor();

    }

    public void initView(){
        topBar = (TopBar) findViewById(R.id.cofActivityTopBar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        head_img = (ImageView) findViewById(R.id.cof_activity_head_img);
        if(MainUser.getInstance().getImageId() == -1){
            head_img.setImageResource(MainUser.getInstance().getImageId());
        }
        else{
            head_img.setImageBitmap(MainUser.getInstance().getImageBitMap());
        }
        head_img.setImageResource(MainUser.getInstance().getImageId());
        head_name = (TextView) findViewById(R.id.cof_activity_head_name);
        head_name.setText(MainUser.getInstance().getUser_name());
        head_photo = (ImageView) findViewById(R.id.cof_activity_head_photo);
        head_photo.setImageResource(R.drawable.cof_photo);
        editText = (EditText) findViewById(R.id.cof_activity_head_content);

        initPictures();
        recyclerView = (RecyclerView) findViewById(R.id.cof_activity_imgs);
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
    }
    private void monitor(){
        topBar = (TopBar) findViewById(R.id.cofActivityTopBar);
        ImageView head_photo = (ImageView) findViewById(R.id.cof_activity_head_photo);


        topBar.setClickListener(new TopBar.TopbarClickListener() {
            @Override
            //返回按钮
            public void leftClicked() {
                Intent intent = new Intent(CofActivity.this, IndexActivity.class);
                Cof cof = null;
                intent.putExtra("cof",cof);
                setResult(StaticConstant.GETCOF_FROM_NEW,intent);
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
                    setResult(StaticConstant.GETCOF_FROM_NEW,intent);
                    intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                                Intent intent = new Intent(getApplicationContext(),PostCofCameraActivity.class);
                                startActivityForResult(intent,0);
                                break;
                            case 1:
                                //从应用图库中寻找
                                Intent intent_1 = new Intent(getApplicationContext(),LookForImages.class);
                                startActivityForResult(intent_1,1);
                                break;
                            case 2:
                                //从本地图库寻找
                                Intent intent_2 = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent_2, 2);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case StaticConstant.GETBITMAP_FROM_CAMERA:
                /*byte[] bitmapByteArray = data.getByteArrayExtra("imgBitmap");
                if (bitmapByteArray!=null) {
                    picturesList.add(new Picture(
                            BitmapFactory.decodeByteArray(bitmapByteArray, 0, bitmapByteArray.length)));
                    adapter.notifyDataSetChanged();
                }*/
                String bitmapPath = data.getStringExtra("BitmapPath");
                if (bitmapPath!=null){
                    picturesList.add(new Picture(GetBitmap.getBitmapFromSD(bitmapPath)));
                }
                initView();
                break;
            case StaticConstant.GETBITMAP_FROM_APP:
                List<Picture> mPictureList=  (List<Picture>)data.getSerializableExtra("LookForPictureList");
                if(mPictureList != null){
                    adapter.addAllPicture(mPictureList);
                }
                initView();
                break;
            case StaticConstant.GETBITMAP_FROM_SD:
                if (resultCode==RESULT_OK && data!=null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String imagePath = c.getString(columnIndex);
                    picturesList.add(new Picture(GetBitmap.getBitmapFromSD(imagePath)));
                    c.close();
                    initView();
                }
                break;

        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            View v=getCurrentFocus();
            boolean  hideInputResult =isShouldHideInput(v,ev);
            if(hideInputResult){
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) CofActivity.this
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                if(v != null){
                    if(imm.isActive()){
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
