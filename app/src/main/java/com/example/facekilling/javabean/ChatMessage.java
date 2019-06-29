package com.example.facekilling.javabean;

import android.graphics.Bitmap;

public class ChatMessage {
    public String text;//信息内容
    public Bitmap img;//头像的图片id
    public boolean left = true;//控制信息显示在左边还有右边，默认左边

    public ChatMessage(String s, Bitmap i) {
        text = s;
        img = i;
    }

    public ChatMessage(String s, Bitmap i, boolean b) {
        text = s;
        img = i;
        left = b;
    }
}
