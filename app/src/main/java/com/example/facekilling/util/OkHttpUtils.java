package com.example.facekilling.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {
    private static final String URL = "http://10.14.172.15:8000/face/attribute/";

    private static OkHttpClient client = new OkHttpClient();


   /* public static boolean LogIn(String email,String password){
        Request request = null;
    }*/

    public static String YanZhiPost(String path){
        Log.d("yanzhiUpload", "YanZhiPost: in"+path);
        Request request = null;
        Response response = null;
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                            "id",
                            "2")
                    .addFormDataPart(
                            "image",
                             path,
                             RequestBody.create(MediaType.parse("image/jpg"), new File(path)));
            Log.d("yanzhiUpload", "YanZhiPost: "+path);
            RequestBody requestBody = builder.build();

            request = new Request.Builder()
                    .url(URL)
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("upload image", "上传失败:"+e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.body()!=null){
                        String json = response.body().string();
                        Log.e("upload image", "上传成功:"+json);
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (response!=null&&response.body()!=null){
                response.close();
            }
        }
        return null;
    }



}
