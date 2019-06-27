package com.example.facekilling.javabean;
import android.graphics.Bitmap;

import java.util.List;

public class MainUser extends User {

    private

    List<Picture> pictureList;
    private List<Integer> friendIdList;
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

    private MainUser(String email, String user_name, Bitmap imageBitMap, String gender, String phone, String address) {
        super(email, user_name, imageBitMap, gender, phone, address);
    }

    public static MainUser newInstance(String user_name, int imageId){
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

    public static MainUser newInstance(String email, String user_name, Bitmap imageBitMap, String gender, String phone, String address){
        if(mainUser==null){
            synchronized (MainUser.class) {
                mainUser=new MainUser(email, user_name, imageBitMap, gender, phone, address);
            }
        }
        return mainUser;
    }

    public static MainUser getInstance(){
        return mainUser;
    }

    public List<Picture> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<Picture> pictureList) {
        this.pictureList = pictureList;
    }
    public void addPicture(Picture picture){
        this.pictureList.add(picture);
    }
    public void addPictureList(List<Picture> pictureList){
        this.pictureList.addAll(pictureList);
    }

    public List<Integer> getFriendIdList() {
        return friendIdList;
    }

    public void setFriendIdList(List<Integer> friendIdList) {
        this.friendIdList = friendIdList;
    }
    public void addFriendId(int friendId){
        this.friendIdList.add(friendId);
    }
    public void addFriendIdList(List<Integer> friendIdList){
        this.friendIdList.addAll(friendIdList);
    }


}
