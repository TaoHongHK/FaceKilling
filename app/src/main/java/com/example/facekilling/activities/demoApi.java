package com.example.facekilling.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facekilling.R;
import com.example.facekilling.util.JsonUtil;
import com.example.facekilling.util.OkHttpUtils;


public class demoApi extends Activity {

    private TextView textView;
    private ImageView imageView;
    private Button button;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initView();

    }

    public void initView() {
        textView = (TextView) findViewById(R.id.activity_demo_text);
        imageView = (ImageView) findViewById(R.id.activity_demo_image);
        button = (Button) findViewById(R.id.activity_demo_butt);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET}, 0);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //读取图片测试demo
                /*try {
                    final Bitmap bitmap = JsonUtil.decodeImgFromString(
                            JsonUtil.getImgStringFromJson(JsonUtil.readFileFromRaw(
                                    getApplicationContext(),R.raw.test)));
                    imageView.setImageBitmap(bitmap);
                }catch (Exception e){
                    e.printStackTrace();
                }*/

                //get接口测试demo
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
                        String s = okHttpUtils.getSometing();
                        Log.d("mmmmmm", "run: "+s);
                    }
                }).start();

                //post接口测试demo
            }
        });
    }



}
