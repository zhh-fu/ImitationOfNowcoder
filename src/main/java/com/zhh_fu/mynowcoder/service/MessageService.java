package com.zhh_fu.mynowcoder.service;

import com.zhh_fu.mynowcoder.dao.MessageDAO;
import com.zhh_fu.mynowcoder.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    @Autowired
    SensitiveWordService sensitiveWordService;

    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveWordService.filter(message.getContent()));
        return messageDAO.addMessage(message) > 0 ? message.getId() : 0;
    }

    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }

    public int getConversationUnreadCount(int userId, String conversationId){
        return messageDAO.getConversationUnreadCount(userId, conversationId);
    }

    public void updateUnreadMessage(String conversationId){
        messageDAO.updateUnreadMessage(conversationId);
    }
}
