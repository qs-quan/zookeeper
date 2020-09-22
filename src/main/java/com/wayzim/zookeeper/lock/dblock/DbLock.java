package com.wayzim.zookeeper.lock.dblock;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wayzim.zookeeper.lock.entity.LockRecord;
import com.wayzim.zookeeper.lock.mapper.LockRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-09-21 14:50
 */
@Component
public class DbLock implements Lock {

    @Autowired
    LockRecordMapper lockRecordMapper;

    public static final String LOCK_NAME = "db_lock_stock";

    public void lock() {
        while (true) {
            boolean isLock = tryLock();
            System.out.println("==========" + Thread.currentThread().getName() + "isLock:" + isLock + "==============");
            if (isLock) {
                LockRecord lockRecord = new LockRecord();
                lockRecord.setLockName(LOCK_NAME);
                lockRecordMapper.insert(lockRecord);
                return;
            } else {
                System.out.println("等待中。。。。。");
            }
        }

    }

    public void lockInterruptibly() throws InterruptedException {

    }

    /**
     * 尝试获取锁根据指定名称在数据库中发起一次查询
     * sql: select * from lock_record where lock_name = 'db_lock_stock'
     *
     * @return
     */
    public boolean tryLock() {
        LockRecord lockRecord = lockRecordMapper.selectOne(new QueryWrapper<LockRecord>().eq("lock_name", LOCK_NAME));
        return lockRecord == null ? true : false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    /**
     * 删除指定名称的记录
     */
    public void unlock() {
        lockRecordMapper.delete(new QueryWrapper<LockRecord>().eq("lock_name", LOCK_NAME));
    }

    public Condition newCondition() {
        return null;
    }
}
