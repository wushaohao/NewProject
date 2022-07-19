package com.wh.spring.redis.springredis.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author:wuhao
 * @description:Redis配置类
 * @date:2019/12/26
 */
@Configuration
public class RedisConfig {

  @Bean
  @ConfigurationProperties(prefix = "spring.redis.pool")
  public JedisPoolConfig jedisPoolConfig() {
    JedisPoolConfig config = new JedisPoolConfig();

    System.out.println("默认值：" + config.getMaxIdle());
    System.out.println("默认值：" + config.getMinIdle());
    System.out.println("默认值：" + config.getMaxTotal());
    return config;
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.redis")
  public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig config) {
    System.out.println("配置完毕：" + config.getMaxIdle());
    System.out.println("配置完毕：" + config.getMinIdle());
    System.out.println("配置完毕：" + config.getMaxTotal());

    JedisConnectionFactory factory = new JedisConnectionFactory();
    // 关联链接池的配置对象
    factory.setPoolConfig(config);
    // 配置链接Redis的信息
    // 主机地址
    /*factory.setHostName("192.168.70.128");
    //端口
    factory.setPort(6379);*/
    return factory;
  }

  /** 3.创建RedisTemplate:用于执行Redis操作的方法 */
  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      JedisConnectionFactory jedisConnectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    // 关联
    template.setConnectionFactory(jedisConnectionFactory);

    // 为key设置序列化器
    template.setKeySerializer(new StringRedisSerializer());
    // 为value设置序列化器
    template.setValueSerializer(new StringRedisSerializer());

    return template;
  }
}
