package com.bodhi.zookeeper.zkdemo.services;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: zk-demo
 * @description:
 * @author: Maxxx.Yg
 * @create: 2018-11-30 15:27
 **/
@Service
@Slf4j
public class WatchService {


    @Autowired
    private CuratorFramework client;

    @Autowired
    private Gson gson;


    public void addWatch(String path) throws Exception {
        PathChildrenCache watcher = new PathChildrenCache(client,path,true);
        watcher.getListenable().addListener((client1,event)->{
            ChildData childData = event.getData();
            log.info(gson.toJson(childData));
        });
        watcher.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
    }

    public void addWatch2(String path) throws Exception {
        // 只能使用一次的监听
        byte[] bytes = client.getData().usingWatcher(new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info(gson.toJson(event));
            }
        }).forPath(path);
        log.info(new String(bytes));
    }

    public void addWatch3(String path) throws Exception {
        // 只能使用一次的监听
        ExecutorService pool = Executors.newCachedThreadPool();

        CuratorListener listener = new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                log.info("监听器  : "+ event.toString());
            }
        };
        client.getCuratorListenable().addListener(listener,pool);
        client.getData().inBackground().forPath("/two");
        client.getData().inBackground().forPath("/two");
        client.getData().inBackground().forPath("/two");
        client.getData().inBackground().forPath("/two");

    }


    /**
     *  第三种监听器的添加方式: Cache 的三种实现 实践
     *  Path Cache：监视一个路径下1）孩子结点的创建、2）删除，3）以及结点数据的更新。
     *                  产生的事件会传递给注册的PathChildrenCacheListener。
     *  Node Cache：监视一个结点的创建、更新、删除，并将结点的数据缓存在本地。
     *  Tree Cache：Path Cache和Node Cache的“合体”，监视路径下的创建、更新、删除事件，并缓存路径下所有孩子结点的数据。
     */


    //1.path Cache  连接  路径  是否获取数据
    //能监听所有的字节点 且是无限监听的模式 但是 指定目录下节点的子节点不再监听
    public void pathChildCache(String path) throws Exception{
        PathChildrenCache childrenCache = new PathChildrenCache(client, path, true);
        PathChildrenCacheListener childrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                log.info("开始进行事件分析:-----");
                ChildData data = event.getData();
                switch (event.getType()) {
                    case CHILD_ADDED:
                        log.info("CHILD_ADDED : "+ data.getPath() +"  数据:"+ data.getData());
                        break;
                    case CHILD_REMOVED:
                        log.info("CHILD_REMOVED : "+ data.getPath() +"  数据:"+ data.getData());
                        break;
                    case CHILD_UPDATED:
                        log.info("CHILD_UPDATED : "+ data.getPath() +"  数据:"+ data.getData());
                        break;
                    default:
                        break;
                }
            }
        };
        childrenCache.getListenable().addListener(childrenCacheListener);
        log.info("Register zk watcher successfully!");
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
    }

    //2.Node Cache  监控本节点的变化情况   连接 目录 是否压缩
    //监听本节点的变化  节点可以进行修改操作  删除节点后会再次创建(空节点)
    public void nodeCache(String path) throws Exception{
        //设置节点的cache
        final NodeCache nodeCache = new NodeCache(client, path, false);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                log.info("the test node is change and result is :");
                log.info("path : "+nodeCache.getCurrentData().getPath());
                log.info("data : "+new String(nodeCache.getCurrentData().getData()));
                log.info("stat : "+nodeCache.getCurrentData().getStat());
            }
        });
        nodeCache.start();
    }
    //3.Tree Cache
    // 监控 指定节点和节点下的所有的节点的变化--无限监听  可以进行本节点的删除(不在创建)
    public void treeCache(String path) throws Exception{

        //设置节点的cache
        TreeCache treeCache = new TreeCache(client, path);
        //设置监听器和处理过程
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                ChildData data = event.getData();
                if(data !=null){
                    switch (event.getType()) {
                        case NODE_ADDED:
                            log.info("NODE_ADDED : "+ data.getPath() +"  数据:"+ new String(data.getData()));
                            break;
                        case NODE_REMOVED:
                            log.info("NODE_REMOVED : "+ data.getPath() +"  数据:"+ new String(data.getData()));
                            break;
                        case NODE_UPDATED:
                            log.info("NODE_UPDATED : "+ data.getPath() +"  数据:"+ new String(data.getData()));
                            break;

                        default:
                            break;
                    }
                }else{
                    log.info( "data is null : "+ event.getType());
                }
            }
        });
        //开始监听
        treeCache.start();

    }
}
