package com.wayzim.zookeeper.lock.stock;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-09-21 18:49
 */
public class Stock {

    private static int stockNum = 1;

    public boolean reduceStock() {
        if (stockNum > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stockNum--;
            return true;
        } else {
            System.err.println("========库存没了========");
            return false;
        }
    }

}
