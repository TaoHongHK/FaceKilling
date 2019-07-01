package com.example.facekilling.javabean;

import android.graphics.Bitmap;

public class ChatMessage {
    public static final int TYPE_RECEIVE = 0;
    public static final int TYPE_SEND = 1;

    private String content;//信息内容
    private Bitmap img;//头像的图片id
    private int type;


    public ChatMessage(String content, Bitmap i, int type) {
        this.content = content;
        this.img = i;
        this.type = type;
    }

    public String getContent(){
        return content;
    }

    public int getType() {
        return type;
    }

    public Bitmap getImg(){
        return img;
    }
}
