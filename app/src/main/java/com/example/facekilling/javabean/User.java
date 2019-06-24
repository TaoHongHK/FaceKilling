package com.example.facekilling.javabean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class User implements Serializable {
    protected String email;
    protected String user_name;
    protected int imageId;
    protected Bitmap bitmap;

    public User(String user_name,int imageId) {
        this.imageId = imageId;
        this.user_name = user_name;
    }
    public User(String user_name,int imageId,String email) {
        this.email = email;
        this.imageId = imageId;
        this.user_name = user_name;
    }

    public User(String user_name, int imageId,String email, Bitmap bitmap) {
        this.email = email;
        this.user_name = user_name;
        this.imageId = imageId;
        this.bitmap = bitmap;
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public boolean equals(User obj) {
        if(this.email.equals(obj.getEmail()) ){
            return true;
        }
        else{
            return false;
        }
    }
}
