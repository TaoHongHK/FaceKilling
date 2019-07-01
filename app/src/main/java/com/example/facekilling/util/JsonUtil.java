package com.example.facekilling.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.facekilling.javabean.Cof;
import com.example.facekilling.javabean.MainUser;
import com.example.facekilling.javabean.Picture;
import com.example.facekilling.javabean.Review;
import com.example.facekilling.javabean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.example.facekilling.util.OkHttpUtils.getUserInfoY;

public class JsonUtil {


    public static MainUser decodeMainUserFronJson(String JsonInfo,int id) throws Exception{
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
                mainUser.setUser_id(id);
            }
        }
        return mainUser;
    }

    public static User decodeUserFronJson(String JsonInfo, int id) throws Exception{
        int error = -1;
        User user = null;

        if (!"".equals(JsonInfo) && JsonInfo != null) {
            JSONObject json = new JSONObject(JsonInfo);
            error = json.getInt("error");
            Log.d("Mlogin", "register error: "+json.getInt("error"));
            if (error == 0) {
                //String email, String user_name, Bitmap imageBitMap, String gender, String phone, String address
                user = new User(json.getString("email"),json.getString("nickname"),
                        JsonUtil.decodeImgFromImgString(json.getString("head")),
                        json.getString("gender"),json.getString("phone"),json.getString("address"));
                user.setUser_id(id);
            }
        }
        return user;
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
            Log.d("changeProfile", "register error: "+json.getInt("error"));
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

    public static int decodeErrorFronJson(String JsonInfo)throws JSONException{
        int error = -1;
        if (!"".equals(JsonInfo) && JsonInfo != null) {
            JSONObject json = new JSONObject(JsonInfo);
            error = json.getInt("error");
        }
        return error;
    }

    public static List<Cof> decodeCofListFromCofString(String string) throws JSONException, ParseException {
        List<Cof> cofList = new ArrayList<>();
        JSONObject json = new JSONObject(string);
        JSONArray jsonCofArray = json.getJSONArray("forum");
        int num = json.getInt("num");
        for(int i=0;i<num;i++){
            JSONObject jsonCof = jsonCofArray.getJSONObject(i);
            final int cof_id = jsonCof.getInt("id");
            int userId = jsonCof.getInt("user_id");
            String dateString = jsonCof.getString("dateTime");
            //内容
            String content = jsonCof.getString("describe");
            //图片列表
            JSONObject jsonImageObject = jsonCof.getJSONObject("image");
            int image_num = jsonImageObject.getInt("num");
            List<Picture> pictureList = new ArrayList<>();
            if(image_num != 0){
                JSONArray jsonImageArray = jsonImageObject.getJSONArray("image");
                for(int j=0;j<image_num;j++){
                    Bitmap bitmap = decodeImgFromImgString((String) jsonImageArray.get(j));
                    pictureList.add(new Picture(bitmap));
                }
            }

            //评论
            JSONObject jsonReviewObject = jsonCof.getJSONObject("comment");
            int review_num = jsonReviewObject.getInt("num");
            List<Review> reviewList = new ArrayList<>();
            if(review_num != 0){
                JSONArray jsonReviewArray = jsonReviewObject.getJSONArray("comment");
                for(int j=0;j<review_num;j++){
                    JSONObject jsonReview = jsonReviewArray.getJSONObject(j);
                    int review_id = jsonReview.getInt("id");
                    int review_user_id = jsonReview.getInt("user_id");
                    String review_content = jsonReview.getString("content");
                    Review review = new Review(review_user_id,review_id,review_content);
                    reviewList.add(review);
                }
            }

            //点赞
            JSONObject jsonLikeObject = jsonCof.getJSONObject("like");
            int like_num = jsonLikeObject.getInt("num");
            List<Integer> like_ids = new ArrayList<>();
            if(like_num != 0){
                JSONArray jsonLikeArray = jsonLikeObject.getJSONArray("like_id");
                for(int j=0;j<like_num;j++){
                    like_ids.add((Integer) jsonLikeArray.get(j));
                }
            }

            final Cof cof = new Cof(userId,content,dateString,pictureList,like_num,like_ids,reviewList);
            cof.setCof_id(cof_id);
            final User[] users = {null};

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    users[0] = getUserInfoY(cof.getUserId());
                }
            });
            thread.start();
            try
            {
                thread.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            cof.setUser(users[0]);
            cofList.add(cof);
        }
        return cofList;
    }
}
