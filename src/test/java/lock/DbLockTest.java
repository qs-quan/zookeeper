package lock;

import com.wayzim.zookeeper.lock.LockApplication;
import com.wayzim.zookeeper.lock.dblock.DbLock;
import com.wayzim.zookeeper.lock.mapper.LockRecordMapper;
import com.wayzim.zookeeper.lock.stock.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-09-21 18:41
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LockApplication.class)
public class DbLockTest {


    @Autowired
    private DbLock dblock;

    @Autowired
    LockRecordMapper lockRecordMapper;


    class StockThread implements Runnable {
        @Override
        public void run() {
            // 上锁
            dblock.lock();
            // 减少库存
            boolean b = new Stock().reduceStock();
            // 解锁
            dblock.unlock();

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
