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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.facekilling.R;
import com.example.facekilling.javabean.Expression;
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


public class BiaoQingBaoCameraActivity extends Activity {

    private Button oneMoreButt;
    private ImageView imageView;
    private static final int BIAOQINGBAO = 1;
    private BiaoQingBaoCallBackHandle biaoQingBaoCallBackHandle;
    private ImageView reviewImg;
    private TextView reviewContent;
    private int biaoQingIndex = 0;
    private GifImageView gifImageView;
    private GifDrawable gifDrawable;
    private LinearLayout linearLayout;
    private final String[] review = {"你太棒了","还要继续努力哦"};
    private final String[] biaoQingText = {"amazing", "happy", "angry", "nature"};
    private final int[] biaoQingImg = {R.drawable.emoji_surprise,R.drawable.emoji_happy,R.drawable.emoji_angry,R.drawable.emoji_nature};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_biaoqingbao_camera);
        Intent intent = new Intent(getApplicationContext(),FaceKCamera.class);
        intent.putExtra(StaticConstant.FUNCTIONBUTT_REQUEST,true);
        startActivityForResult(intent, StaticConstant.CAMERA_RETURN);
        biaoQingBaoCallBackHandle = new BiaoQingBaoCallBackHandle(this);
    }

    public void initView(){
        oneMoreButt = (Button) findViewById(R.id.biaoqingbao_oneMoreButt);
        imageView = (ImageView) findViewById(R.id.biaoqingbao_img);
        reviewImg = (ImageView) findViewById(R.id.reviewImg);
        reviewContent = (TextView) findViewById(R.id.reviewContent);
        gifImageView = (GifImageView) findViewById(R.id.biaoqingbao_gif);
        linearLayout = (LinearLayout) findViewById(R.id.biaoqingbao_info_part);
        oneMoreButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FaceKCamera.class);
                intent.putExtra(StaticConstant.FUNCTIONBUTT_REQUEST,true);
                startActivityForResult(intent, StaticConstant.CAMERA_RETURN);

            }
        });
        gifImageView.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        gifDrawable = (GifDrawable) gifImageView.getDrawable();
        gifDrawable.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==StaticConstant.CAMERA_RETURN && data!=null){
            biaoQingIndex = data.getIntExtra("biaoQingIndex",0);
            final String bitmapPath = data.getStringExtra(StaticConstant.BITMAP_PATH);
            if (bitmapPath!=null && !bitmapPath.equals("")) {
                initView();
                imageView.setImageBitmap(GetBitmap.getBitmapFromSD(bitmapPath));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                             List<Expression> expressions = OkHttpUtils.postFaceBiaoQingImage(MainUser.getInstance(),bitmapPath);
                            Expression oneExpression= null;
                            if(!expressions.isEmpty()){
                                oneExpression = expressions.get(0);
                                Log.d("OKHTTP", "run: "+oneExpression.getExpresiion());
                            }
                            biaoQingBaoCallBackHandle.obtainMessage(BIAOQINGBAO,oneExpression).sendToTarget();
                        }catch (IOException ie){
                            ie.printStackTrace();
                        }catch (JSONException je){
                            je.printStackTrace();
                        }
                    }
                }).start();
            }
            else BiaoQingBaoCameraActivity.this.finish();
        } else BiaoQingBaoCameraActivity.this.finish();
    }

    public void setExpressionText(final Expression expression){
        final int index = biaoQingIndex;
        if(expression!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reviewImg.setImageResource(biaoQingImg[index]);
                    if(biaoQingText[index].equals(expression.getExpresiion())){
                        reviewContent.setText(review[0]);
                    }
                    else{
                        reviewContent.setText(review[1]);
                    }
                }
            });
        }
        else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    reviewImg.setImageResource(biaoQingImg[index]);
                    reviewContent.setText(review[1]);
                }
            });
        }
        gifDrawable.stop();
        linearLayout.setVisibility(View.VISIBLE);
        gifImageView.setVisibility(View.GONE);
    }

    private class BiaoQingBaoCallBackHandle extends Handler {
        private BiaoQingBaoCameraActivity biaoQingBaoCallBackHandle;

        public BiaoQingBaoCallBackHandle(BiaoQingBaoCameraActivity biaoQingBaoCallBackHandle) {
            this.biaoQingBaoCallBackHandle = biaoQingBaoCallBackHandle;
        }

        @Override
        public void handleMessage(Message msg) {
           Expression expression;
            if (msg.what == BIAOQINGBAO) {
                expression = (Expression) msg.obj;
                biaoQingBaoCallBackHandle.setExpressionText(expression);
            }
        }
    }
}