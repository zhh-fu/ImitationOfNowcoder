package com.zhh_fu.mynowcoder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.zhh_fu.mynowcoder.Util.JedisAdapter;
import com.zhh_fu.mynowcoder.Util.MynowcoderUtil;
import com.zhh_fu.mynowcoder.Util.RedisKeyUtil;
import com.zhh_fu.mynowcoder.async.EventHandler;
import com.zhh_fu.mynowcoder.async.EventModel;
import com.zhh_fu.mynowcoder.async.EventType;
import com.zhh_fu.mynowcoder.model.Feed;
import com.zhh_fu.mynowcoder.model.Question;
import com.zhh_fu.mynowcoder.model.User;
import com.zhh_fu.mynowcoder.service.FeedService;
import com.zhh_fu.mynowcoder.service.FollowService;
import com.zhh_fu.mynowcoder.service.QuestionService;
import com.zhh_fu.mynowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler {

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void doHandle(EventModel model) {

        //产生新的feed
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));

        if (feed.getData() == null){
            return;
        }

        feedService.addFeed(feed);

        //给事件的粉丝推送

        List<Integer> followers = followService.getFollowers(model.getActorId(),MynowcoderUtil.ENTITY_USER,100);
        followers.add(0);
        //给所有粉丝推送
        for (int follower : followers){
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        //关注或者评论行为
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }

    private String buildFeedData(EventModel model){
        Map<String,String> map = new HashMap<String,String>();
        User actor = userService.getUser(model.getActorId());
        if (actor == null){
            return null;
        }
        map.put("userId",String.valueOf(actor.getId()));
        map.put("userHead",actor.getHeadUrl());
        map.put("userName",actor.getName());

        if (model.getType() == EventType.COMMENT ||
                (model.getType() == EventType.FOLLOW && model.getEntityType() == MynowcoderUtil.ENTITY_QUESTION)){
            Question question = questionService.getQuestionById(model.getEntityId());
            if (question == null){
                return null;
            }

            map.put("questionId",String.valueOf(question.getId()));
            map.put("questionTitle",question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }
}
