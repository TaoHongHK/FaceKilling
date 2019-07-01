package com.example.facekilling.javabean;


import java.io.Serializable;

public class Review implements Serializable {
    private int userId;
    private String content;
    private int reviewId;

    public Review(int userId,String content){
        this.userId = userId;
        this.content = content;
    }
    public Review(int userId,int reviewId,String content){
        this.userId = userId;
        this.reviewId = reviewId;
        this.content = content;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getContent() {
        return content;
    }

    public int getUserId() {
        return userId;
    }
}
