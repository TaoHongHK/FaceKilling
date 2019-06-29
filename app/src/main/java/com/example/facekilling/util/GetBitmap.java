package com.example.facekilling.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.facekilling.javabean.Picture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetBitmap {

    public static Bitmap getBitmapFromSD(String path){
        File mFile=new File(path);
        //若该文件存在
        if (mFile.exists()) {
            return BitmapFactory.decodeFile(path);
        }
        else return null;
    }

    public static String getDefaultHead(Context context,String name){
        String basePath = context.getFilesDir().getAbsolutePath();
        String pickPath;
        switch (name){
            case "male":
                pickPath = basePath + File.separator + "male.jpg";
               break;
            case "female":
                pickPath = basePath + File.separator + "female.jpg";
                break;
            case "unknown":
                pickPath = basePath + File.separator +  "unknown.jpg";
                break;
            default:
                pickPath = basePath + File.separator +  "unknown.jpg";
            break;
        }
        if (!new File(pickPath).exists()){
            return null;
        }else return pickPath;
    }




    //获取用户本地存储的基本路径
    public static String getUserFilePath(int id){
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        String baseFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FaceK"+File.separator+id;
        return baseFilePath;
    }
    //存储图片到用户id文件夹下
    public static String savePicToUser(final Bitmap bitmap,final int id,final Activity activity,final Context context){
        final String[] mBitMapPath = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }
                FileOutputStream fileOutputStream = null;
                String baseFilePath = getUserFilePath(id);
                File baseFile = new File(baseFilePath);
                if (!baseFile.exists()) {
                    baseFile.mkdirs();
                }
                String pickName = GetSysTime.getCurrTime() +".jpg";
                try {
                    File picFile = new File(baseFile,pickName);
                    fileOutputStream = new FileOutputStream(picFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    MediaStore.Images.Media.insertImage(context.getContentResolver(), picFile.getPath(), pickName, "description");
                    Uri uri = Uri.fromFile(picFile);
                    activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    mBitMapPath[0] = picFile.getPath();
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
        return mBitMapPath[0];
    }
    //获取用户id文件夹下的所有图片
    public static List<Picture> getAllPicFromUser(int id){
        List<Picture> pictureList = new ArrayList<>();
        String imagePath = getUserFilePath(id);
        File fileAll = new File(imagePath);
        File[] files = fileAll.listFiles();
        if(files == null){
            return pictureList;
        }
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                Bitmap bitmap= BitmapFactory.decodeFile(file.getPath(),getBitmapOption(4));
                pictureList.add(new Picture(bitmap,imagePath,file.getName()));

            }
        }
        // 返回得到的图片列表
        return pictureList;
    }



    //获取文件的基本路径
    public static String getBaseFilePath(String filename){
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        String baseFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FaceK"+File.separator+filename;
        return baseFilePath;
    }
    //存储图片到自定义文件夹下
    public static String savePicToFile(final Bitmap bitmap,final String filename,final Activity activity,final Context context){
        final String[] mBitMapPath = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }
                FileOutputStream fileOutputStream = null;
                String baseFilePath = getBaseFilePath(filename);
                File baseFile = new File(baseFilePath);
                if (!baseFile.exists()) {
                    baseFile.mkdirs();
                }
                String pickName = GetSysTime.getCurrTime() +".jpg";
                try {
                    File picFile = new File(baseFile,pickName);
                    fileOutputStream = new FileOutputStream(picFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    MediaStore.Images.Media.insertImage(context.getContentResolver(), picFile.getPath(), pickName, "description");

                    Uri uri = Uri.fromFile(picFile);
                    activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    mBitMapPath[0] = picFile.getPath();
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
        return mBitMapPath[0];
    }
    //存储图片列表到自定义文件夹下
    public static void savePicListToFile(final List<Picture> pictureList,final String filename,final Activity activity,final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sdStatus = Environment.getExternalStorageState();
                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                    return;
                }
                FileOutputStream fileOutputStream = null;
                String baseFilePath = getBaseFilePath(filename);
                File baseFile = new File(baseFilePath);
                if (!baseFile.exists()) {
                    baseFile.mkdirs();
                }

                try {
                    for(int i=0;i<pictureList.size();i++){
                        Picture picture = pictureList.get(i);
                        String pickName = GetSysTime.getCurrTime()+ i +".jpg";
                        File picFile = new File(baseFile,pickName);
                        fileOutputStream = new FileOutputStream(picFile);
                        picture.getImageBitMap().compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        MediaStore.Images.Media.insertImage(context.getContentResolver(), picFile.getPath(), pickName, "description");
//                        fileOutputStream.flush();
                    }
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
    //获取自定义文件夹下所有的照片
    public static List<Picture> getAllPicFromUser(String filename){
        List<Picture> pictureList = new ArrayList<>();
        String imagePath = getBaseFilePath(filename);
        File fileAll = new File(imagePath);
        File[] files = fileAll.listFiles();
        if(files == null){
            return pictureList;
        }
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath())) {
                Bitmap bitmap= BitmapFactory.decodeFile(file.getPath(),getBitmapOption(4));
                pictureList.add(new Picture(bitmap,imagePath,file.getName()));
            }
        }
        // 返回得到的图片列表
        return pictureList;
    }



    //删除本地图片
    public static void deleteImgFile(final String imgPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(imgPath);
                // 路径为文件且不为空则进行删除
                if (file.isFile() && file.exists()) {
                    file.delete();
                }
            }
        }).start();
    }
    //删除本地图片列表
    public static void deleteImgListFile(final List<Picture> pictureList){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(Picture picture:pictureList){
                    File file = new File(picture.getImagePath());
                    // 路径为文件且不为空则进行删除
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                }
            }
        }).start();
    }
    //删除本地文件夹
    public static void deleteFilePackage(final String filename){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String baseFile = getBaseFilePath(filename);
                File fileAll = new File(baseFile);
                File[] files = fileAll.listFiles();
                for(File file:files){
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                }

            }
        }).start();

    }

    //压缩文件的操作
    private static BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }
    //检查扩展名，得到图片格式的文件
    private static boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }
    //获取存储权限
    public static void askForStorePermission(Activity activity, Context context){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.INTERNET}, 3);
        }
    }


    //判断网络状态
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }



}
