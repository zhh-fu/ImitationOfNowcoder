package com.zhh_fu.mynowcoder.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhh_fu.mynowcoder.Util.MynowcoderUtil;
import com.zhh_fu.mynowcoder.async.EventModel;
import com.zhh_fu.mynowcoder.async.EventProducer;
import com.zhh_fu.mynowcoder.async.EventType;
import com.zhh_fu.mynowcoder.model.*;
import com.zhh_fu.mynowcoder.service.CommentService;
import com.zhh_fu.mynowcoder.service.FollowService;
import com.zhh_fu.mynowcoder.service.QuestionService;
import com.zhh_fu.mynowcoder.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class FollowController {
    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FollowService followService;

    @Autowired
    EventProducer eventProducer;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FollowController.class);

    //关注用户
    @RequestMapping(path = {"/followUser"},method = {RequestMethod.POST})
    @ResponseBody
    public String addFollower(@RequestParam("userId") int userId){
        User user = hostHolder.getUser();
        if (user == null){
            return MynowcoderUtil.getJSONString(999);
        }

        boolean ret = followService.follow(user.getId(), userId, MynowcoderUtil.ENTITY_USER);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(user.getId()).setEntityId(userId)
                .setEntityType(MynowcoderUtil.ENTITY_USER)
                .setEntityOwnerId(userId));

        return MynowcoderUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(user.getId(), MynowcoderUtil.ENTITY_USER)));
    }

    //取消关注用户
    @RequestMapping(path = {"/unfollowUser"},method = {RequestMethod.POST})
    @ResponseBody
    public String unfollow(@RequestParam("userId") int userId){
        User user = hostHolder.getUser();
        if (user == null){
            return MynowcoderUtil.getJSONString(999);
        }

        boolean ret = followService.unfollow(user.getId(), userId, MynowcoderUtil.ENTITY_USER);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(user.getId()).setEntityId(userId)
                .setEntityType(MynowcoderUtil.ENTITY_USER)
                .setEntityOwnerId(userId));

        return MynowcoderUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(user.getId(), MynowcoderUtil.ENTITY_USER)));
    }

    //关注问题
    @RequestMapping(path = {"/followQuestion"},method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId){
        User user = hostHolder.getUser();
        if (user == null){
            return MynowcoderUtil.getJSONString(999);
        }

        Question question = questionService.getQuestionById(questionId);
        if (question == null){
            return MynowcoderUtil.getJSONString(1, "问题不存在");
        }

        boolean ret = followService.follow(user.getId(), questionId, MynowcoderUtil.ENTITY_QUESTION);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(user.getId()).setEntityId(questionId)
                .setEntityType(MynowcoderUtil.ENTITY_QUESTION)
                .setEntityOwnerId(questionId));

        //存储关注者的信息
        Map<String, Object> info = new HashMap<String, Object>();
        info.put("id", user.getId());
        info.put("name", user.getName());
        info.put("headUrl", user.getHeadUrl());
        info.put("count", followService.getFollowerCount(questionId, MynowcoderUtil.ENTITY_QUESTION));

        return MynowcoderUtil.getJSONString(ret ? 0 : 1, info);
    }

    //取消关注问题
    @RequestMapping(path = {"/unfollowQuestion"},method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId){
        User user = hostHolder.getUser();
        if (user == null){
            return MynowcoderUtil.getJSONString(999);
        }

        Question question = questionService.getQuestionById(questionId);
        if (question == null){
            return MynowcoderUtil.getJSONString(1, "问题不存在");
        }

        boolean ret = followService.unfollow(user.getId(), questionId, MynowcoderUtil.ENTITY_QUESTION);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setActorId(user.getId()).setEntityId(questionId)
                .setEntityType(MynowcoderUtil.ENTITY_QUESTION)
                .setEntityOwnerId(questionId));

        Map<String, Object> info = new HashMap<>();
        info.put("id", user.getId());
        info.put("count", followService.getFollowerCount(questionId, MynowcoderUtil.ENTITY_QUESTION));
        return MynowcoderUtil.getJSONString(ret ? 0 : 1, info);
    }

    //当前用户关注了哪些人
    @RequestMapping(path = {"/user/{uid}/followees"},method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId){
        List<Integer> followeeIds = followService.getFollowees(userId,MynowcoderUtil.ENTITY_USER,0,-1);
        if (hostHolder.getUser() != null){
            model.addAttribute("followees", getUserInfo(hostHolder.getUser().getId(), followeeIds));
        }
        else {
            model.addAttribute("followees", getUserInfo(0,followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(userId, MynowcoderUtil.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";
    }

    //当前用户被哪些人关注
    @RequestMapping(path = {"/user/{uid}/followers"},method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId){
        List<Integer> followerIds = followService.getFollowers(userId,MynowcoderUtil.ENTITY_USER,0,-1);
        if (hostHolder.getUser() != null){
            model.addAttribute("followers", getUserInfo(hostHolder.getUser().getId(), followerIds));
        }
        else {
            model.addAttribute("followers", getUserInfo(0,followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(userId, MynowcoderUtil.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";

    }

    //取出所有数据
    private List<ViewObject> getUserInfo(int localUserId, List<Integer> userId){
        List<ViewObject> userInfos = new ArrayList<>();
        for (Integer uid : userId ){
            User user = userService.getUser(uid);
            if (user == null){
                continue;
            }

            ViewObject vo = new ViewObject();
            vo.set("user",user);
            vo.set("followerCount", followService.getFollowerCount(uid,MynowcoderUtil.ENTITY_USER));
            vo.set("followeeCount", followService.getFolloweeCount(uid,MynowcoderUtil.ENTITY_USER));
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            if (localUserId != 0){
                vo.set("followed", followService.isFollower(localUserId, uid, MynowcoderUtil.ENTITY_USER));
            }
            else{
                vo.set("followed",false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
