package com.example.facekilling.javabean;

import android.graphics.Bitmap;

public class Information {
    private int post_id;
    private int recieve_id;
    private String information = null;
    private Bitmap bitmap = null;

    public Information(int post_id, int recieve_id, String information) {
        this.post_id = post_id;
        this.recieve_id = recieve_id;
        this.information = information;
    }

    public Information(int post_id, int recieve_id, Bitmap bitmap) {
        this.post_id = post_id;
        this.recieve_id = recieve_id;
        this.bitmap = bitmap;
    }

    public int getPost_id() {
        return post_id;
    }

    public int getRecieve_id() {
        return recieve_id;
    }

    public String getInformation() {
        return information;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
