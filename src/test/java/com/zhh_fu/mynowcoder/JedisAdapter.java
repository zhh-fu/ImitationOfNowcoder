package com.zhh_fu.mynowcoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhh_fu.mynowcoder.model.User;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

 class JedisAdapter {
    public static void print(int index, Object obj){
        System.out.println(String.format("%d, %s",index,obj.toString()));
    }

    public static void main(String[] args){
        Jedis jedis = new Jedis("redis://localhost:6379/14");
        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        jedis.rename("hello","newhello");
        //验证码一类自动保存时间
        jedis.setex("testhello",20,"zxc");

        jedis.set("pv","100");
        jedis.incr("pv");
        print(2,jedis.get("pv"));


        //list
        String listName = "list";
        jedis.del(listName);
        for (int i=0;i<10;i++){
            jedis.lpush(listName,"a" + String.valueOf(i));
        }
        print(4,jedis.lrange(listName,0,5));
        print(5,jedis.llen(listName));
        print(6,jedis.lpop(listName));
        print(7,jedis.llen(listName));
        print(8,jedis.lindex(listName,3));
        print(9,jedis.linsert(listName,BinaryClient.LIST_POSITION.AFTER,"a4","zxc"));
        print(10,jedis.linsert(listName,BinaryClient.LIST_POSITION.BEFORE,"a4","qwer"));
        print(11,jedis.lrange(listName,0,15));


        //hash
        String userKey = "userxx";
        jedis.hset(userKey,"field","study");
        jedis.hset(userKey,"name","fuzhihang");
        jedis.hset(userKey,"age","24");
        jedis.hset(userKey,"phone","13259942426");
        print(12,jedis.hget(userKey,"name"));
        print(13,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"field");
        jedis.hdel(userKey,"name1");
        print(14,jedis.hgetAll(userKey));
        print(15,jedis.hexists(userKey,"zxc"));
        print(16,jedis.hexists(userKey,"age"));
        print(17,jedis.hkeys(userKey));
        print(18,jedis.hvals(userKey));
        //hsetnx 意为 if not exist 则 set
        jedis.hsetnx(userKey,"school","xidian");
        jedis.hsetnx(userKey,"age","19");
        print(19,jedis.hgetAll(userKey));

        //set
        String setKey1 = "setKey1";
        String setKey2 = "setKey2";
        for (int i = 0;i< 10;i++){
            jedis.sadd(setKey1,String.valueOf(i));
            jedis.sadd(setKey2,String.valueOf(i*i));
        }
        print(20,jedis.smembers(setKey1));
        print(21,jedis.smembers(setKey2));
        //并
        print(22,jedis.sunion(setKey1,setKey2));
        //前者的异
        print(23,jedis.sdiff(setKey1,setKey2));
        //交
        print(24,jedis.sinter(setKey1,setKey2));
        print(25,jedis.sismember(setKey1,"18"));
        print(26,jedis.srem(setKey2,"64"));
        print(27,jedis.smembers(setKey2));
        jedis.smove(setKey2,setKey1,"25");
        print(28,jedis.smembers(setKey1));
        print(29,jedis.smembers(setKey2));
        print(30,jedis.scard(setKey1));

        //zet
        String rankKey = "rankKey";
        jedis.zadd(rankKey,15,"A");
        jedis.zadd(rankKey,25,"B");
        jedis.zadd(rankKey,35,"C");
        jedis.zadd(rankKey,55,"D");
        jedis.zadd(rankKey,75,"E");

        print(31,jedis.zcard(rankKey));
        print(32,jedis.zcount(rankKey,30,60));
        print(33,jedis.zscore(rankKey,"D"));
        jedis.zincrby(rankKey,30,"D");
        print(33,jedis.zscore(rankKey,"D"));
        print(34,jedis.zcount(rankKey,30,60));
        print(35,jedis.zrange(rankKey,0,-1));
        for (Tuple tuple:jedis.zrangeWithScores(rankKey,0,-1)){
            print(36,tuple.getElement() + tuple.getScore());
        }

        String setKey = "zset";
        jedis.zadd(setKey,1,"b");
        jedis.zadd(setKey,1,"a");
        jedis.zadd(setKey,1,"c");
        jedis.zadd(setKey,1,"d");
        jedis.zadd(setKey,1,"e");

        print(37,jedis.zrange(setKey,0,-1));
        //jedis.zrem(setKey,"c");
        print(38,jedis.zlexcount(setKey,"[b","[d"));

        //连接池
        //默认连接只有8个
        JedisPool pool = new JedisPool();
//        for (int i=0;i<10;i++){
//            Jedis j = pool.getResource();
//            print(39,j.get("pv"));
//            j.close();
//        }

        User user = new User();
        user.setName("xx");
        user.setPassword("fuzhihang");
        user.setHeadUrl("b.png");
        user.setId(12);
        print(40,JSONObject.toJSONString(user));
        jedis.set("user1",JSONObject.toJSONString(user));

        String value = jedis.get("user1");
        User user2 = JSON.parseObject(value,User.class);
        print(41,value);
        print(42,user2.getName());

    }
}
