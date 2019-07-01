package com.example.facekilling.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.facekilling.R;
import com.example.facekilling.javabean.Face;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.util.GetBitmap;
import com.example.facekilling.util.OkHttpUtils;
import com.example.facekilling.util.StaticConstant;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class YanZhiCameraActivity extends Activity {

    private ImageView imageView;
    private Button oneMoreButt;
    private GifImageView wait_gif;
    private GifDrawable gifDrawable;
    private LinearLayout linearLayout;

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

    private static final String EYE_GLASSES = "eyeglasses";
    private static final String EYE_RESULT_TURE = "戴眼镜";
    private static final String EYE_RESULT_FALSE = "无眼镜";

    private static final String BEARD = "bread";
    private static final String BEARD_RESULT_TURE = "有胡子";
    private static final String BEARD_RESULT_FALSE = "无胡子";

    private static final String CHUBBY = "chubby";
    private static final String CHUBBY_RESULT_TURE = "圆润";
    private static final String CHUBBY_RESULT_FALSE = "苗条";

    private static final String HAIR_SHAPE = "straight_hair";
    private static final String HAIR_SHAPE_RESULT_TURE = "直发";
    private static final String HAIR_SHAPE_RESULT_FALSE = "卷发";

    private static final String HAT = "hat";
    private static final String HAT_RESULT_TURE = "戴帽子";
    private static final String HAT_RESULT_FALSE = "无帽子";

    private static final String MAKE_UP = "makeup";
    private static final String MAKE_UP_RESULT_TURE = "化妆";
    private static final String MAKE_UP_RESULT_FALSE = "素颜";

    private static final String SEX = "male";
    private static final String SEX_RESULT_TURE = "男性";
    private static final String SEX_RESULT_FALSE = "女性";


    private static final String UNKNOWN = "未知";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_yanzhi_camera);
        Intent intent = new Intent(getApplicationContext(),FaceKCamera.class);
        startActivityForResult(intent, StaticConstant.CAMERA_RETURN);
        yanZhiCallBackHandle = new YanZhiCallBackHandle(this);
    }

    public void initView(){
        linearLayout = (LinearLayout) findViewById(R.id.yanzhi_info_part);
        imageView = (ImageView) findViewById(R.id.yanzhi_img);
        wait_gif = (GifImageView) findViewById(R.id.yanzhi_gif);
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
        linearLayout.setVisibility(View.GONE);
        wait_gif.setVisibility(View.VISIBLE);
        gifDrawable = (GifDrawable) wait_gif.getDrawable();
        gifDrawable.start();
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
            gifDrawable.stop();
            wait_gif.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (face.getEyeglasses().equals(EYE_GLASSES))
                        faceGlasses.setText(EYE_RESULT_TURE);
                    else faceGlasses.setText(EYE_RESULT_FALSE);

                    if (face.getChubby().equals(CHUBBY))
                        faceFat.setText(CHUBBY_RESULT_TURE);
                    else faceFat.setText(CHUBBY_RESULT_FALSE);

                    if (face.getSex().equals(SEX))
                        faceGender.setText(SEX_RESULT_TURE);
                    else faceGender.setText(SEX_RESULT_FALSE);

                    if (face.getHair_shape().equals(HAIR_SHAPE))
                        faceHairstyle.setText(HAIR_SHAPE_RESULT_TURE);
                    else faceHairstyle.setText(HAIR_SHAPE_RESULT_FALSE);

                    if (face.getMakeup().equals(MAKE_UP))
                        faceMakeup.setText(MAKE_UP_RESULT_TURE);
                    else faceMakeup.setText(MAKE_UP_RESULT_FALSE);

                    if (face.getBeard().equals(BEARD))
                        faceMoustache.setText(BEARD_RESULT_TURE);
                    else faceMoustache.setText(BEARD_RESULT_FALSE);

                    if (face.getHat().equals(HAT))
                        faceHat.setText(HAT_RESULT_TURE);
                    else faceHat.setText(HAT_RESULT_FALSE);

                    faceScore.setText(face.getAttra_score());
                }
            });
        }
        else{
            gifDrawable.stop();
            wait_gif.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            Toast toast = Toast.makeText(getApplicationContext(),"没有检测到人脸，哈哈",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
            faceGlasses.setText(UNKNOWN);
            faceFat.setText(UNKNOWN);
            faceGender.setText(UNKNOWN);
            faceHairstyle.setText(UNKNOWN);
            faceMakeup.setText(UNKNOWN);
            faceScore.setText(UNKNOWN);
            faceMoustache.setText(UNKNOWN);
            faceHat.setText(UNKNOWN);
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