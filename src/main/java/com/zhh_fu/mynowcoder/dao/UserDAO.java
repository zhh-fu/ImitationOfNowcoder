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

    @Insert({"insert into ", TABLE_NAME ," (name, password, salt, head_url )" +
    "values(#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select * from ", TABLE_NAME ," where id = #{id}"})
    User selectById(int id);

    @Update({"update ", TABLE_NAME ," set password = #{password} where id = #{id}"})
    void updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME ," where id = #{id}"})
    void deleteById(int id);
}
