package com.example.facekilling.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
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

}
