package com.example.facekilling.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
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

}
