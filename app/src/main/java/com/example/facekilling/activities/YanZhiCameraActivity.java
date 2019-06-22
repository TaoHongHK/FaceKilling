package com.example.facekilling.activities;


import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.facekilling.R;
import com.example.facekilling.util.FaceKCamera;


public class YanZhiCameraActivity extends Activity {

    private SurfaceView mSurfaceView;
    private ImageView iv_show;
    private Button takePicButt;
    private Button flipCamera;
    private boolean isCameraing;
    private FaceKCamera faceKCamera;


    public YanZhiCameraActivity() {
        // Required empty public constructor
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_yanzhi_camera);
        initView();
    }


    private void initView() {
        iv_show = (ImageView) findViewById(R.id.yz_iv_show_camera);
        takePicButt = (Button) findViewById(R.id.yz_take_pic_butt);
        flipCamera = (Button) findViewById(R.id.yz_flip_camera);
        mSurfaceView = (SurfaceView) findViewById(R.id.yz_surface_view_camera);
        faceKCamera = new FaceKCamera(mSurfaceView,this);
        isCameraing = true;
        takePicButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceKCamera.takePicture();
                faceKCamera.showImg(iv_show);
                changeShowingViews(isCameraing);
            }
        });
        flipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceKCamera.changeCamera();
            }
        });
    }




    @Override
    public void onBackPressed() {
        if (iv_show.getVisibility()==View.GONE){
            super.onBackPressed();
        }else {
            changeShowingViews(isCameraing);
            faceKCamera.startProView();
        }
    }


    public void changeShowingViews(boolean cameraing){
        if (cameraing){
            mSurfaceView.setVisibility(View.GONE);
            flipCamera.setVisibility(View.GONE);
            takePicButt.setVisibility(View.GONE);
            iv_show.setVisibility(View.VISIBLE);
        }else{
            mSurfaceView.setVisibility(View.VISIBLE);
            flipCamera.setVisibility(View.VISIBLE);
            takePicButt.setVisibility(View.VISIBLE);
            iv_show.setVisibility(View.GONE);
        }
        isCameraing = !isCameraing;
    }
}