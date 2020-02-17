package com.zhh_fu.mynowcoder.dao;

import com.zhh_fu.mynowcoder.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
//定义为接口，不需要具体去实现
public interface CommentDAO {
    //表名，为常量
    final String TABLE_NAME = "comment";
    final String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status";

    //注解的方式
    @Insert({"insert into ", TABLE_NAME ," (", INSERT_FIELDS ," )" +
            " values(#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);


    //通过实体选择评论
    @Select({"select * from ", TABLE_NAME ,
                " where entity_id = #{entityId} and entity_type = #{entityType} and status = 0 "
                  + " order by created_date desc"})
    List<Comment> selectCommentByEntity(@Param("entityId") int entityId,
                                         @Param("entityType") int entityType);


    @Select({"select * from ", TABLE_NAME, "  where id=#{id} "})
    Comment getCommentById(@Param("id") int id);

    //通过实体得到数量
    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} "})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    //更新状态
    @Update({"update ", TABLE_NAME, " set status = #{status} where id = #{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);


}
