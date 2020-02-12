package com.zhh_fu.mynowcoder.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    //实际上为每个线程都分配了一个User对象
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    //通过get()方法找到当前线程对应的user
    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
