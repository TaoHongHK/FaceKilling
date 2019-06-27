package com.example.facekilling.activities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.facekilling.R;
import com.example.facekilling.util.GetBitmap;
import com.example.facekilling.util.StaticConstant;


public class BiaoQingBaoCameraActivity extends Activity {

    private Button oneMoreButt;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_biaoqingbao_camera);
        Intent intent = new Intent(getApplicationContext(),FaceKCamera.class);
        intent.putExtra(StaticConstant.FUNCTIONBUTT_REQUEST,true);
        startActivityForResult(intent, StaticConstant.CAMERA_RETURN);
    }

    public void initView(){
        oneMoreButt = (Button) findViewById(R.id.biaoqingbao_oneMoreButt);
        imageView = (ImageView) findViewById(R.id.biaoqingbao_img);
        oneMoreButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FaceKCamera.class);
                intent.putExtra(StaticConstant.FUNCTIONBUTT_REQUEST,true);
                startActivityForResult(intent, StaticConstant.CAMERA_RETURN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==StaticConstant.CAMERA_RETURN && data!=null){
            String bitmapPath = data.getStringExtra(StaticConstant.BITMAP_PATH);
            if (bitmapPath!=null && !bitmapPath.equals("")) {
                initView();
                imageView.setImageBitmap(GetBitmap.getBitmapFromSD(bitmapPath));
            }
            else BiaoQingBaoCameraActivity.this.finish();
        } else BiaoQingBaoCameraActivity.this.finish();
    }
}