package com.example.facekilling.javabean;

import java.util.List;

public class Expression {
    private List<Integer> position;  //人脸定位，x y w h
    private String expresiion;

    public Expression(List<Integer> position, String expresiion) {
        this.position = position;
        this.expresiion = expresiion;
    }

    public List<Integer> getPosition() {
        return position;
    }

    public String getExpresiion() {
        return expresiion;
    }
}
