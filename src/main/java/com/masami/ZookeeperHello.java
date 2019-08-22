package com.masami;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

/**
 * zookeeper 节点的新增，修改，删除
 * Date:2019/8/17
 * Author:gyc
 * Desc:
 */
public class ZookeeperHello {

    public static final String CONNECT_STR = "192.168.3.14:2181";

    public  CuratorFramework curatorFramework = null;

    @Before
    public  void before() {
        CuratorFramework tmpCuratorFramework = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STR).sessionTimeoutMs(1000).retryPolicy(new ExponentialBackoffRetry(1000, 1))
                .namespace("curator")
                .build();
        tmpCuratorFramework.start();
        this.curatorFramework = tmpCuratorFramework;

    }

    /**
     * 创建一个永久的节点
     * @throws Exception
     */
    @Test
    public   void createNode() throws Exception {
         curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .forPath("/newNode","text".getBytes());
    }


    /**
     * 修改一个节点
     */
    @Test
    public  void udpateNode() throws Exception {
        curatorFramework.setData().forPath("/newNode","value".getBytes());
    }


    /**
     * 获取一个节点信息
     */
    @Test
    public  void getNodeData() throws Exception {
        byte[] bytes = curatorFramework.getData().forPath("/newNode");

        String value = new String(bytes);
        System.out.println(value);
    }


    /**
     * 删除节点
     * @throws Exception
     */
    @Test
    public  void deleteNode() throws Exception {
        curatorFramework.delete().forPath("/newNode");

    }


    /**
     * 测试一个节点是否存在

     * @throws Exception
     */
    @Test
    public  void checkNodeExist() throws Exception {
        Stat stat = curatorFramework.checkExists().forPath("/newNode"); //stat 返回空代表不存在
        System.out.println(stat);
    }


    /**
     * 创建一个临时节点
     */
    @Test
    public  void createTempNode() throws Exception {
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/tmpNode","tmpNode".getBytes());
    }

}
