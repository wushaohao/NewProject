package com.wh.spring.redis.springredis.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author:wuhao
 * @description:Redis配置信息
 * @date:2019/12/26
 */
@Component
@ConfigurationProperties(prefix = "spring.redis.cache")
@Data
public class RedisProperties {
    private String clusterNodes;
    private int commandTimeOut;
}
