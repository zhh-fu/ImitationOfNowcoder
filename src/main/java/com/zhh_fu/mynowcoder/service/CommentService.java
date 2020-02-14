package com.zhh_fu.mynowcoder.service;

import com.zhh_fu.mynowcoder.dao.CommentDAO;
import com.zhh_fu.mynowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    @Autowired
    SensitiveWordService sensitiveWordService;

    //获取当前实体的全部评论
    public List<Comment> getCommentByEntity(int entityId,int entityType){
        return commentDAO.selectCommentByEntity(entityId, entityType);
    }

    //增加评论
    public int addComment(Comment comment){
        //题目和内容过滤html语句
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        //敏感词过滤
        comment.setContent(sensitiveWordService.filter(comment.getContent()));

        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDAO.getCommentCount(entityId, entityType);
    }

    public boolean deleteComment(int commentId){
        return commentDAO.updateStatus(commentId, 1) > 0;
    }
}
