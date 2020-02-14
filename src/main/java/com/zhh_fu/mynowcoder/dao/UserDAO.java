package com.zhh_fu.mynowcoder.dao;

import com.zhh_fu.mynowcoder.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
//定义为接口，不需要具体去实现
public interface UserDAO {
    //表名，为常量
    final String TABLE_NAME = "user";

    //增
    @Insert({"insert into ", TABLE_NAME ," (name, password, salt, head_url )" +
    "values(#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    //查
    @Select({"select * from ", TABLE_NAME ," where id = #{id}"})
    User selectById(int id);

    @Select({"select * from ", TABLE_NAME ," where name = #{name}"})
    User selectByName(@Param("name") String name);

    //改
    @Update({"update ", TABLE_NAME ," set password = #{password} where id = #{id}"})
    void updatePassword(User user);

    //删
    @Delete({"delete from ", TABLE_NAME ," where id = #{id}"})
    void deleteById(@Param("id") int id);
}
