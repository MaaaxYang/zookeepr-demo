package com.bodhi.zookeeper.zkdemo.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @program: zk-demo
 * @description:
 * @author: Maxxx.Yg
 * @create: 2018-11-30 16:20
 **/
@Service
@Slf4j
public class LockSerivce {

    @Autowired
    private CuratorFramework client;

    public void demo(String path) throws Exception {

        /**
         * https://www.jianshu.com/p/6618471f6e75
         *
         * InterProcessMutex：分布式可重入排它锁
         * InterProcessSemaphoreMutex：分布式排它锁
         * InterProcessReadWriteLock：分布式读写锁
         * InterProcessMultiLock：将多个锁作为单个实体管理的容器
         */

        InterProcessMutex lock = new InterProcessMutex(client,path);
        try{
            if(lock.acquire(10,TimeUnit.SECONDS)){
                log.info("lock start");
                Thread.sleep(1000*5);
            }
        }catch (Exception e){
            log.error("lock error",e);
        }finally {
            lock.release();
            log.info("lock end");
        }
    }

}
