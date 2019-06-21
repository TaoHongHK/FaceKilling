package com.example.facekilling.util;

import android.graphics.Bitmap;

public class User {
    private String user_name;
    private int imageId;
    Bitmap bitmap;

    public User(String user_name,int imageId) {
        this.imageId = imageId;
        this.user_name = user_name;
    }

    public User(String user_name, int imageId, Bitmap bitmap) {
        this.user_name = user_name;
        this.imageId = imageId;
        this.bitmap = bitmap;
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
