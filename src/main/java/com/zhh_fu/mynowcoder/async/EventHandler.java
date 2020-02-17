package com.zhh_fu.mynowcoder.async;

import java.util.List;

//事件处理器接口
public interface EventHandler {

    //处理代码
    void doHandle(EventModel model);
    //注册自己，声明自己对这些事件进行处理
    List<EventType> getSupportEventTypes();
}
