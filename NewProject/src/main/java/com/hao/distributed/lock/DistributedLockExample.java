package com.hao.distributed.lock;

import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * redis实现分布式锁
 */
public class DistributedLockExample {
    private static final String LOCK_KEY = "lock_key";
    private static final long LOCK_EXPIRE = 30000; //锁过期时间

    private Jedis jedis;

    public DistributedLockExample() {
        this.jedis = new Jedis("localhost");
    }

    // 尝试获取分布式锁
    public String tryLock() {
        // 生成唯一锁值
        String lockValue = UUID.randomUUID().toString();
        long currentTime = System.currentTimeMillis();
        long expireTime = currentTime + LOCK_EXPIRE;

        // 使用 SETNX 命令设置锁
        if (jedis.setnx(LOCK_KEY, lockValue) == 1) {
            jedis.expire(LOCK_KEY, LOCK_EXPIRE);
            return lockValue;
        }

        // 检查锁是否过期
        String currentLockValue = jedis.get(LOCK_KEY);
        if (currentLockValue != null && Long.parseLong(currentLockValue) < currentTime) {
            // 如果当前锁值过期 则获取锁
            String oldLockValue = jedis.getSet(LOCK_KEY, lockValue);
            if (oldLockValue != null && oldLockValue.equals(currentLockValue)) {
                // 刷新过期时间
                jedis.expire(LOCK_KEY, LOCK_EXPIRE);
                return lockValue;
            }
        }
        return null;
    }

    // 释放分布式锁
    public void releaseLock(String lockValue) {
        if (lockValue.equals(jedis.get(LOCK_KEY))) {
            jedis.del(LOCK_KEY);
        }
    }

    // 消费请求的逻辑
    public void consumeRequest(int requestId) {
        String lockValue = tryLock();
        if (lockValue != null) {
            try {
                System.out.println("Processing request: " + requestId);
                Thread.sleep(1000);
                System.out.println("Finished Processing request: " + requestId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                //确保释放锁
                releaseLock(lockValue);
            }
        } else {
            System.out.println("Unable to acquire lock for request: " + requestId);
        }
    }

    public static void main(String[] args) {
        DistributedLockExample example = new DistributedLockExample();
        // 模拟多个请求
        for (int i = 0; i < 5; i++) {
            final int requestId = i;
            new Thread(() -> example.consumeRequest(requestId)).start();
        }
    }
}
