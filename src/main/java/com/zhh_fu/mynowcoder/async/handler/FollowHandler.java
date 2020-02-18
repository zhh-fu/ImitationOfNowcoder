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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FollowHandler implements EventHandler {
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
        if (model.getEntityType() == MynowcoderUtil.ENTITY_QUESTION){
            message.setContent("用户" + user.getName()
                    + "关注了你的问题，http://127.0.0.1:5555/question/" + model.getEntityId());
        }
        else if (model.getEntityType() == MynowcoderUtil.ENTITY_USER) {
            message.setContent("用户" + user.getName()
                    + "关注了你，http://127.0.0.1:5555/user/" + model.getActorId());
        }
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
