package com.wh.spring.redis.springredis.service;

import com.wh.spring.redis.springredis.util.JedisClusterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:wuhao
 * @description:Redis客户端模版
 * @date:2019/12/26
 */
@Service
@Slf4j
public class RedisClientTemplate {
    @Autowired
    private JedisClusterConfig jedisClusterConfig;

    /**
     * 设置值
     * @param key
     * @param value
     * @return
     */
    public boolean setToRedis(String key,Object value){
        try {
            String str=jedisClusterConfig.getJedisCluster().set(key, String.valueOf(value));
            if("OK".equals(str)){
                return true;
            }
        }catch (Exception ex){
            log.error("setToRedis:{Key:"+key+",value"+value+"}",ex);
        }
        return false;
    }

    /**
     * 获取值
     * @param key
     * @return
     */
    public Object getRedis(String key){
        String str=null;
        try {
            str=jedisClusterConfig.getJedisCluster().get(key);
        }catch (Exception ex){
            log.error("getRedis:{Key:"+key+"}",ex);
        }
        return str;
    }
}
