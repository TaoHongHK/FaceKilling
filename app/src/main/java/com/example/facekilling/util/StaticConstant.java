package com.example.facekilling.util;

public class StaticConstant {
    public static final int GETBITMAP_FROM_CAMERA = 0;
    public static final int GETBITMAP_FROM_APP = 1;
    public static final int GETBITMAP_FROM_SD = 2;
    public static final int GETCOF_FROM_NEW = 3;
    public static final int CAMERA_RETURN = 4;
    public static final String FUNCTIONBUTT_REQUEST = "FunctionButtRequest";
    public static final String BITMAP_PATH = "BitmapPath";
    public static final String FRONT_IP = "http://";
    public static final String IP = "10.14.172.138";
    public static final String PORT = "8000";
    public static final String GET_TEST = FRONT_IP + IP + ":"+PORT+"/test_get/";
    public static final String POST_TEST = FRONT_IP + IP + ":"+PORT+"/test_post/";
    public static final String FACE_ATTRIBUTE = FRONT_IP + IP + ":"+PORT+"/face/attribute/";
    public static final String FACE_EXPRESSION = FRONT_IP+IP+":"+PORT+"/face/expression/";
    public static final String LOGIN = FRONT_IP+IP+":"+PORT+"/account/login/";
    public static final String REGISTER = FRONT_IP+IP+":"+PORT+"/account/register/";
    public static final String DELETE_ACCOUNT = FRONT_IP+IP+":"+PORT+"/account/delete/";
    public static final String FORGET_PWD = FRONT_IP+IP+":"+PORT+"/account/forget/";
    public static final String CHANGE_ACCOUNT = FRONT_IP+IP+":"+PORT+"/account/change/";
    public static final String GET_ACCOUNT_INFO = FRONT_IP+IP+":"+PORT+"/account/get_account/";
    public static final String GET_EMAIL = FRONT_IP+IP+":"+PORT+"/account/get_email/";
    public static final String GET_ID = FRONT_IP+IP+":"+PORT+"/account/get_id/";
    public static final String FRIEND_ADD = FRONT_IP+IP+":"+PORT+"/friend/add/";
    public static final String FRIEND_DELETE = FRONT_IP+IP+":"+PORT+"/friend/delete/";
    public static final String SEND_MSG = FRONT_IP+IP+":"+PORT+"/information/send/";
    public static final String REFRESH_MSG = FRONT_IP+IP+":"+PORT+"/information/refresh/";
}
