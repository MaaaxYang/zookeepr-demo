package com.bodhi.zookeeper.zkdemo.samples;

import com.bodhi.zookeeper.zkdemo.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: zk-demo
 * @description:
 * @author: Maxxx.Yg
 * @create: 2018-11-30 15:06
 **/
@RestController
public class CrudSample {

    @Autowired
    private CrudService crudService;

    @GetMapping("/create/node")
    public boolean createNode() throws Exception {
        crudService.create("/bodhi/samples/nodeA");
        return true;
    }

    @GetMapping("/update/{value}")
    public boolean updateNode(@PathVariable("value") String value) throws Exception {
        crudService.update("/bodhi/samples/nodeA",value);
        return true;
    }


    @GetMapping("/delete/node")
    public boolean deleteNode() throws Exception {
        crudService.delete("/bodhi/samples/nodeA");
        return true;
    }

    @GetMapping("/get/node")
    public String getNode() throws Exception {
        return crudService.get("/bodhi/samples/nodeA",String.class);
    }
}
