package com.zhh_fu.mynowcoder.dao;

import com.zhh_fu.mynowcoder.model.Comment;
import com.zhh_fu.mynowcoder.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MessageDAO {
    //表名，为常量
    final String TABLE_NAME = "message";
    final String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id";

    //增
    @Insert({"insert into ", TABLE_NAME ," (", INSERT_FIELDS ," )" +
            " values(#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);


    //通过唯一标识码选择信息
    @Select({"select * from ", TABLE_NAME ,
            " where conversation_id = #{conversationId} order by created_date desc "
             + "limit #{limit} offset #{offset}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    //通过conversationId选择相应的信息并选出最晚的
//    @Select({"select m1.from_id, m1.to_id, m1.content, m1.created_date, m1.has_read, m1.conversation_id, m2.id from ", TABLE_NAME ,
//            " as m1 inner join (select MAX(id) as max1, count(id) as id from ", TABLE_NAME,
//            " where from_id=#{userId} or to_id=#{userId} group by conversation_id) as m2 " +
//            " on m1.id = m2.max1 order by created_date desc limit #{limit} offset #{offset}"})
    @Select({"select ", INSERT_FIELDS, ", m2.id from ", TABLE_NAME ,
            " as m1 inner join (select MAX(id) as max1, count(id) as id from ", TABLE_NAME,
            " where from_id=#{userId} or to_id=#{userId} group by conversation_id) as m2 " +
                    " on m1.id = m2.max1 order by created_date desc limit #{limit} offset #{offset}"})
    List<Message> getConversationList(@Param("userId") int conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    //未读消息的数量
    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Update({"update ", TABLE_NAME ," set has_read = 1 where conversation_id=#{conversationId}"})
    int updateUnreadMessage(@Param("conversationId") String conversationId);


}
