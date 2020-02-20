package com.zhh_fu.mynowcoder.controller;

import com.zhh_fu.mynowcoder.Util.MynowcoderUtil;
import com.zhh_fu.mynowcoder.async.EventModel;
import com.zhh_fu.mynowcoder.async.EventProducer;
import com.zhh_fu.mynowcoder.async.EventType;
import com.zhh_fu.mynowcoder.model.Comment;
import com.zhh_fu.mynowcoder.model.HostHolder;
import com.zhh_fu.mynowcoder.service.CommentService;
import com.zhh_fu.mynowcoder.service.QuestionService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionService questionService;

    @Autowired
    EventProducer eventProducer;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuestionController.class);

    //为问题增加评论
    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addQuestionComment(@RequestParam("content") String content,
                                    @RequestParam("questionId") int questionId){
        try {
            Comment comment = new Comment();
            comment.setContent(content);

            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                //用户未登录时设置为匿名id
                comment.setUserId(MynowcoderUtil.ANONYMOUS_USERID);
                //也可以设置为让其登陆
                //return "redirect:/reglogin";
            }
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(MynowcoderUtil.ENTITY_QUESTION);
            comment.setStatus(0);
            commentService.addComment(comment);

            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(),count);

            //由FeedHandler来处理
            eventProducer.fireEvent(new EventModel(EventType.COMMENT)
                    .setActorId(comment.getUserId())
                    .setEntityId(questionId));

        }
        catch (Exception ex){
            logger.error("增加评论失败" + ex.getMessage());
        }
        return "redirect:/question/" + questionId;
    }
}
