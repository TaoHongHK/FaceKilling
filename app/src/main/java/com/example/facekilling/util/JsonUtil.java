package com.example.facekilling.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.facekilling.javabean.MainUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class JsonUtil {


    public static MainUser decodeMainUserFronJson(String JsonInfo) throws Exception{
        int error = -1;
        MainUser mainUser = null;

        if (!"".equals(JsonInfo) && JsonInfo != null) {
            JSONObject json = new JSONObject(JsonInfo);
            error = json.getInt("error");
            Log.d("Mlogin", "register error: "+json.getInt("error"));
            if (error == 0) {
                //String email, String user_name, Bitmap imageBitMap, String gender, String phone, String address
                mainUser = MainUser.newInstance(json.getString("email"),json.getString("nickname"),
                        JsonUtil.decodeImgFromImgString(json.getString("head")),
                                json.getString("gender"),json.getString("phone"),json.getString("address"));
                mainUser.setFriendIdList(JsonUtil.getFrindsList(json));
            }
        }
        return mainUser;
    }

    public static List<Integer> getFrindsList(JSONObject jsonObject){
        List<Integer> friendsList = new ArrayList<>();
        try {
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("friendID"));
            int num  = jsonObject1.getInt("num");
            if(num!=0){
                JSONArray friendIDArray = jsonObject1.getJSONArray("friendID");
                for (int i = 0;i<friendIDArray.length();i++){
                    friendsList.add((Integer)friendIDArray.get(i));
                }
            }
        }catch (JSONException je){
            je.printStackTrace();
        }
        return friendsList;
    }

    public static int decodeIDFromJson(String JsonInfo) throws Exception{
        int error = -1;
        int result = -1;
        if (!"".equals(JsonInfo) && JsonInfo != null) {
            JSONObject json = new JSONObject(JsonInfo);
            error = json.getInt("error");
            Log.d("Mlogin", "register error: "+json.getInt("error"));
            if (error == 0) {
                Log.d("Mlogin", "register id: " + json.getInt("id"));
                result = json.getInt("id");
            }
        }
        return result;
    }

    public static Bitmap decodeImgFromImgString(String jsonImgString){
        byte[] decodedBytes = Base64.getDecoder().decode(jsonImgString);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes,0,decodedBytes.length);
        return bitmap;
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

    public static int decodeErrorFronJson(String JsonInfo)throws Exception{
        int error = -1;
        if (!"".equals(JsonInfo) && JsonInfo != null) {
            JSONObject json = new JSONObject(JsonInfo);
            error = json.getInt("error");
        }
        return error;
    }
}
