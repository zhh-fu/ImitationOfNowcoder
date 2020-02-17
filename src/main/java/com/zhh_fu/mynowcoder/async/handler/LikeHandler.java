package com.zhh_fu.mynowcoder.async.handler;

import com.zhh_fu.mynowcoder.Util.MynowcoderUtil;
import com.zhh_fu.mynowcoder.async.EventHandler;
import com.zhh_fu.mynowcoder.async.EventModel;
import com.zhh_fu.mynowcoder.async.EventType;
import com.zhh_fu.mynowcoder.model.Message;
import com.zhh_fu.mynowcoder.model.User;
import com.zhh_fu.mynowcoder.service.MessageService;
import com.zhh_fu.mynowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
//点赞处理
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(MynowcoderUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());

        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName()
                + "攒了你的评论，http://127.0.0.1:5555/question/" + model.getExt("questionId"));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
