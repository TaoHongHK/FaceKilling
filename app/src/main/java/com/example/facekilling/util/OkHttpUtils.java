package com.example.facekilling.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUtils {
    private static final String URL1 = "http://10.13.7.78:8000/test_get/?get=abv";
    private static final String URL3 = "http://127.0.0.1:8000/test_img/";

    private static OkHttpUtils okHttpUtils = new OkHttpUtils();
    private OkHttpClient client;
    private Request request;
    private Response response;
    private FileInputStream fileInputStream;
    private Bitmap bitmap;

    public OkHttpUtils(){
        this.client = new OkHttpClient();
    }

    public static OkHttpUtils getInstance(){
        return okHttpUtils;
    }

   /* public Bitmap getBitmap(){
        Request request = new Request.Builder().url(URL).post().build();
        try {
            Response response= client.newCall(request).execute();
            fileInputStream = new FileInputStream(response.toString());
            bitmap = BitmapFactory.decodeStream(fileInputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }*/

    public String getSometing(){
        String theReturn =null;
        request = new Request.Builder().url(URL1).build();
        Call call = client.newCall(request);
        try {
            response = call.execute();
            theReturn = response.body().string();
        }catch (IOException ie){
            ie.printStackTrace();
        }finally{
            if (response.body()!=null){
                response.body().close();
            }
        }
        return theReturn;
    }

}
