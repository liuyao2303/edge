package com.liuyao.framework.service.api;

public class Dog implements Amininal {

    @Override
    public int earn(int pay) {
        return pay + 10;
    }
}