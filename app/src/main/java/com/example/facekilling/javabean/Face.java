package com.example.facekilling.javabean;

import java.util.List;

public class Face {
    private List<Integer> position;  //人脸定位，x y w h
    private String attractive;  //是否好看
    private String bread;       //是否有胡子
    private String chubby;       //是否圆润
    private String eyeglasses;   //是否戴眼镜
    private String hair_color;   //头发颜色
    private String hair_shape;   //发型
    private String hat;          //是否戴帽子
    private String makeup;       //是否化妆
    private String sex;          //性别
    private String attra_score;   //颜值的分

    public Face(List<Integer> position, String attractive, String bread, String chubby, String eyeglasses, String hair_color, String hair_shape, String hat, String makeup, String sex, String attra_score) {
        this.position = position;
        this.attractive = attractive;
        this.bread = bread;
        this.chubby = chubby;
        this.eyeglasses = eyeglasses;
        this.hair_color = hair_color;
        this.hair_shape = hair_shape;
        this.hat = hat;
        this.makeup = makeup;
        this.sex = sex;
        this.attra_score = attra_score;
    }

    public String getAttractive() {
        return attractive;
    }

    public String getBread() {
        return bread;
    }

    public String getChubby() {
        return chubby;
    }

    public String getEyeglasses() {
        return eyeglasses;
    }

    public String getHair_color() {
        return hair_color;
    }

    public String getHair_shape() {
        return hair_shape;
    }

    public String getHat() {
        return hat;
    }

    public String getMakeup() {
        return makeup;
    }

    public String getSex() {
        return sex;
    }

    public String getAttra_score() {
        return attra_score;
    }
}
