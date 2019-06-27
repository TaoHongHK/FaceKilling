package com.example.facekilling.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.facekilling.R;
import com.example.facekilling.util.GetBitmap;
import com.example.facekilling.util.StaticConstant;



public class YanZhiCameraActivity extends Activity {

    private ImageView imageView;
    private Button oneMoreButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_yanzhi_camera);
        Intent intent = new Intent(getApplicationContext(),FaceKCamera.class);
        startActivityForResult(intent, StaticConstant.CAMERA_RETURN);
        Log.d("yanzhi", "onCreate: ");
    }

    public void initView(){
        imageView = (ImageView) findViewById(R.id.yanzhi_img);
        oneMoreButt = (Button) findViewById(R.id.yanzhi_oneMoreButt);
        oneMoreButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FaceKCamera.class);
                startActivityForResult(intent, StaticConstant.CAMERA_RETURN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == StaticConstant.CAMERA_RETURN && data!=null){
            String bitmapPath = data.getStringExtra(StaticConstant.BITMAP_PATH);
            if (bitmapPath!=null && !bitmapPath.equals("")) {
                initView();
                imageView.setImageBitmap(GetBitmap.getBitmapFromSD(bitmapPath));
            }
            else YanZhiCameraActivity.this.finish();
        }else YanZhiCameraActivity.this.finish();
    }
}