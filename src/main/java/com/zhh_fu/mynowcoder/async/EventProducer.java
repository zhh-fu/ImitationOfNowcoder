package com.zhh_fu.mynowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.zhh_fu.mynowcoder.Util.JedisAdapter;
import com.zhh_fu.mynowcoder.Util.RedisKeyUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;

@Service
//事件的实现者，入口
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EventProducer.class);


    //将事件放入处理队列
    public boolean fireEvent(EventModel eventModel){
        try{
            //可以采用BlockingQueue进行
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            return true;
        }
        catch (Exception ex){
            logger.error("事件处理出错" + ex.getMessage());
            return false;
        }
    }

}
