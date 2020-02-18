package com.zhh_fu.mynowcoder.Util;

import com.zhh_fu.mynowcoder.controller.QuestionController;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;

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

    public long lpush(String key,String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.lpush(key,value);
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

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        }
        catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public Jedis getJedis(){
        return pool.getResource();
    }

    public Transaction multi(Jedis jedis){
        try{
            return jedis.multi();
        }
        catch (Exception ex){
            logger.error("发生异常" + ex.getMessage());
        }
        return null;
    }

    public List<Object> exec(Transaction tx, Jedis jedis){
        try{
            return tx.exec();
        }
        catch (Exception ex){
            logger.error("发生异常" + ex.getMessage());
            //事务回滚很重要
            tx.discard();
        }
        finally {
            if (tx != null){
                try{
                    tx.close();
                }
                catch(Exception ex){
                    logger.error("发生异常" + ex.getMessage());
                }
            }

            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    public long zadd(String key, double score, String value){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zadd(key,score,value);
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

    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key ,String member){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
