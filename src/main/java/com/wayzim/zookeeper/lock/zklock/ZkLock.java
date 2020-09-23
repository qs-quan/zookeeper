package com.wayzim.zookeeper.lock.zklock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-09-22 13:38
 */
@Component
public class ZkLock implements Lock {

    public static final String LOCK_NAME = "ZK_LOCK";

    private ThreadLocal<String> nodeId = new ThreadLocal<>();


    @Autowired
    ZooKeeper zooKeeper;

    @Value("${zookeeper.lock.root}")
    private String root;


    /**
     * watcher 监听临时顺序节点的删除
     */
    class LockWatcher implements Watcher {

        private CountDownLatch latch = null;


        public LockWatcher(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.NodeDeleted) {
                latch.countDown();
            }
        }
    }


    @Override
    public void lock() {

        // 创建临时子节点
        try {
            String myNode = zooKeeper.create(root + "/" + LOCK_NAME, null,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            System.out.println(Thread.currentThread().getName() + myNode + "created。。。。。。。。。");

            if (isSmallNode(myNode)) {
                //如果是最小节点 表示获得锁
                System.out.println(Thread.currentThread().getName() + myNode + "获取锁资源");
                this.nodeId.set(myNode);
                return;
            }
            // 监听上一个节点
            watchPreNode(myNode);

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void watchPreNode(String myNode) throws KeeperException, InterruptedException {
        String preNode = getSortNodes().lower(myNode);

        CountDownLatch latch = new CountDownLatch(1);
        // 注册监听
        Stat stat = zooKeeper.exists(preNode, new LockWatcher(latch));
        // 判断比自己小的节点是否存在，如果不存在则无需等待，同时注册监听
        if (stat != null) {
            System.out.println(Thread.currentThread().getName() + myNode + "等待节点： " + preNode + "释放锁资源");
            //等待，这里一直等待其他线程释放锁
            latch.await();
            // 判断当前节点是不是最小的节点 防止为其他原因导致的节点删除
            if (isSmallNode(myNode)) {
                System.out.println(Thread.currentThread().getName() + myNode + "获取锁资源");
                nodeId.set(myNode);
            } else {
                watchPreNode(myNode);
            }
        }
    }


    private Boolean isSmallNode(String currentNode) {
        String smallestNode = "";
        try {
            TreeSet<String> sortNodes = getSortNodes();
            smallestNode = sortNodes.first();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return currentNode.equals(smallestNode);
    }

    private TreeSet<String> getSortNodes() throws KeeperException, InterruptedException {
        // 取出所有子节点
        List<String> children = zooKeeper.getChildren(root, false);

        TreeSet<String> sortNodes = new TreeSet<>();

        for (String child : children) {
            sortNodes.add(root + "/" + child);
        }
        return sortNodes;
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

        try {
            System.out.println(Thread.currentThread().getName() + "unlock");

            if (nodeId.get() != null) {
                zooKeeper.delete(nodeId.get(), -1);
                nodeId.remove();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
