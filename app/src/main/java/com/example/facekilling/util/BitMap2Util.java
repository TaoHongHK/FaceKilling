package com.example.facekilling.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

    public static Bitmap idToBitMap(Resources res, int id){
        InputStream is = res.openRawResource(id);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }


}
