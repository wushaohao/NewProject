package com.wh.spring.redis.springredis;

import com.wh.spring.redis.springredis.dto.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author:wuhao
 * @description:测试类
 * @date:2019/12/26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringRedisApplication.class)
public class SpringRedisKeyTest {

  @Autowired private RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加值
     */
  @Test
  public void testSet() {
    this.redisTemplate.opsForValue().set("key", "RedisDemoTest");
  }

  /** 获取一个字符串 */
  @Test
  public void testGet() {
    String value = (String) this.redisTemplate.opsForValue().get("key");
    System.out.println(value);
  }
  /** 添加Users对象 */
  @Test
  public void testSetUesrs() {
    User users = new User();
    users.setAge(20);
    users.setName("张三丰");
    users.setId(1);
    // 重新设置序列化器
    this.redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
    this.redisTemplate.opsForValue().set("users", users);
  }

  /** 取Users对象 */
  @Test
  public void testGetUsers() {
    // 重新设置序列化器
    this.redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
    User users = (User) this.redisTemplate.opsForValue().get("users");
    System.out.println(users);
  }

  /** 基于JSON格式存Users对象 */
  @Test
  public void testSetUsersUseJSON() {
    User users = new User();
    users.setAge(20);
    users.setName("李四丰");
    users.setId(1);
    this.redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
    this.redisTemplate.opsForValue().set("users_json", users);
  }

  /** 基于JSON格式取Users对象 */
  @Test
  public void testGetUseJSON() {
    this.redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
    User users = (User) this.redisTemplate.opsForValue().get("users_json");
    System.out.println(users);
  }
}
