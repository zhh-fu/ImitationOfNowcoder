package com.zhh_fu.mynowcoder.async.handler;

import com.zhh_fu.mynowcoder.async.EventHandler;
import com.zhh_fu.mynowcoder.async.EventModel;
import com.zhh_fu.mynowcoder.async.EventType;
import com.zhh_fu.mynowcoder.controller.QuestionController;
import com.zhh_fu.mynowcoder.service.SearchService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AddQuestionHandler implements EventHandler {
    @Autowired
    SearchService searchService;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Override
    public void doHandle(EventModel model) {
        try{
            searchService.indexQuestion(model.getEntityId(),model.getExt("title"),
                                        model.getExt("content"));
        }
        catch (Exception ex){
            logger.error("增加题目索引失败" + ex.getMessage());
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
