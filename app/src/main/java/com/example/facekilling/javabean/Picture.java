package com.example.facekilling.javabean;

import android.graphics.Bitmap;
import android.view.View;

import java.io.Serializable;
import java.util.Date;


public class Picture implements Serializable ,Cloneable{
    private Date data;
    private Bitmap imageBitMap;
    private int imageId;

    private boolean isChecked;

    public Picture(int imageId){
        this.imageId = imageId;
        this.isChecked = false;
    }



    public Picture(Bitmap imageId){
        this.imageBitMap = imageId;
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
}
