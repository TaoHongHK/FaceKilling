package com.example.facekilling.javabean;
import android.graphics.Bitmap;

import java.util.List;

public class MainUser extends User {

    private static MainUser mainUser;
    //测试用
    private MainUser(String user_name,int imageId){
        super(user_name,imageId);
    }
    private MainUser(String user_name,int imageId,String email){
        super(user_name,imageId,email);
    }
    //真正使用
    private MainUser(String user_name,Bitmap imageBitMap){
        super(user_name,imageBitMap);
    }
    private MainUser(String user_name,Bitmap imageBitMap,String email){
        super(user_name,imageBitMap,email);
    }
    private MainUser(String user_name, Bitmap imageBitMap, String email, String password, int user_id, List<Picture> pictureList){
        super(user_name,imageBitMap,email,password,user_id,pictureList);
    }
    public static MainUser newInstance(String user_name,int imageId){
        if(mainUser==null){
            synchronized (MainUser.class) {
                mainUser=new MainUser(user_name,imageId);
            }
        }
        return mainUser;
    }

    public static MainUser newInstance(String user_name,int imageId,String email){
        if(mainUser==null){
            synchronized (MainUser.class) {
                mainUser=new MainUser(user_name,imageId,email);
            }
        }
        return mainUser;
    }

    public static MainUser newInstance(String user_name,Bitmap imageBitMap,String email){
        if(mainUser==null){
            synchronized (MainUser.class) {
                mainUser=new MainUser(user_name,imageBitMap,email);
            }
        }
        return mainUser;
    }
    public static MainUser newInstance(String user_name,Bitmap imageBitMap){
        if(mainUser==null){
            synchronized (MainUser.class) {
                mainUser=new MainUser(user_name,imageBitMap);
            }
        }
        return mainUser;
    }
    public static MainUser newInstance(String user_name, Bitmap imageBitMap, String email, String password, int user_id, List<Picture> pictureList) {
        if(mainUser==null){
            synchronized (MainUser.class) {
                mainUser=new MainUser(user_name,imageBitMap,email,password,user_id,pictureList);
            }
        }
        return mainUser;
    }
    public static MainUser getInstance(){
        return mainUser;
    }
}
