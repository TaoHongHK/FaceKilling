package com.example.facekilling.javabean;


import java.io.Serializable;

public class Review implements Serializable {
    private User user;
    private String content;

    public Review(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }
}
