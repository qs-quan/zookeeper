package com.wayzim.zookeeper.lock.redislock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-09-22 16:21
 */
@Component
public class RedisLock implements Lock {

    @Autowired
    RedisTemplate redisTemplate;

    public static final String LOCK_NAME = "redis_lock";

    public static final String LOCK_NAME_VALUE = "redis_lock_stock";


    @Override
    public void lock() {
        while (true) {
            Boolean b = redisTemplate.opsForValue().setIfAbsent(LOCK_NAME, LOCK_NAME_VALUE, 3, TimeUnit.SECONDS);
            if (b) {
                return;
            } else {
                System.out.println(Thread.currentThread().getName() + "循环等待。。。。。。。。。。");
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        redisTemplate.delete(LOCK_NAME);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
