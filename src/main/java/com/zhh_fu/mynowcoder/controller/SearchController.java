package com.zhh_fu.mynowcoder.controller;

import com.zhh_fu.mynowcoder.Util.MynowcoderUtil;
import com.zhh_fu.mynowcoder.async.EventModel;
import com.zhh_fu.mynowcoder.async.EventProducer;
import com.zhh_fu.mynowcoder.async.EventType;
import com.zhh_fu.mynowcoder.model.Comment;
import com.zhh_fu.mynowcoder.model.HostHolder;
import com.zhh_fu.mynowcoder.model.Question;
import com.zhh_fu.mynowcoder.model.ViewObject;
import com.zhh_fu.mynowcoder.service.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    SearchService searchService;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @RequestMapping(path = {"/search"},method = {RequestMethod.GET})
    public String search(Model model,
                         @RequestParam("q") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0")  int offset,
                         @RequestParam(value = "count", defaultValue = "10")  int count){
        try {
            List<Question> questionList = searchService.searchQuestion(keyword,offset,count,"<em>", "</em>");
            List<ViewObject> vos = new ArrayList<>();
            for (Question question:questionList) {
                Question q = questionService.getQuestionById(question.getId());
                ViewObject vo = new ViewObject();
                if (question.getContent() != null){
                    q.setContent(question.getContent());
                }
                if (question.getTitle() != null){
                    q.setTitle(question.getTitle());
                }
                vo.set("question", q);
                vo.set("user", userService.getUser(q.getUserId()));
                vo.set("followCount", followService.getFollowerCount(question.getId(), MynowcoderUtil.ENTITY_QUESTION));
                vos.add(vo);
            }
            model.addAttribute("vos",vos);
        }
        catch (Exception ex){
            logger.error("搜索失败" + ex.getMessage());
        }
        return "result";
    }
}
