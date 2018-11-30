package com.bodhi.zookeeper.zkdemo.services;

import com.bodhi.zookeeper.zkdemo.utils.ByteArrayUtils;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @program: zk-demo
 * @description:
 * @author: Maxxx.Yg
 * @create: 2018-11-30 14:53
 **/
@Service
public class CrudService {

    @Autowired
    private CuratorFramework client;

    public void create(String path) throws Exception {
        client.create().creatingParentsIfNeeded().forPath(path);
    }

    public <T> void update(String path,T obj) throws Exception {
        client.setData().forPath(path,ByteArrayUtils.objectToBytes(obj).orElse(null));
    }

    public void delete(String path) throws Exception {
        client.delete().forPath(path);
    }

    public <T> T get(String path,Class<T> tClass) throws Exception {
        byte[] bytes = client.getData().forPath(path);
        Optional<T> res = ByteArrayUtils.bytesToObject(bytes);
        return res.orElse(null);
    }

}
