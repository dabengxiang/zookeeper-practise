package com.masami;

import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Date:2019/8/18
 * Author:gyc
 * Desc:
 */
public class ZookeeperAcl {

    public static final String CONNECT_STR = "192.168.3.14:2181";

    public static CuratorFramework curatorFramework = null;


    @Before
    public void before(){

        AuthInfo authInfo= new
                AuthInfo( "digest", "u1:us".getBytes());
        List<AuthInfo> authInfos= new ArrayList<>();
        authInfos.add(authInfo);
        CuratorFramework tmpcuratorFramework=
                CuratorFrameworkFactory. builder ().
                        connectString( "192.168.3.14:2181").sessionTimeoutMs(5000).
                        retryPolicy( new
                                ExponentialBackoffRetry(1000,3)).authorization(authInfos).
                        namespace( "curator").build();
        tmpcuratorFramework.start();

        curatorFramework = tmpcuratorFramework;
    }


    /**
     * 权限创建
     */
    @Test
    public void setAccess() throws Exception {
        List<ACL> acls= new ArrayList<>();
        Id id1= new Id( "digest",
                DigestAuthenticationProvider.generateDigest ( "u1:us"));
        Id id2= new Id( "digest",
                DigestAuthenticationProvider.generateDigest ( "u2:us"));
        acls.add( new ACL(ZooDefs.Perms. ALL ,id1)); //针对
        acls.add( new ACL(ZooDefs.Perms. DELETE  |
                ZooDefs.Perms. READ ,id2));
        curatorFramework.create().creatingParentsIfNeeded(
        ).withMode(CreateMode. PERSISTENT ).
                withACL(acls, false).forPath( "/auth", "sc".getBytes(
        ));
    }


   /**
     * 带上权限访问
     */

    @Test
    public void access() throws Exception {

        curatorFramework.getData().forPath("/tmp1");



    }




    @Test
     public void generateDigest()
            throws NoSuchAlgorithmException {
        String s = DigestAuthenticationProvider.generateDigest("u1:us");
        System.out.println(s);
    }







}
