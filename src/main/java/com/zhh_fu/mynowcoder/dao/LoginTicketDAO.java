package com.zhh_fu.mynowcoder.dao;

import com.zhh_fu.mynowcoder.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface LoginTicketDAO {

    //表名，为常量
    final String TABLE_NAME = "login_ticket";
    final String INSERT_FIELDS = " user_id, expired, status, ticket ";

    @Insert({"insert into ", TABLE_NAME ,"(", INSERT_FIELDS ,") " +
      "values (#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket loginTicket);

    @Select({"select * from ", TABLE_NAME , " where ticket = #{ticket}"})
    LoginTicket selectByTicket(String ticket);

    //用户登出，改变状态
    @Update({"update ", TABLE_NAME , " set status = #{status} where ticket = #{ticket}"})
    void updateTicket(@Param("ticket") String ticket,@Param("status") int status);
}
