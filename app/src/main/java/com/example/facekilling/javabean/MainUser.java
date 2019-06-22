package com.example.facekilling.javabean;
import android.graphics.Bitmap;

public class MainUser extends User {

    private static MainUser mainUser;

    private MainUser(String user_name,int imageId){
        super(user_name,imageId);
    }
    private MainUser(String email,String user_name,int imageId){
        super(email,user_name,imageId);
    }
    private MainUser(String email,String user_name, int imageId, Bitmap bitmap){
        super(email,user_name,imageId,bitmap);
    }
    public static MainUser getInstance(String user_name,int imageId){
        if(mainUser==null){
            synchronized (MainUser.class) {
                mainUser=new MainUser(user_name,imageId);
            }
        }
        return mainUser;
    }
    public static MainUser getInstance(String email,String user_name,int imageId){
        if(mainUser==null){
            synchronized (MainUser.class) {
                mainUser=new MainUser(email,user_name,imageId);
            }
        }
        return mainUser;
    }
    public static MainUser getInstance(String email,String user_name,int imageId, Bitmap bitmap){
        if(mainUser==null){
            synchronized (MainUser.class) {
                mainUser=new MainUser(email,user_name,imageId,bitmap);
            }
        }
        return mainUser;
    }
    public static MainUser getInstance(){
        return mainUser;
    }
}
