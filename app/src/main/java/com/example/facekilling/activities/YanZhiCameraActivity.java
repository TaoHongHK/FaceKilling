package com.example.facekilling.activities;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facekilling.R;
import com.example.facekilling.util.GetSysTime;
import com.example.facekilling.util.OkHttpUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class YanZhiCameraActivity extends Activity {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private ImageView iv_show;
    private int viewWidth, viewHeight;
    private int frontCameraId;
    private int backCameraId;
    private int numberOfCameras;
    private int usingCamera;
    private Button takePicButt;
    private Button flipCamera;
    private Button upLoadButt;
    private String imgString;
    private TextView scoreTextView;
    private boolean isCameraing;
    private String scroeString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_yanzhi_camera);
        isCameraing = true;
        initView();
    }

    private void initView() {
        iv_show = (ImageView) findViewById(R.id.yz_iv_show_camera);
        takePicButt = (Button) findViewById(R.id.yz_take_pic_butt);
        flipCamera = (Button) findViewById(R.id.yz_flip_camera);
        upLoadButt = (Button) findViewById(R.id.yz_upload_butt);
        scoreTextView = (TextView) findViewById(R.id.yz_get_score);
        initCameraInfo();
        askForPermission();
        mSurfaceView = (SurfaceView) findViewById(R.id.yz_surface_view_camera);
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
                    public void onAutoFocus(boolean success, Camera camera) {
                        Toast.makeText(getApplicationContext(),"聚焦",Toast.LENGTH_SHORT).show();
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

        upLoadButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_show.getVisibility()==View.VISIBLE && imgString!=null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("yanzhiUpload", "onClick: nowIn");
                            scroeString = OkHttpUtils.YanZhiPost(imgString);
                            Log.d("yanzhiUpload", "onClick: "+scroeString);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    scoreTextView.setText(scroeString);
                                }
                            });
                        }
                    });
                }
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
                parameters.setPreviewFpsRange(viewWidth, viewHeight);
                //设置相机预览照片帧数
                parameters.setPreviewFpsRange(4, 10);
                //设置图片格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                //设置图片的质量
                parameters.set("jpeg-quality", 90);
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
        mCamera.setDisplayOrientation(270);//摄像头进行旋转90°
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
                parameters.set("jpeg-quality", 90);
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
            mCamera.takePicture(new Camera.ShutterCallback() {//按下快门
                @Override
                public void onShutter() {
                    //按下快门瞬间的操作
                }
            }, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {//是否保存原始图片的信息

                }
            }, pictureCallback);
        }
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            final Bitmap resource = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (resource == null) {
                Toast.makeText(getApplicationContext(), "拍照失败", Toast.LENGTH_SHORT).show();
            }
            final Matrix matrix = new Matrix();
            matrix.setRotate(-90);
            final Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);
            if (bitmap != null && iv_show != null && iv_show.getVisibility() == View.GONE) {
                iv_show.setImageBitmap(bitmap);
                savePic(bitmap);
                changeShowingViews();
            }
        }
    };

    public void savePic(final Bitmap bitmap){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }
                FileOutputStream fileOutputStream = null;
                String baseFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FaceK"+File.separator+"Camera";
                File baseFile = new File(baseFilePath);
                if (!baseFile.exists()) {
                    baseFile.mkdirs();
                    Log.d("cameraMMMMM","file created");
                }
                String pickName = GetSysTime.getCurrTime() +".jpg";
                Log.d("cameraMMMMM",pickName);
                try {
                    File picFile = new File(baseFile,pickName);
                    fileOutputStream = new FileOutputStream(picFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    MediaStore.Images.Media.insertImage(getContentResolver(), picFile.getPath(), pickName, "description");
                    Uri uri = Uri.fromFile(picFile);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    imgString = picFile.getPath();
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


    @Override
    public void onBackPressed() {
        if (iv_show.getVisibility()==View.GONE){
            stopCamera();
            super.onBackPressed();
        }else {
            changeShowingViews();
        }
    }

    public void askForPermission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET}, 3);
        }
    }

    public void changeShowingViews(){
        if (isCameraing){
            mCamera.stopPreview();
            mSurfaceView.setVisibility(View.GONE);
            flipCamera.setVisibility(View.GONE);
            takePicButt.setVisibility(View.GONE);
            iv_show.setVisibility(View.VISIBLE);
            upLoadButt.setVisibility(View.VISIBLE);
            scoreTextView.setVisibility(View.VISIBLE);
        }else{
            mSurfaceView.setVisibility(View.VISIBLE);
            flipCamera.setVisibility(View.VISIBLE);
            takePicButt.setVisibility(View.VISIBLE);
            iv_show.setVisibility(View.GONE);
            upLoadButt.setVisibility(View.GONE);
            scoreTextView.setVisibility(View.GONE);
        }
        isCameraing = !isCameraing;
    }
}