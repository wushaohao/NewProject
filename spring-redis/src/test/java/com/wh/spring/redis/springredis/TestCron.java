package com.wh.spring.redis.springredis;

import org.junit.Assert;
import org.junit.Test;
import org.quartz.CronExpression;

import java.text.SimpleDateFormat;

public class TestCron {
    @Test
    public void cron() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CronExpression cronExpression = new CronExpression("0 0/5 8-19 * * ? *");
        boolean resCron = cronExpression.isSatisfiedBy(simpleDateFormat.parse("2019-09-09 19:55:00"));
        Assert.assertTrue(resCron);
    }
}