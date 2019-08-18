package com.masami;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * zookeeper 节点的新增，修改，删除
 * Date:2019/8/17
 * Author:gyc
 * Desc:
 */
public class ZookeeperHello {

    public static final String CONNECT_STR = "192.168.1.104:2181";
    public static void main(String[] args) throws Exception {

        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_STR).sessionTimeoutMs(1000).retryPolicy(new ExponentialBackoffRetry(1000, 1))
                .build();

        curatorFramework.start();

//        createNode(curatorFramework);
//        udpateNode(curatorFramework);
//        getNodeData(curatorFramework);
//        deleteNode(curatorFramework);\

        createTempNode(curatorFramework);
        checkNodeExist(curatorFramework,"/tmpNode");


    }


    /**
     * 创建一个永久的节点
     * @param curatorFramework
     * @throws Exception
     */
    public static  void createNode(CuratorFramework curatorFramework) throws Exception {
         curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .forPath("/newNode","text".getBytes());
    }


    /**
     * 修改一个节点
     * @param curatorFramework
     */
    public static void udpateNode(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.setData().forPath("/newNode","value".getBytes());
    }


    /**
     * 获取一个节点信息
     * @param curatorFramework
     */
    public static void getNodeData(CuratorFramework curatorFramework) throws Exception {
        byte[] bytes = curatorFramework.getData().forPath("/newNode");

        String value = new String(bytes);
        System.out.println(value);
    }


    /**
     * 删除节点
     * @param curatorFramework
     * @throws Exception
     */
    public static void deleteNode(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.delete().forPath("/newNode");

    }


    public static void checkNodeExist(CuratorFramework curatorFramework,String nodeName) throws Exception {
        Stat stat = curatorFramework.checkExists().forPath(nodeName); //stat 返回空代表不存在
        System.out.println(stat);
    }


    /**
     * 创建一个临时节点
     */
    public static void createTempNode(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/tmpNode","tmpNode".getBytes());

    }






}
