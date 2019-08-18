package com.masami;

import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Date:2019/8/18
 * Author:gyc
 * Desc:
 */
public class ZookeeperAcl {

    public static final String CONNECT_STR = "192.168.1.104:2181";

    public static CuratorFramework curatorFramework = null;


    @Before
    public void before(){
        CuratorFramework tmpcuratorFramework = CuratorFrameworkFactory.builder().connectString(CONNECT_STR).sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(3000, 3)).build();
        tmpcuratorFramework.start();

        curatorFramework = tmpcuratorFramework;
    }


    /**
     * 权限创建
     */
    @Test
    public void setAccess() throws Exception {
        List<ACL> list = new ArrayList<>();
//        Id digest = new Id("digest", DigestAuthenticationProvider.generateDigest("u2:us"));
        ACL acl = new ACL(ZooDefs.Perms.ALL | ZooDefs.Perms.READ | ZooDefs.Perms.WRITE ,new Id("digest","u2:us"));
        list.add(acl);
        //创建节点时给权限，如果其他人要做操作的话，要带上权限才能做
        curatorFramework.create().withMode(CreateMode.PERSISTENT).withACL(list).forPath("/tmp5");
//        Stat stat = curatorFramework.setACL().withACL(ZooDefs.Perms.ALL).forPath("/auth");

//        curatorFramework.setACL().withACL(ZooDefs.Ids.CREATOR_ALL_ACL).forPath("/auth");

//        curatorFramework.setACL().withACL();
    }


   /**
     * 带上权限访问
     */

    @Test
    public void access() throws Exception {
        AuthInfo authInfo = new AuthInfo("digest","u2:us".getBytes());
        List<AuthInfo> list = new ArrayList<>();
        CuratorFramework curator = CuratorFrameworkFactory.builder().sessionTimeoutMs(5000).connectString(CONNECT_STR)
                .retryPolicy(new ExponentialBackoffRetry(3000, 3)).authorization("digest","u2:us".getBytes()).build();

        curator.start();
//        curator.setACL().withACL(ZooDefs.Ids.CREATOR_ALL_ACL).forPath("/auth");

//        curator.setData().forPath("/tmp","qwer".getBytes());
        byte[] bytes = curator.getData().forPath("/tmp5");

//        curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/tmp4","value2".getBytes());

        System.out.println(new String(bytes));



    }

}
