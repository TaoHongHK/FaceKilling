package com.example.facekilling.javabean;


import java.io.Serializable;

public class Review implements Serializable {
    private int userId;
    private String content;

    public Review(int userId,String content){
        this.userId = userId;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public int getUserId() {
        return userId;
    }
}
