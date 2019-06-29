package com.example.facekilling.javabean;


import com.example.facekilling.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Cof implements Serializable {
    private int userId;
    private String content;      //发表的内容
    private Date date;           //发表的时间
    List<Picture> imagesList;        //发表的所有图片
    private int praise_num = 0;      //点赞数
    private List<Review> reviewList;
    private int cof_id;

    public Cof(int userId,String content){
        this.userId = userId;
        this.content = content;
        this.date = new Date();
        this.imagesList = new ArrayList<>();
        this.praise_num=0;
        this.reviewList = new ArrayList<>();
    }
    //用于创建一个朋友圈消息
    public Cof(int userId,String content,List<Picture> imagesList){
        this.userId = userId;
        this.content = content;
        this.date = new Date();
        this.imagesList = imagesList;
        this.praise_num=0;
        this.reviewList = new ArrayList<>();
    }
    //用于从数据库中加载朋友圈
    public Cof(int userId, String content, Date date, List<Picture> imagesList,int praise_num,
               List<Review> reviewList){
        this.userId = userId;
        this.content = content;
        this.date = date;
        this.imagesList = new ArrayList<>();
        this.imagesList.addAll(imagesList);
        this.praise_num=praise_num;
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


    public Date getDate() {
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
}
