package lock;

import com.wayzim.zookeeper.lock.LockApplication;
import com.wayzim.zookeeper.lock.stock.Stock;
import com.wayzim.zookeeper.lock.zklock.ZkLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-09-22 16:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LockApplication.class)
public class ZkLockTest {

    @Autowired
    private ZkLock zkLock;


    class StockThread implements Runnable {
        @Override
        public void run() {
            // 上锁
            zkLock.lock();
            // 减少库存
            boolean b = new Stock().reduceStock();
            // 解锁
            zkLock.unlock();

            if (b) {
                System.err.println(Thread.currentThread().getName() + "减库存成功。。。。。。");
            } else {
                System.err.println(Thread.currentThread().getName() + "减库存失败。。。。。。");
            }
        }
    }

    @Test
    public void stockTest() throws InterruptedException {


        for (int i = 0; i < 100; i++) {
            new Thread(new StockThread(), "t" + i).start();
        }
        Thread.sleep(60000);

    }
}
