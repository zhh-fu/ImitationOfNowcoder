package com.zhh_fu.mynowcoder.controller;

import com.zhh_fu.mynowcoder.Util.MynowcoderUtil;
import com.zhh_fu.mynowcoder.model.HostHolder;
import com.zhh_fu.mynowcoder.model.Question;
import com.zhh_fu.mynowcoder.service.QuestionService;
import com.zhh_fu.mynowcoder.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
//问题控制层
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @RequestMapping(value = {"/question/add"} ,method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@Param("title") String title,@Param("content") String content){
        try{
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            //匿名用户，设置为统一的userId
            if (hostHolder.getUser() == null){
                question.setUserId(MynowcoderUtil.ANONYMOUS_USERID);
            }
            else {
                question.setUserId(hostHolder.getUser().getId());
            }

            //成功全部返回0
            if (questionService.addQuestion(question) > 0){
                return MynowcoderUtil.getJSONString(0);
            }
        }
        catch (Exception ex){
            logger.error("添加问题失败" + ex.getMessage());
        }
        return MynowcoderUtil.getJSONString(1,"失败");
    }

    @RequestMapping(path = {"/question/{qid}"})
    public String questionDetail(Model model,
                                 @PathVariable("qid") int qid){
        Question question = questionService.getQuestionById(qid);
        model.addAttribute("question",question);
        model.addAttribute("user",userService.getUser(question.getUserId()));
        return "detail";
    }
}