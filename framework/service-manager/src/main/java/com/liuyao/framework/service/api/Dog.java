package com.liuyao.framework.service.api;

public class Dog implements Animinal {

    @Override
    public int earn(int pay) {
        return pay + 10;
    }

    @Override
    public void fee(String fee) {
        System.out.println(fee);
    }
}