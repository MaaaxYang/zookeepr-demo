package com.bodhi.zookeeper.zkdemo.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @program: zk-demo
 * @description:
 * @author: Maxxx.Yg
 * @create: 2018-11-30 14:49
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ZookeeperAutoConfiguration.class)
public @interface EnableZookeeper {

}
