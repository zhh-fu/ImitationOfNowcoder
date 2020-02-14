package com.zhh_fu.mynowcoder.service;

import com.zhh_fu.mynowcoder.dao.QuestionDAO;
import com.zhh_fu.mynowcoder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    SensitiveWordService sensitiveWordService;

    public int addQuestion(Question question){

        //题目和内容过滤html语句
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        //敏感词过滤
        question.setTitle(sensitiveWordService.filter(question.getTitle()));
        question.setContent(sensitiveWordService.filter(question.getContent()));


        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;

    }

    public List<Question> getLatestQuestions(int userId, int offset, int limit){
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public Question getQuestionById(int qid){
        return questionDAO.selectQuestionById(qid);
    }
}
