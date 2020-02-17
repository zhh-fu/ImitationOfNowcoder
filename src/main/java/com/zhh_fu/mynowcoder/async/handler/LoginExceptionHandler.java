package com.zhh_fu.mynowcoder.async.handler;

import com.zhh_fu.mynowcoder.Util.MailSendUtil;
import com.zhh_fu.mynowcoder.async.EventHandler;
import com.zhh_fu.mynowcoder.async.EventModel;
import com.zhh_fu.mynowcoder.async.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MailSendUtil mailSendUtil;

    @Override
    public void doHandle(EventModel model) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", model.getExt("username"));
        mailSendUtil.sendWithHTMLTemplate(model.getExt("email"),"登陆IP异常","mails/login_exception.html",map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
