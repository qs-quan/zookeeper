package com.wayzim.zookeeper.lock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-09-21 15:36
 */
@SpringBootApplication
@MapperScan(basePackages = "com.wayzim.zookeeper.lock.mapper")
public class LockApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(LockApplication.class, args);
    }
}
