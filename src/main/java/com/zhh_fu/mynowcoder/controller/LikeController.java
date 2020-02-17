package com.zhh_fu.mynowcoder.controller;

import com.zhh_fu.mynowcoder.Util.JedisAdapter;
import com.zhh_fu.mynowcoder.Util.MynowcoderUtil;
import com.zhh_fu.mynowcoder.async.EventModel;
import com.zhh_fu.mynowcoder.async.EventProducer;
import com.zhh_fu.mynowcoder.async.EventType;
import com.zhh_fu.mynowcoder.model.Comment;
import com.zhh_fu.mynowcoder.model.HostHolder;
import com.zhh_fu.mynowcoder.service.CommentService;
import com.zhh_fu.mynowcoder.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/like"} ,method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        if (hostHolder.getUser() == null){
            return MynowcoderUtil.getJSONString(999);
        }

        Comment comment = commentService.getCommentById(commentId);

        eventProducer.fireEvent(new EventModel(EventType.LIKE).
                setActorId(hostHolder.getUser().getId()).
                setEntityId(commentId).
                setEntityType(MynowcoderUtil.ENTITY_COMMENT).
                setExt("questionId",String.valueOf(comment.getEntityId())).
                setEntityOwnerId(comment.getUserId()));

        long likeCount = likeService.like(hostHolder.getUser().getId(),commentId,MynowcoderUtil.ENTITY_COMMENT);
        return MynowcoderUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"} ,method = {RequestMethod.POST})
    @ResponseBody
    public String disLike(@RequestParam("commentId") int commentId){
        if (hostHolder.getUser() == null){
            return MynowcoderUtil.getJSONString(999);
        }

        long likeCount = likeService.disLike(hostHolder.getUser().getId(),commentId,MynowcoderUtil.ENTITY_COMMENT);
        return MynowcoderUtil.getJSONString(0,String.valueOf(likeCount));
    }

}
