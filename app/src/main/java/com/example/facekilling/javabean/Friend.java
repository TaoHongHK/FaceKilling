package com.example.facekilling.javabean;

public class Friend extends User{
    private String message;
    public Friend(String user_name, int imageId, String message){
        super(user_name,imageId);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

}
