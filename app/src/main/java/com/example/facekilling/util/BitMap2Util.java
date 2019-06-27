package com.example.facekilling.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.facekilling.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class BitMap2Util {
    public static byte[] BitMap2ByteArray(Bitmap bitmap){
            if (bitmap!=null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                return baos.toByteArray();
            }else return null;
    }

    public static Bitmap Drawable2BitMap(Resources res, int id){
       return BitmapFactory.decodeResource(res, id);
    }


}
