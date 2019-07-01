package com.example.facekilling.activities;


import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.util.GetBitmap;
import com.example.facekilling.util.GetSysTime;
import com.example.facekilling.util.StaticConstant;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.oguzdev.circularfloatingactionmenu.library.SubActionButton.THEME_DARKER;

public class FaceKCamera extends AppCompatActivity {

    private static final int CAMERA_WHAT = 1;
    private SavePicCallBackHandle savePicCallBackHandle;

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    //private static final int viewWidth = 640, viewHeight = 480;
    private int viewWidth,viewHeight;
    private int frontCameraId;
    private int backCameraId;
    private int numberOfCameras;
    private int usingCamera;
    private Button takePicButt;
    private Button flipCamera;
    private boolean isCameraing;
    private ImageView iv_show;
    private CardView cardView;
    private Button reTakeButt;
    private Button chosenButt;
    private String mBitMapPath;
    private int biaoQingIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_kcamera);
        if(getIntent().getBooleanExtra(StaticConstant.FUNCTIONBUTT_REQUEST,false)){
            SetFunctionButton();
        }
        savePicCallBackHandle = new SavePicCallBackHandle(this);
        isCameraing = true;
        initView();
    }

    private void initView() {
        takePicButt = (Button) findViewById(R.id.camera_take_pic_butt);
        flipCamera = (Button) findViewById(R.id.camera_flip_camera);
        reTakeButt = (Button) findViewById(R.id.camera_reTakeButt);
        chosenButt = (Button) findViewById(R.id.camera_chosenButt);
        iv_show = (ImageView) findViewById(R.id.camera_iv_show_camera);
        cardView = (CardView) findViewById(R.id.camera_iv_cardview);
        initCameraInfo();
        GetBitmap.askForStorePermission(getActivity(),getApplicationContext());
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_surface_view_camera);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) { //SurfaceView创建
                // 初始化Camera
                initFrontCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { //SurfaceView销毁
            }
        });
        //设置点击监听
        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera == null) return;
                //自动对焦后拍照
                //mCamera.autoFocus(autoFocusCallback);
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera acamera) {
                        Toast toast = Toast.makeText(getApplicationContext(),"聚焦",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,0);
                        toast.show();
                    }
                });
            }
        });
        takePicButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        flipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCamera();
            }
        });

        reTakeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeShowingViews();
            }
        });

        chosenButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /*intent.putExtra("imgBitmap", BitMap2Util.BitMap2ByteArray(mBitMap));*/
                intent.putExtra(StaticConstant.BITMAP_PATH,mBitMapPath);
                intent.putExtra("biaoQingIndex",biaoQingIndex);
                FaceKCamera.this.setResult(StaticConstant.CAMERA_RETURN,intent);
                FaceKCamera.this.finish();
            }
        });
    }

    public void initCameraInfo(){
        numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; ++i) {
            final Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);
            //后置摄像头
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                backCameraId = i;
            }
            //前置摄像头
            else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontCameraId = i;
            }
        }
    }


    private void initFrontCamera() {
        mCamera = Camera.open(frontCameraId);//默认开启后置
        mCamera.setDisplayOrientation(90);//摄像头进行旋转90°
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                //设置预览照片的大小
                parameters.setPreviewSize(viewWidth, viewHeight);
                //设置相机预览照片帧数
                parameters.setPreviewFpsRange(4, 10);
                //设置图片格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                //设置图片的质量
                parameters.set("jpeg-quality", 85);
                //设置照片的大小
                parameters.setPictureSize(viewWidth, viewHeight);

                //通过SurfaceView显示预览
                mCamera.setPreviewDisplay(mSurfaceHolder);
                //开始预览
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        usingCamera = frontCameraId;
    }

    private void initBackCamera() {
        mCamera = Camera.open(backCameraId);//默认开启后置
        mCamera.setDisplayOrientation(90);//摄像头进行旋转90°
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                //设置预览照片的大小
                parameters.setPreviewFpsRange(viewWidth, viewHeight);
                //设置相机预览照片帧数
                parameters.setPreviewFpsRange(4, 10);
                //设置图片格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                //设置图片的质量
                parameters.set("jpeg-quality", 85);
                //设置照片的大小
                parameters.setPictureSize(viewWidth, viewHeight);
                //通过SurfaceView显示预览
                mCamera.setPreviewDisplay(mSurfaceHolder);
                //开始预览
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        usingCamera = backCameraId;
    }

    public void stopCamera(){
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void changeCamera(){
        if (usingCamera==frontCameraId){
            stopCamera();
            initBackCamera();
        }else {
            stopCamera();
            initFrontCamera();
        }
    }

    public void takePicture(){
        if (mCamera==null){
            return;
        }
        else {
            mCamera.takePicture(null,null, pictureCallback);
        }
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            final Bitmap resource = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (resource == null) {
                Toast toast = Toast.makeText(getApplicationContext(),"拍照失败",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();
            }
            final Matrix matrix = new Matrix();
            matrix.setRotate(-90);
            final Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0,
                    resource.getWidth(), resource.getHeight(), matrix, true);
            if (bitmap != null) {
                saveTempPic(bitmap);
                savePic(bitmap);
            }
        }
    };

    public void saveTempPic(final Bitmap bitmap){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }
                String baseFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "FaceK"+File.separator+ "CompressedPics";
                File baseFile = new File(baseFilePath);
                if (!baseFile.exists()) {
                    baseFile.mkdirs();
                }
                String pickName = GetSysTime.getCurrTime() +".jpg";
                File picFile = new File(baseFile,pickName);
                GetBitmap.transImage(bitmap,picFile.getAbsolutePath(),480,640,50);
                savePicCallBackHandle.obtainMessage(CAMERA_WHAT,picFile.getAbsolutePath()).sendToTarget();
            }
        }).start();
    }

    public void savePic(final Bitmap bitmap){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }
                FileOutputStream fileOutputStream = null;
                String baseFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "FaceK"+File.separator+ MainUser.getInstance().getUser_id();
                File baseFile = new File(baseFilePath);
                if (!baseFile.exists()) {
                    baseFile.mkdirs();
                }
                String pickName = GetSysTime.getCurrTime() +".jpg";
                try {
                    File picFile = new File(baseFile,pickName);
                    fileOutputStream = new FileOutputStream(picFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    MediaStore.Images.Media.insertImage(getContentResolver(), picFile.getPath(), pickName, "description");
                    Uri uri = Uri.fromFile(picFile);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (NullPointerException ne){
                        ne.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public Activity getActivity(){
        return this;
    }

    @Override
    public void onBackPressed() {
        if (mSurfaceView.getVisibility()==View.VISIBLE){
            stopCamera();
            super.onBackPressed();
        }else {
            changeShowingViews();
        }
    }

    public void changeShowingViews(){
        if (isCameraing){
            stopCamera();
            mSurfaceView.setVisibility(View.GONE);
            flipCamera.setVisibility(View.GONE);
            takePicButt.setVisibility(View.GONE);
            reTakeButt.setVisibility(View.VISIBLE);
            chosenButt.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
        }else{
            mSurfaceView.setVisibility(View.VISIBLE);
            flipCamera.setVisibility(View.VISIBLE);
            takePicButt.setVisibility(View.VISIBLE);
            reTakeButt.setVisibility(View.GONE);
            chosenButt.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
        }
        isCameraing = !isCameraing;
    }

    public void setBitmapPath(String bitmapPath){
        mBitMapPath = bitmapPath;
    }



    private class SavePicCallBackHandle extends Handler{
        private FaceKCamera faceKCamera;
        private String bitmapPath;

        public SavePicCallBackHandle(FaceKCamera faceKCamera){
            this.faceKCamera = faceKCamera;
        }

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==CAMERA_WHAT){
                bitmapPath = (String) msg.obj;
                if (bitmapPath!=null){
                    faceKCamera.setBitmapPath(bitmapPath);
                    changeShowingViews();
                    iv_show.setImageBitmap(GetBitmap.getBitmapFromSD(bitmapPath));
                }
            }
        }
    }


    public void SetFunctionButton() {
        final ImageView fabIconNew = new ImageView(this);
        // 设置菜单按钮Button的图标
        fabIconNew.setImageResource(R.drawable.settings_menu);
        final FloatingActionButton rightLowerButton = new FloatingActionButton
                .Builder(FaceKCamera.this).setContentView(fabIconNew).build();
        //设置悬浮按钮的参数
        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        rLSubBuilder.setTheme(THEME_DARKER);
        FloatingActionButton.LayoutParams pParams = new FloatingActionButton.LayoutParams(
                150, 150);
        pParams.setMargins(30,
                30, 30,
                30);

        rLSubBuilder.setLayoutParams(pParams);
        rightLowerButton.setPosition(2,pParams);

        ImageView rlIcon1 = new ImageView(this);
        ImageView rlIcon2 = new ImageView(this);
        ImageView rlIcon3 = new ImageView(this);
        ImageView rlIcon4 = new ImageView(this);
        // 设置弹出菜单的图标

        rlIcon1.setImageResource(R.drawable.emoji_nature);
        rlIcon2.setImageResource(R.drawable.emoji_angry);
        rlIcon3.setImageResource(R.drawable.emoji_happy);
        rlIcon4.setImageResource(R.drawable.emoji_surprise);

//        // 设置菜单按钮Button的宽、高，边距
        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(
                100, 100);
        starParams.setMargins(25,
                25,0,0);


        final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(
                this)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1,starParams).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2,starParams).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3,starParams).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon4,starParams).build())
                .setStartAngle(90).setEndAngle(180)
                .attachTo(rightLowerButton).build();

        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {

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

        rlIcon1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                biaoQingIndex = 3;
                rightLowerMenu.close(true);
            }
        });
        rlIcon2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                biaoQingIndex = 2;
                rightLowerMenu.close(true);
            }
        });

        rlIcon3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                biaoQingIndex = 1;
                rightLowerMenu.close(true);
            }
        });

        rlIcon4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                biaoQingIndex = 0;
                rightLowerMenu.close(true);
            }
        });
    }
}
