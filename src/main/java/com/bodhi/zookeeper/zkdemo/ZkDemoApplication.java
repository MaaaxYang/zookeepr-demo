package com.bodhi.zookeeper.zkdemo;

import com.bodhi.zookeeper.zkdemo.config.EnableZookeeper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZookeeper
public class ZkDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZkDemoApplication.class, args);
    }
}
