package com.bodhi.zookeeper.zkdemo.samples;

import com.bodhi.zookeeper.zkdemo.services.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: zk-demo
 * @description:
 * @author: Maxxx.Yg
 * @create: 2018-11-30 15:16
 **/
@RestController
public class WatchSample {

    @Autowired
    private WatchService watchService;

    @GetMapping("/watch")
    public String addWatch() throws Exception {
        watchService.treeCache("/bodhi/samples/nodeA");
        return "ok";
    }

    @GetMapping("/watch2")
    public String addWatch2() throws Exception {
        watchService.pathChildCache("/bodhi/samples/nodeA");
        return "ok";
    }


    @GetMapping("/watch3")
    public String addWatct3() throws Exception {
        watchService.nodeCache("/bodhi/samples/nodeA");
        return "ok";
    }

}
