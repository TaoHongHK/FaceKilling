package com.example.facekilling.javabean;


import com.example.facekilling.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Cof implements Serializable {
    private User user;
    private int userId;
    private String content;      //发表的内容
    private String date;           //发表的时间
    List<Picture> imagesList;        //发表的所有图片
    private int praise_num = 0;      //点赞数
    private List<Integer> like_ids;  //点赞列表
    private List<Review> reviewList;
    private int cof_id;

    private boolean UserLikeStatus;         //false表示用户之前没点赞，ture表示点赞了
    private boolean likeStatus;             //false表示目前没点赞，true表示目前点赞了



    //用于从数据库中加载朋友圈
    public Cof(int userId, String content, String date, List<Picture> imagesList,int praise_num,
               List<Integer> like_ids,List<Review> reviewList){
        this.userId = userId;
        this.content = content;
        this.date = date;
        this.imagesList = new ArrayList<>();
        this.imagesList.addAll(imagesList);
        this.praise_num=praise_num;
        this.like_ids = like_ids;
        this.reviewList = reviewList;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }


    public String getDate() {
        return date;
    }

    public List<Picture> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<Picture> imagesList) {
        this.imagesList = imagesList;
    }


    public int getPraise_num() {
        return praise_num;
    }
    public void setPraise_num(int praise_num) {
        this.praise_num = praise_num;
    }


    public List<Review> getReviewList() {
        return reviewList;
    }
    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }


    public int getCof_id() {
        return cof_id;
    }
    public void setCof_id(int cof_id) {
        this.cof_id = cof_id;
    }


    public List<Integer> getLike_ids() {
        return like_ids;
    }

    public boolean isUserLikeStatus() {
        return UserLikeStatus;
    }

    public void setUserLikeStatus(boolean userLikeStatus) {
        UserLikeStatus = userLikeStatus;
    }

    public boolean isLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(boolean likeStatus) {
        this.likeStatus = likeStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
