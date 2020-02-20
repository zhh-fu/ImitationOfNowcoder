package com.zhh_fu.mynowcoder.dao;

import com.zhh_fu.mynowcoder.model.Feed;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FeedDAO {
    //表名，为常量
    final String TABLE_NAME = "feed";
    final String INSERT_FIELDS = " user_id, data, created_date, type";

    //注解的方式
    @Insert({"insert into ", TABLE_NAME ," (", INSERT_FIELDS ," )" +
            " values(#{userId},#{data},#{createdDate},#{type})"})
    int addFeed(Feed feed);

    @Select({"select * from ", TABLE_NAME, "  where id=#{id} "})
    Feed getFeedById(@Param("id") int id);

    //未登录选择所有的userId
    //登陆状态选择关注的用户ID
    //maxId是一页内容的最大Id
    //count为一页内容的数量
    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);

}
