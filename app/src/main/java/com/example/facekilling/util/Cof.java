package com.example.facekilling.util;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class Cof {
    private int user_image;       //用户头像
    private String user_name;     //用户名称
    private String content;      //发表的内容
    private Date date;           //发表的时间
    List<Integer> images;        //发表的所有图片
    private int praise_num;      //点赞数

    //用于创建一个朋友圈消息
    public Cof(int user_image,String user_name,String content,List<Integer> images){
        this.user_image = user_image;
        this.user_name = user_name;
        this.content = content;
        this.date = new Date();;
        this.images = images;
        this.praise_num=0;
    }
    //用于从数据库中加载朋友圈
    public Cof(int user_image, String user_name, String content, Date date, List<Integer> images,int praise_num){
        this.user_image = user_image;
        this.user_name = user_name;
        this.content = content;
        this.date = date;
        this.images = images;
        this.praise_num=praise_num;
    }

    public int getUser_image() {
        return user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public List<Integer> getImages() {
        return images;
    }
    public Integer getImage(){
        int max = images.size();
        Random random = new Random();
        int index = random.nextInt(max);
        return images.get(index);
    }
}
