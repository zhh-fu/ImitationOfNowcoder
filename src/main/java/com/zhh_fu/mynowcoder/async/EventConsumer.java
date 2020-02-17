package com.zhh_fu.mynowcoder.async;

import com.alibaba.fastjson.JSON;
import com.zhh_fu.mynowcoder.Util.JedisAdapter;
import com.zhh_fu.mynowcoder.Util.RedisKeyUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventConsumer implements InitializingBean ,ApplicationContextAware {
    //将同一种类型的事件放入同一个Handler的处理队列中
    private Map<EventType,List<EventHandler>> config = new HashMap<EventType,List<EventHandler>>();
    private ApplicationContext applicationContext;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        //获取所有实现了EventHandler接口的类
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        //构建映射表
        if (beans != null){
            for (Map.Entry<String,EventHandler> entry : beans.entrySet()){
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for (EventType type : eventTypes){
                    if (!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    //将能处理相同类型的处理器放在一起
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    //返回元素的key和元素值
                    //如果没有元素可取一直阻塞直到超时
                    List<String> events = jedisAdapter.brpop(0,key);
                    for (String message : events){
                        if (message.equals(key)){
                            continue;
                        }

                        //反序列化
                        EventModel eventModel = JSON.parseObject(message,EventModel.class);
                        //不包含证明为非法事件
                        if (!config.containsKey(eventModel.getType())){
                            logger.error("不能识别的事件类型");
                            continue;
                        }

                        //找到关联的handler去挨个处理
                        for (EventHandler handler : config.get(eventModel.getType())){
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
