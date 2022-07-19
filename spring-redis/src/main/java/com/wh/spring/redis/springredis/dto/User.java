package com.wh.spring.redis.springredis.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:wuhao
 * @description:用户类
 * @date:2019/12/26
 */
@Data
public class User implements Serializable {
    private Integer id;
    private String name;
    private Integer age;

    @Override
    public String toString() {
        return "Users [id=" + id + ", name=" + name + ", age=" + age + "]";
    }
}

