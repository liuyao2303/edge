package com.liuyao.framework.service.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Dog implements Animinal {

    @Override
    public int earn(int pay) {
        return pay + 10;
    }

    @Override
    public void fee(String fee) {
        System.out.println(fee);
    }

    @Override
    public Map<String, String> getMaps(Date date) {
        System.out.println(date);
        Map<String,String> values = new HashMap<>();
        values.put("date", new Date().toString());
        return values;
    }
}