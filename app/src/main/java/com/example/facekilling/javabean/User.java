package com.example.facekilling.javabean;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    protected String email;     //邮箱地址
    protected String password;   //账⼾密码
    protected String user_name;   //昵称
    protected int imageId = -1;  //仅用于测试
    protected Bitmap imageBitMap;   //头像
    protected String gender;    //性别["male", "female", "unknown"]
    protected String phone;     //手机号
    protected String address;   //地址

    protected int user_id = -1;    //用户id，唯一

    public User(String user_name,int imageId) {
        this.imageId = imageId;
        this.user_name = user_name;
    }
    public User(String user_name,int imageId,String email) {
        this.email = email;
        this.imageId = imageId;
        this.user_name = user_name;
    }

    public User(String user_name,Bitmap imageBitMap) {
        this.imageBitMap = imageBitMap;
        this.user_name = user_name;
    }

    public User(String user_name,Bitmap imageBitMap,String email) {
        this.email = email;
        this.imageBitMap = imageBitMap;
        this.user_name = user_name;
    }


    public User(String email, String user_name, Bitmap imageBitMap, String gender, String phone, String address) {
        this.email = email;
        this.user_name = user_name;
        this.imageBitMap = imageBitMap;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }

    public User() {
    }



    public boolean equals(User obj) {
        if(this.email.equals(obj.getEmail()) ){
            return true;
        }
        else{
            return false;
        }
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getImageId() {
        return imageId;
    }

    public Bitmap getImageBitMap() {
        return imageBitMap;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setImageBitMap(Bitmap imageBitMap) {
        this.imageBitMap = imageBitMap;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

}
