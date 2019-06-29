package com.example.facekilling.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facekilling.R;
import com.example.facekilling.javabean.Face;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.util.GetBitmap;
import com.example.facekilling.util.OkHttpUtils;
import com.example.facekilling.util.StaticConstant;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;


public class YanZhiCameraActivity extends Activity {

    private ImageView imageView;
    private Button oneMoreButt;
    private static final int YANZHIWHAT = 0;
    private YanZhiCallBackHandle yanZhiCallBackHandle;

    private TextView faceGlasses;
    private TextView faceMoustache;
    private TextView faceHat;
    private TextView faceMakeup;
    private TextView faceGender;
    private TextView faceHairstyle;
    private TextView faceFat;
    private TextView faceScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_yanzhi_camera);
        Intent intent = new Intent(getApplicationContext(),FaceKCamera.class);
        startActivityForResult(intent, StaticConstant.CAMERA_RETURN);
        yanZhiCallBackHandle = new YanZhiCallBackHandle(this);
        Log.d("yanzhi", "onCreate: ");
    }

    public void initView(){
        imageView = (ImageView) findViewById(R.id.yanzhi_img);
        oneMoreButt = (Button) findViewById(R.id.yanzhi_oneMoreButt);
        faceGlasses = (TextView) findViewById(R.id.face_glasses_text);
        faceHat = (TextView) findViewById(R.id.face_hat_text);
        faceFat = (TextView) findViewById(R.id.face_fat_text);
        faceGender = (TextView) findViewById(R.id.face_gender_text);
        faceMakeup = (TextView) findViewById(R.id.face_makeup_text);
        faceScore = (TextView) findViewById(R.id.face_score_text);
        faceHairstyle = (TextView) findViewById(R.id.face_hairstyle_text);
        faceMoustache = (TextView) findViewById(R.id.face_moustache_text);
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
            final String bitmapPath = data.getStringExtra(StaticConstant.BITMAP_PATH);
            if (bitmapPath!=null && !bitmapPath.equals("")) {
                initView();
                imageView.setImageBitmap(GetBitmap.getBitmapFromSD(bitmapPath));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<Face> faces = OkHttpUtils.postFaceYanZhiImage(MainUser.getInstance(),bitmapPath);
                            Face oneFace = null;
                            if(!faces.isEmpty()){
                                oneFace = faces.get(0);
                            }
                            yanZhiCallBackHandle.obtainMessage(YANZHIWHAT,oneFace).sendToTarget();
                        }catch (IOException ie){
                            ie.printStackTrace();
                        }catch (JSONException je){
                            je.printStackTrace();
                        }
                    }
                }).start();
            }
            else YanZhiCameraActivity.this.finish();
        }else YanZhiCameraActivity.this.finish();
    }

    public void setFaceText(final Face face){
        if(face!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    faceGlasses.setText(face.getEyeglasses());
                    faceFat.setText(face.getChubby());
                    faceGender.setText(face.getSex());
                    faceHairstyle.setText(face.getHair_shape());
                    faceMakeup.setText(face.getMakeup());
                    faceScore.setText(face.getAttra_score());
                    faceMoustache.setText(face.getBeard());
                    faceHat.setText(face.getHat());
                }
            });
        }
    }

    private class YanZhiCallBackHandle extends Handler {
        private YanZhiCameraActivity yanZhiCameraActivity;

        public YanZhiCallBackHandle(YanZhiCameraActivity yanZhiCameraActivity) {
            this.yanZhiCameraActivity = yanZhiCameraActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            Face face;
            if (msg.what == YANZHIWHAT) {
                face = (Face) msg.obj;
                yanZhiCameraActivity.setFaceText(face);
            }
        }
    }
}