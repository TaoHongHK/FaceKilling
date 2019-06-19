package com.example.facekilling.util;

import java.util.Date;

public class Picture {
    private Date data;
    private int imageId;

    public Picture(int imageId){
        this.imageId = imageId;
    }
    public int getImageId() {
        return imageId;
    }
}
