package com.wayzim.zookeeper.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-09-18 13:42
 */
public class ZkApiTest {

    public static final String url = "172.16.89.94:2181";

    public static void main(String[] args) throws Exception {
        //1： 创建zookeeper连接
        ZooKeeper zooKeeper = new ZooKeeper(url, 2000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("触发了" + watchedEvent.getType() + "事件");
            }
        });

        //2： 创建父节点
//        String path = zooKeeper.create("/wayzim", "wms".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        System.out.println(path);

        //3：创建子节点
//        String child = zooKeeper.create("/wayzim/it部门", "20人".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        System.out.println(child);

        //4：获取节点中的值（父节点和子节点）
        byte[] data = zooKeeper.getData("/wayzim", false, null);
        System.out.println(new String(data));
        List<String> children = zooKeeper.getChildren("/wayzim", false);
        for (String s : children) {
            System.out.println(s);
        }


        //5：修改节点值(-1 表示匹配任意版本)
        Stat stat = zooKeeper.setData("/wayzim", "主要做分拣机".getBytes(), -1);
        System.out.println(stat);

        //6：判断某节点是否存在
        Stat exists = zooKeeper.exists("/wayzim", false);
        System.out.println(exists);

        //7：删除节点
        zooKeeper.delete("/wayzim", -1);
    }
}
