package com.example.facekilling.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

public class JsonUtil {


    public static Bitmap decodeImgFromString(String jsonImgString){
        byte[] decodedBytes = Base64.getDecoder().decode(jsonImgString);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes,0,decodedBytes.length);
        return bitmap;
    }


    public static String getImgStringFromJson(String JsonInfo) throws Exception {
        String imgString =null;
        if (!"".equals(JsonInfo) && JsonInfo != null) {
            JSONObject json = new JSONObject(JsonInfo);
             imgString = json.getString("image");
        }
        return imgString;
    }

    public static String readFileFromRaw(Context context, int resourceId) {
        if (null == context || resourceId < 0) {
            return null;
        }

        String result = null;
        try {
            InputStream input = context.getResources().openRawResource(resourceId);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = input.read(buffer)) != -1) {
                output.write(buffer, 0, length);
            }
            output.close();
            input.close();
            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
