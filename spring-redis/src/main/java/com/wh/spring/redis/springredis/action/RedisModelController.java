package com.wh.spring.redis.springredis.action;

import com.wh.spring.redis.springredis.service.RedisClientTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:wuhao
 * @description:Controller入口类
 * @date:2019/12/26
 */
@RestController
@RequestMapping("/redisModel")
@Slf4j
public class RedisModelController {

    @Autowired
   private RedisClientTemplate redisClientTemplate;

    @GetMapping(value = "/redisOk")
    public Object testSet(){
        redisClientTemplate.setToRedis("7004","7004测试RedisCluster");
        System.out.println(redisClientTemplate.getRedis("7004"));
        return null;
    }

    @GetMapping(value = "/getValue")
    public String getRedisValue(){
        String value= (String) redisClientTemplate.getRedis("Hulk");
        log.info("Redis获取的值是:{}",value);
        return value;
    }
}
