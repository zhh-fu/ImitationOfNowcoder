package com.zhh_fu.mynowcoder.controller;

import com.zhh_fu.mynowcoder.Util.JedisAdapter;
import com.zhh_fu.mynowcoder.Util.MynowcoderUtil;
import com.zhh_fu.mynowcoder.Util.RedisKeyUtil;
import com.zhh_fu.mynowcoder.model.Feed;
import com.zhh_fu.mynowcoder.model.HostHolder;
import com.zhh_fu.mynowcoder.service.FeedService;
import com.zhh_fu.mynowcoder.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {
    @Autowired
    FeedService feedService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    //拉取消息
    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET})
    private String getPullFeeds(Model model){
        //此处用户 0 会产生干扰
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        List<Integer> followees = new ArrayList<Integer>();
        if (localUserId != 0){
            //登陆用户关注的人
            followees = followService.getFollowees(localUserId, MynowcoderUtil.ENTITY_USER, Integer.MAX_VALUE);
        }
        //取出关注的人的feeds
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE,followees,10);
        model.addAttribute("feeds",feeds);
        return "feeds";
    }

    //推送消息
    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET})
    private String getPushFeeds(Model model){
        //此处用户 0 会产生干扰, 0 代表系统
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        //取出feeds的id
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0 ,10);

        List<Feed> feeds = new ArrayList<Feed>();
        for (String feedId : feedIds){
            Feed feed =  feedService.getFeedById(Integer.parseInt(feedId));
            if (feed == null){
                continue;
            }
            feeds.add(feed);
        }
        model.addAttribute("feeds",feeds);
        return "feeds";
    }

}
