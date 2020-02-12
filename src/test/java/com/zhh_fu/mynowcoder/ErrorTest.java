package com.zhh_fu.mynowcoder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorTest {
    public static void main(String[] args){
        Date date = new Date();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(date));
        long addTime = 3600*24*1000*100L;
        date.setTime((long)3600*24*1000*100 + date.getTime());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(date));
    }
}
