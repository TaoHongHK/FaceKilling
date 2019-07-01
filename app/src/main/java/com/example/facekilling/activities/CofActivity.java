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

import static com.example.facekilling.util.GetBitmap.deleteFilePackage;
import static com.example.facekilling.util.GetBitmap.getAllPicFromUser;
import static com.example.facekilling.util.GetBitmap.getBitmapFromSD;
import static com.example.facekilling.util.GetBitmap.getSuoLueBitmapFromSD;
import static com.example.facekilling.util.OkHttpUtils.postSendCofImgList;
import static com.example.facekilling.util.OkHttpUtils.postSendCofMessage;
import static com.example.facekilling.util.StaticConstant.TACK_DEFAULT;
import static com.example.facekilling.util.StaticConstant.TACK_PICTURE_TO_COF;
import static com.example.facekilling.util.StaticConstant.TMP_IMG_FILE;

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
    private String content;


    private List<Picture> picturesList = new ArrayList<>();
    PictureAdapater adapter = new PictureAdapater(picturesList);


    private Context getContent(){
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cof);
        mContext = getApplicationContext();


        initPictures();
        initView();
        //各种监控事件
        monitor();

    }

    public void initView(){
        topBar = (TopBar) findViewById(R.id.cofActivityTopBar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        head_img = (ImageView) findViewById(R.id.cof_activity_head_img);
        head_img.setImageBitmap(MainUser.getInstance().getImageBitMap());

        head_name = (TextView) findViewById(R.id.cof_activity_head_name);
        head_name.setText(MainUser.getInstance().getUser_name());
        head_photo = (ImageView) findViewById(R.id.cof_activity_head_photo);
        head_photo.setImageResource(R.drawable.cof_photo);
        editText = (EditText) findViewById(R.id.cof_activity_head_content);


        recyclerView = (RecyclerView) findViewById(R.id.cof_activity_imgs);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        setAdaptiveHeight();

        recyclerView.setAdapter(adapter);
    }
    //设置高度自适应
    private void setAdaptiveHeight(){
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
                deleteFilePackage(TMP_IMG_FILE);
                finish();
            }

            @Override
            //发表朋友圈
            public void rightClicked() {
                String content = editText.getText().toString().trim();
                if(!content.equals("") || picturesList.size() != 0){
                    if(content.equals(""))
                        content = " ";
                    int cof_id = -1;
                    try {
                        cof_id= postSendCofMessage(MainUser.getInstance().getUser_id(),content);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(picturesList.size() != 0){
                        postSendCofImgList(cof_id,MainUser.getInstance().getUser_id(),picturesList);
                    }
                    Intent intent = new Intent(CofActivity.this, IndexActivity.class);
                    intent.putExtra("id",3);
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
                                Intent intent = new Intent(getApplicationContext(),FaceKCamera.class);
                                startActivityForResult(intent,StaticConstant.GETBITMAP_FROM_CAMERA);
                                break;
                            case 1:
                                //从应用图库中寻找
                                Intent intent_1 = new Intent(getApplicationContext(),LookForImages.class);
                                startActivityForResult(intent_1,StaticConstant.GETBITMAP_FROM_APP);
                                break;
                            case 2:
                                //从本地图库寻找
                                Intent intent_2 = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent_2, StaticConstant.GETBITMAP_FROM_SD);
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
        picturesList.clear();
        Intent intent = getIntent();
        int tackid = intent.getIntExtra("tack",TACK_DEFAULT);
        List<String> picPathList = (List<String>) intent.getStringArrayListExtra("picPathList");
        if(tackid == TACK_PICTURE_TO_COF){
            for(int i=0;i<picPathList.size();i++){
                Bitmap bitmap = getBitmapFromSD(picPathList.get(i));
                picturesList.add(new Picture(bitmap,picPathList.get(i)));
            }
        }
    }

    //响应添加图片按钮
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case StaticConstant.GETBITMAP_FROM_CAMERA:
                String bitmapPath = data.getStringExtra(StaticConstant.BITMAP_PATH);
                if (bitmapPath!=null){
                    picturesList.add(new Picture(getSuoLueBitmapFromSD(bitmapPath),bitmapPath));
                }
                setAdaptiveHeight();
                adapter.notifyDataSetChanged();
                break;
            case StaticConstant.GETBITMAP_FROM_APP:
                List<String> picPathList = data.getStringArrayListExtra("picPathList");
                for(int i=0;i<picPathList.size();i++){
                    Bitmap bitmap = getSuoLueBitmapFromSD(picPathList.get(i));
                    picturesList.add(new Picture(bitmap,picPathList.get(i)));
                }
                setAdaptiveHeight();
                adapter.notifyDataSetChanged();
                break;
            case StaticConstant.GETBITMAP_FROM_SD:
                if (resultCode==RESULT_OK && data!=null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String imagePath = c.getString(columnIndex);
                    picturesList.add(new Picture(getSuoLueBitmapFromSD(imagePath),imagePath));
                    c.close();
                    setAdaptiveHeight();
                    adapter.notifyDataSetChanged();
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
