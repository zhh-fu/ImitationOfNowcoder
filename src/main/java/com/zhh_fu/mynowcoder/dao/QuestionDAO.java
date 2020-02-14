package com.zhh_fu.mynowcoder.dao;

import com.zhh_fu.mynowcoder.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
//定义为接口，不需要具体去实现
public interface QuestionDAO {
    //表名，为常量
    final String TABLE_NAME = "question";
    final String INSERT_FIELDS = " title, content, created_date, user_id, comment_count";

    //注解的方式
    @Insert({"insert into ", TABLE_NAME ," (", INSERT_FIELDS ," )" +
            " values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    //xml的方式
    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    Question selectQuestionById(@Param("id") int id);

    @Update({"update ", TABLE_NAME ," set comment_count = #{commentCount} where id = #{id} "})
    int updateCommentCount(@Param("id") int id,@Param("commentCount") int commentCount);


}
