package com.example.facekilling.javabean;
import android.graphics.Bitmap;

public class MainUser extends User {

    private static MainUser mainUser;

    private MainUser(String user_name,int imageId){
        super(user_name,imageId);
    }
    private MainUser(String user_name,int imageId,String email){
        super(user_name,imageId,email);
    }
    private MainUser(String user_name, int imageId,String email, Bitmap bitmap){
        super(user_name,imageId,email,bitmap);
    }
    public static MainUser getInstance(String user_name,int imageId){
        if(mainUser==null){
            synchronized (MainUser.class) {
                mainUser=new MainUser(user_name,imageId);
            }
        }
        return mainUser;
    }
    public static MainUser getInstance(String user_name,int imageId,String email){
        if(mainUser==null){
            synchronized (MainUser.class) {
                mainUser=new MainUser(user_name,imageId,email);
            }
        }
        return mainUser;
    }
    public static MainUser getInstance(String user_name,int imageId, String email,Bitmap bitmap){
        if(mainUser==null){
            synchronized (MainUser.class) {
                mainUser=new MainUser(user_name,imageId,email,bitmap);
            }
        }
        return mainUser;
    }
    public static MainUser getInstance(){
        return mainUser;
    }
}
