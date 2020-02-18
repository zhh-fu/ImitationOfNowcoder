package com.zhh_fu.mynowcoder.Util;

public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";

    //粉丝
    private static String BIZ_FOLLOWER = "FOLLOWER";
    //关注对象
    private static String BIZ_FOLLOWEE = "FOLLOWEE";

    public static String getLikeKey(int entityId,int entityType){
        return BIZ_LIKE + SPLIT + String.valueOf(entityId) + SPLIT + String.valueOf(entityType);
    }

    public static String getDisLikeKey(int entityId,int entityType){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityId) + SPLIT + String.valueOf(entityType);
    }

    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }

    //一个实体的粉丝数的key
    public static String getFollowerKey(int entityId, int entityType){
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityId) + SPLIT + String.valueOf(entityType);
    }

    //某个用户关注某一类实体的key
    public static String getFolloweeKey(int userId, int entityType){
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }
}
