package com.zhh_fu.mynowcoder.controller;

import com.zhh_fu.mynowcoder.Util.MynowcoderUtil;
import com.zhh_fu.mynowcoder.model.HostHolder;
import com.zhh_fu.mynowcoder.model.Message;
import com.zhh_fu.mynowcoder.model.User;
import com.zhh_fu.mynowcoder.model.ViewObject;
import com.zhh_fu.mynowcoder.service.MessageService;
import com.zhh_fu.mynowcoder.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @RequestMapping(value = {"/msg/list"} ,method = {RequestMethod.GET})
    public String getConversationList(Model model){
        if (hostHolder.getUser() == null){
            return "redirect:/reglogin";
        }
        int localUserId = hostHolder.getUser().getId();
        List<Message> conversationList = messageService.getConversationList(localUserId,0,10);
        List<ViewObject> conversations = new ArrayList<>();
        for (Message message : conversationList){
            ViewObject vo = new ViewObject();
            vo.set("conversation",message);
            int target = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
            vo.set("user",userService.getUserById(target));
            vo.set("unread",messageService.getConversationUnreadCount(localUserId, message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations",conversations);
        return "letter";
    }

    @RequestMapping(value = {"/msg/detail"} ,method = {RequestMethod.GET})
    public String getConversationDetail(Model model,@RequestParam("conversationId") String conversationId){
        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for (Message message : messageList){
                ViewObject vo = new ViewObject();
                vo.set("message",message);
                vo.set("user",userService.getUserById(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
            //进入该页面后，所有的信息状态全部更新
            messageService.updateUnreadMessage(conversationId);
        }
        catch (Exception ex){
            logger.error("获取详情失败" + ex.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(value = {"/msg/addMessage"} ,method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content){
        try {
            if (hostHolder.getUser() == null){
                return MynowcoderUtil.getJSONString(999,"未登录");
            }

            //接收用户
            User user = userService.getUserByName(toName);
            if (user == null){
                return MynowcoderUtil.getJSONString(1,"用户不存在");
            }

            Message message = new Message();
            //发送用户
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setContent(content);
            message.setCreatedDate(new Date());
            messageService.addMessage(message);
            return MynowcoderUtil.getJSONString(0);
        }
        catch (Exception ex){
            logger.error("发送失败" + ex.getMessage());
            return MynowcoderUtil.getJSONString(1,"发送失败");
        }
    }
}
