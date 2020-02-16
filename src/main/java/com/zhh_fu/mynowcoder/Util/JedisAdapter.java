package com.zhh_fu.mynowcoder.Util;

import com.zhh_fu.mynowcoder.controller.QuestionController;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class JedisAdapter implements InitializingBean {
    private JedisPool pool;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JedisAdapter.class);


    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    public long sadd(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }
        catch (Exception ex){
            logger.error("发生异常" + ex.getMessage());
        }
        finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }
        catch (Exception ex){
            logger.error("发生异常" + ex.getMessage());
        }
        finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.scard(key);
        }
        catch (Exception ex){
            logger.error("发生异常" + ex.getMessage());
        }
        finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key,String member){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.sismember(key, member);
        }
        catch (Exception ex){
            logger.error("发生异常" + ex.getMessage());
        }
        finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return false;
    }
}
