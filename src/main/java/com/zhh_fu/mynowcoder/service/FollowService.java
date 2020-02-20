package com.zhh_fu.mynowcoder.service;

import com.zhh_fu.mynowcoder.Util.JedisAdapter;
import com.zhh_fu.mynowcoder.Util.RedisKeyUtil;
import org.omg.PortableServer.AdapterActivator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {

    @Autowired
    JedisAdapter jedisAdapter;

    //关注
    //关注发生两个行为
    //将关注对象放入followee中
    //将自己放入关注对象的follower中
    public boolean follow(int userId, int entityId, int entityType){
        //某个实体下的关注者（粉丝）
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
        //我关注某个类型下的什么实体
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        Date now = new Date();

        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedis.multi();
        //向某个实体的关注者set中加入自己的id
        tx.zadd(followerKey, now.getTime(), String.valueOf(userId));
        //在我关注的某个类型下，加入该类型的新实体的id
        tx.zadd(followeeKey, now.getTime(), String.valueOf(entityId));
        //执行事务
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    //取消关注
    //与关注类似，但是是从相应set中删除
    public boolean unfollow(int userId, int entityId, int entityType){
        //某个实体下的关注者（粉丝）
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
        //我关注某个类型下的什么实体
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedis.multi();
        tx.zrem(followerKey, String.valueOf(userId));
        tx.zrem(followeeKey, String.valueOf(entityId));
        //执行事务
        List<Object> ret = jedisAdapter.exec(tx, jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    //获取所有的粉丝
    public List<Integer> getFollowers(int entityId, int entityType, int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
        return getIdFromSet(jedisAdapter.zrevrange(followerKey,0, count));
    }

    //翻页获取
    public List<Integer> getFollowers(int entityId, int entityType, int offset, int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
        return getIdFromSet(jedisAdapter.zrevrange(followerKey, offset, count + offset));
    }

    //获取粉丝数量
    public long getFollowerCount(int entityId, int entityType){
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
        return jedisAdapter.zcard(followerKey);
    }

    //获取所有的关注者
    public List<Integer> getFollowees(int userId, int entityType, int count){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdFromSet(jedisAdapter.zrevrange(followeeKey,0, count));
    }

    //翻页获取
    public List<Integer> getFollowees(int userId, int entityType, int offset, int count){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdFromSet(jedisAdapter.zrevrange(followeeKey, offset, count + offset));
    }

    //获取关注者数量
    public long getFolloweeCount(int userId, int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    //判断某个用户是否关注了某个实体
    public boolean isFollower(int userId, int entityId, int entityType) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
        return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
    }

    private List<Integer> getIdFromSet(Set<String> idset){
        List<Integer> ids = new ArrayList<>();
        for (String str : idset){
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }
}
