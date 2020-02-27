package com.zhh_fu.mynowcoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ErrorTest {
    public static void main(String[] args){
//        Date date = new Date();
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(date));
//        long addTime = 3600*24*1000*100L;
//        date.setTime((long)3600*24*1000*100 + date.getTime());
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(date));
        HashMap<Short,String> map = new HashMap<>();
        map.put((short)0,String.valueOf(0));
        for(short i=1;i<10;i++){
            map.put(i,String.valueOf(i));
            System.out.println(map.size());
            map.remove(i-1,String.valueOf(i-1));
            System.out.println(map.size());
            map.put(i,String.valueOf(i+1));
            System.out.println(map.size());
        }
        System.out.println(map.size());
    }
}
