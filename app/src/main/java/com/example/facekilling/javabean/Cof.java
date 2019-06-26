package com.example.facekilling.javabean;


import com.example.facekilling.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Cof implements Serializable {
    private User user;
    private String content;      //发表的内容
    private Date date;           //发表的时间
    List<Picture> imagesList;        //发表的所有图片
    private int praise_num;      //点赞数
    private List<Review> reviewList;

    public Cof(User user,String content){
        this.user = user;
        this.content = content;
        this.date = new Date();
        this.imagesList = new ArrayList<>();
        initPictures();
        this.praise_num=0;
        this.reviewList = new ArrayList<>();
        initReview();
    }
    //用于创建一个朋友圈消息
    public Cof(User user,String content,List<Picture> imagesList){
        this.user = user;
        this.content = content;
        this.date = new Date();
        this.imagesList = new ArrayList<>();
        this.imagesList.addAll(imagesList);
        this.praise_num=0;
        this.reviewList = new ArrayList<>();
    }
    //用于从数据库中加载朋友圈
    public Cof(User user, String content, Date date, List<Picture> imagesList,int praise_num,
               List<Review> reviewList){
        this.user = user;
        this.content = content;
        this.date = date;
        this.imagesList = new ArrayList<>();
        this.imagesList.addAll(imagesList);
        this.praise_num=praise_num;
        this.reviewList = new ArrayList<>();
        reviewList.addAll(reviewList);
    }

    public User getUser() {
        return user;
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



    public int getPraise_num() {
        return praise_num;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    //用于测试评论
    private void initReview(){
        String user_name = "测试";
        User user = new User(user_name, R.drawable.picture_04);
        this.reviewList.add(new Review(user,"这是测试1"));
        this.reviewList.add(new Review(user,"这是测试1这是测试2"));
        this.reviewList.add(new Review(user,"这是测试1这是测试2这是测试3"));
        this.reviewList.add(new Review(user,"这是测试1这是测试2这是测试3这是测试4"));
        this.reviewList.add(new Review(user,"asdfasdfasdf"));
    }

    //用于测试显示图片
    private Picture[] pictures = {
            new Picture(R.drawable.picture_01),
            new Picture(R.drawable.picture_02),
            new Picture(R.drawable.picture_03),
            new Picture(R.drawable.picture_04),
            new Picture(R.drawable.picture_05),
            new Picture(R.drawable.picture_06),
            new Picture(R.drawable.picture_07),
            new Picture(R.drawable.picture_08),
            new Picture(R.drawable.picture_09),
            new Picture(R.drawable.picture_10),
            new Picture(R.drawable.picture_11),
            new Picture(R.drawable.picture_12),
            new Picture(R.drawable.picture_13),
            new Picture(R.drawable.picture_14),
    };
    private void initPictures(){
        imagesList.clear();
        for(int i=0;i<4;i++){
            Random random = new Random();
            int index = random.nextInt(pictures.length);
            imagesList.add(pictures[index]);
        }
    }


}
