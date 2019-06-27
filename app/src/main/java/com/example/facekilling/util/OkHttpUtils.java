package com.example.facekilling.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.facekilling.javabean.MainUser;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class OkHttpUtils {

    private static OkHttpClient client = new OkHttpClient();



    /**
     * @param email
     * @param password*/
    public static int LogIn(String email,String password) {
        int result = -1;
        //发起请求
        final Request request = new Request.Builder()
                .url(StaticConstant.LOGIN+"?email="+email+"&password="+password)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = JsonUtil.decodeIDFromJson(response.body().string());
                response.body().close();
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param nickName
     * @param email
     * @param password
     * @param mobile
     * @param gender
     * @param address
     * @param path*/
    public static int SignUp(String nickName,String email,String password,
                             String mobile,String gender,String address,String path){
        int result = -1;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "email",
                        email)
                .addFormDataPart(
                        "password",
                        password)
                .addFormDataPart(
                        "nickname",
                        nickName)
                .addFormDataPart(
                        "head",
                        path,
                        RequestBody.create(MediaType.parse("image/jpg"), new File(path)))
                .addFormDataPart(
                        "gender",
                        gender)
                .addFormDataPart(
                        "phone",
                        mobile)
                .addFormDataPart(
                        "address",
                        address);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(StaticConstant.REGISTER).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = JsonUtil.decodeIDFromJson(response.body().string());
                response.body().close();
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param id
     */
    public static MainUser GetMainUserInfo(int id){
        MainUser mainUser = null;
        //发起请求
        final Request request = new Request.Builder()
                .url(StaticConstant.GET_ACCOUNT_INFO+"?id="+ id)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                mainUser = JsonUtil.decodeMainUserFronJson(response.body().string());
                response.body().close();
            } else {
                throw new IOException("Unexpected code:" + response);
            }
        } catch (IOException ie) {
            ie.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return mainUser;
    }

    /**
     * @param email
     * @param password
     */
    public boolean DleteAccount(String email,String password){
        boolean result = false;
        FormBody.Builder builder = new FormBody.Builder()
                .add("email",email)
                .add("password",password);
        Request request = new Request.Builder().url(StaticConstant.DELETE_ACCOUNT)
                .post(builder.build()).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                String json = response.body().string();
                result = (JsonUtil.decodeIDFromJson(json)!=-1);
                response.body().close();
            }
        }catch (IOException ie){
            ie.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param email
     * @param phone
     * @param newPassword
     */
    public boolean ForgetPassword(String email,String phone,String newPassword){
        boolean result = false;
        FormBody.Builder builder = new FormBody.Builder()
                .add("email",email)
                .add("phone",phone)
                .add("newPassword",newPassword);
        Request request = new Request.Builder().url(StaticConstant.FORGET_PWD)
                .post(builder.build()).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                String json = response.body().string();
                result = (JsonUtil.decodeIDFromJson(json)!=-1);
                response.body().close();
            }
        }catch (IOException ie){
            ie.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param id
     * @param password
     * @param newEmail
     * @param newPassword
     * @param newNickname
     * @param newHead
     * @param newGender
     * @param newPhone
     * @param newAddress
     */
    public boolean ChangeAccountInfo(int id,String email,String password,String newEmail,
                                     String newPassword,String newNickname,String newHead,String newGender,
                                     String newPhone,String newAddress){
        boolean result = false;
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .addFormDataPart("id",String.valueOf(id))
                .addFormDataPart("email",email)
                .addFormDataPart("newEmail",newEmail)
                .addFormDataPart("newPassword",newPassword)
                .addFormDataPart("newNickName",newNickname)
                .addFormDataPart("newHead",newHead,RequestBody.create(MediaType.parse("image/jpg"),new File(newHead)))
                .addFormDataPart("newGender",newGender)
                .addFormDataPart("newPhone",newPhone)
                .addFormDataPart("newAddress",newAddress);
        Request request = new Request.Builder().url(StaticConstant.FORGET_PWD)
                .post(builder.build()).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                String json = response.body().string();
                result = (JsonUtil.decodeIDFromJson(json)!=-1);
                response.body().close();
            }
        }catch (IOException ie){
            ie.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }




}
