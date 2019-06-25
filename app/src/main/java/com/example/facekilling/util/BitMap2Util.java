package com.example.facekilling.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;


public class BitMap2Util {
    public static byte[] BitMap2ByteArray(Bitmap bitmap){
            if (bitmap!=null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                return baos.toByteArray();
            }else return null;
    }


}
