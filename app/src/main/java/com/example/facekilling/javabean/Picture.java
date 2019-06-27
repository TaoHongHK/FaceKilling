package com.example.facekilling.javabean;

import android.graphics.Bitmap;
import android.view.View;

import java.io.File;
import java.io.Serializable;
import java.util.Date;


public class Picture implements Serializable ,Cloneable{
    private Bitmap imageBitMap;
    private String imagePath;
    private int imageId = -1;

    private boolean isChecked;

    public Picture(int imageId){
        this.imageId = imageId;
        this.isChecked = false;
    }


    public Picture(Bitmap imageId){
        this.imageBitMap = imageId;
        this.isChecked = false;
}

    public Picture(Bitmap imageBitMap, String imagePath) {
        this.imageBitMap = imageBitMap;
        this.imagePath = imagePath;
        this.isChecked = false;
    }

    public Picture(Bitmap imageBitMap, String basePath, String imageName) {
        this.imageBitMap = imageBitMap;
        this.imagePath = basePath + File.separator + imageName;
        this.isChecked = false;
    }

    public Object clone(){
        Picture p=null;
        try{
            p=(Picture)super.clone();
        }
        catch(Exception e ){
            e.printStackTrace();
        }
        return p;
    }
    public Bitmap getImageBitMap() {
        return imageBitMap;
    }

    public int getImageId(){
        return  imageId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String basePath, String imageName){
        this.imagePath = basePath + File.separator + imageName;
    }
}
