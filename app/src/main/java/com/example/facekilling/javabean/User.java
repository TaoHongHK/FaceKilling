package com.example.facekilling.javabean;

import android.graphics.Bitmap;

public class User {
    protected String email;
    protected String user_name;
    protected int imageId;
    protected Bitmap bitmap;

    public User(String user_name,int imageId) {
        this.imageId = imageId;
        this.user_name = user_name;
    }
    public User(String email,String user_name,int imageId) {
        this.email = email;
        this.imageId = imageId;
        this.user_name = user_name;
    }

    public User(String email,String user_name, int imageId, Bitmap bitmap) {
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
}
