package com.example.facekilling.javabean;

import android.graphics.Bitmap;

public class Friend extends User{
    private String message;

    public Friend(int id,String email,String user_name,Bitmap bitmap, String gender,String phone,String address){
        super(email,user_name,bitmap,gender,phone,address);
        this.user_id = id;
    }

    public String getMessage(){
        return message;
    }

}
