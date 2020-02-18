package com.zhh_fu.mynowcoder.controller;

import com.zhh_fu.mynowcoder.Util.MynowcoderUtil;
import com.zhh_fu.mynowcoder.model.HostHolder;
import com.zhh_fu.mynowcoder.model.Question;
import com.zhh_fu.mynowcoder.model.User;
import com.zhh_fu.mynowcoder.model.ViewObject;
import com.zhh_fu.mynowcoder.service.CommentService;
import com.zhh_fu.mynowcoder.service.FollowService;
import com.zhh_fu.mynowcoder.service.QuestionService;
import com.zhh_fu.mynowcoder.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
//首页
public class IndexController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FollowService followService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));

        User user = userService.getUser(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(userId, MynowcoderUtil.ENTITY_USER));
        vo.set("followeeCount", followService.getFolloweeCount(userId, MynowcoderUtil.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), userId, MynowcoderUtil.ENTITY_USER));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        return "profile";
    }

    @RequestMapping(path = {"/index"}, method = {RequestMethod.GET})
    public String index(Model model){
        model.addAttribute("vos",getQuestions(0,0,10));
        return "index";
    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit){
        List<Question> questionList = questionService.getLatestQuestions(userId,offset,limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question:questionList) {
            ViewObject vo = new ViewObject();
            //前端通过vo.questionkey来获取question对象
            //也就是通过key来获取
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vo.set("followCount", followService.getFollowerCount(question.getId(), MynowcoderUtil.ENTITY_QUESTION));
            vos.add(vo);
        }
        return vos;
    }
}
