package com.zhh_fu.mynowcoder;


import com.zhh_fu.mynowcoder.dao.QuestionDAO;
import com.zhh_fu.mynowcoder.dao.UserDAO;
import com.zhh_fu.mynowcoder.model.Question;
import com.zhh_fu.mynowcoder.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.Random;

@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTest {
    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Test
    public void initDatabase(){
        Random random = new Random();
        for (int i = 0; i < 11;i++){
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
            user.setName(String.format("user%d",i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);
            if (i == 10){
                user.setPassword("fuzhihang");
                userDAO.updatePassword(user);
            }
            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            question.setCreatedDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE{%d}", i));
            question.setContent(String.format("Balaababalalalal Content %d", i));
            questionDAO.addQuestion(question);
        }
        userDAO.deleteById(2);

        System.out.println(questionDAO.selectLatestQuestions(0,0,9));
        System.out.println(questionDAO.selectQuestionById(4));
    }
}
