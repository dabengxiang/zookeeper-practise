package com.masami;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Before;
import org.junit.Test;

/**
 * Date:2019/8/18
 * Author:gyc
 * Desc:
 */
public class ZookeeperWatcher {

    public static final String CONNECT_STR = "192.168.1.104:2181";

    public static CuratorFramework curatorFramework = null;


    @Before
    public void before(){
        CuratorFramework tmpcuratorFramework = CuratorFrameworkFactory.builder().connectString(CONNECT_STR).sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(3000, 3)).build();
        tmpcuratorFramework.start();

        curatorFramework = tmpcuratorFramework;
    }


    /**
     * 监控节点的变化
     */
    @Test
    public void watchNodeChange() throws Exception {

        NodeCache nodeCache = new NodeCache(curatorFramework,"/watch");
        NodeCacheListener nodeCacheListener = ()->{
            System.out.println("node is change");
            if(nodeCache.getCurrentData()==null){
                System.out.println("node be delete");
            }else{
                System.out.println("path: "+ nodeCache.getPath()+"  data: "+ new String(nodeCache.getCurrentData().getData()) );
            }

        };


//        NodeCacheListener nodeCacheListener = new NodeCacheListener() {
//            @Override
//            public void nodeChanged() throws Exception {
//                System.out.println("node is change");
//                System.out.println("path: " + nodeCache.getPath() + "  data: " + new String(nodeCache.getCurrentData().getData()));
//            }
//        };


        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();

        Thread.sleep(30000);


    }




}
