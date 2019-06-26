package com.example.facekilling.javabean;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    protected String email;
    protected String user_name;
    protected int imageId = -1;  //仅用于测试
    protected Bitmap imageBitMap;

    protected String password;
    protected int user_id;
    protected List<Picture> pictureList;

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
    public User(String user_name,Bitmap imageBitMap,String email,String password,int user_id,List<Picture> pictureList) {
        this.email = email;
        this.imageBitMap = imageBitMap;
        this.user_name = user_name;
        this.password = password;
        this.user_id = user_id;
        this.pictureList = pictureList;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getImageId() {
        return imageId;
    }


    public boolean equals(User obj) {
        if(this.email.equals(obj.getEmail()) ){
            return true;
        }
        else{
            return false;
        }
    }

    public Bitmap getImageBitMap() {
        return imageBitMap;
    }

    public String getPassword() {
        return password;
    }

    public int getUser_id() {
        return user_id;
    }

    public List<Picture> getPictureList() {
        return pictureList;
    }
}
