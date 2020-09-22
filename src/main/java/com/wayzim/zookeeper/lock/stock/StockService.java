package com.wayzim.zookeeper.lock.stock;

import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-09-22 9:39
 */
@Component
public class StockService {

    private Lock lock = new ReentrantLock();


    class StockThread implements Runnable {
        @Override
        public void run() {
            System.out.println("===============" + Thread.currentThread().getName() + "进入" + "===================");
            // 上锁
            lock.lock();
            // 减少库存
            Stock stock = new Stock();
            boolean b = stock.reduceStock();
            // 解锁
            lock.unlock();

            if (b) {
                System.err.println(Thread.currentThread().getName() + "减库存成功。。。。。。");
            } else {
                System.err.println(Thread.currentThread().getName() + "减库存失败。。。。。。");
            }
        }
    }

    @Test
    public void stockTest() throws InterruptedException {

        new Thread(new StockThread(),"t1").start();
        new Thread(new StockThread(),"t2").start();
        new Thread(new StockThread(),"t3").start();
        Thread.sleep(2000);
    }

}
