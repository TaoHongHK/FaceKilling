package com.example.facekilling.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetPKScores {
    private static final String URL = "http://127.0.0.1:8000/test_img/";

    private OkHttpClient client;
    private FileInputStream fileInputStream;
    private Bitmap bitmap;

    public Bitmap getBitmap(){
        client = new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();
        try {
            Response response= client.newCall(request).execute();
            fileInputStream = new FileInputStream(response.toString());
            bitmap = BitmapFactory.decodeStream(fileInputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }

}
