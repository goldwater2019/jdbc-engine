package com.ane56.xsql.service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 1:39 AM
 * @Desc:
 * @Version: v1.0
 */

@SpringBootApplication
@EnableDubbo
public class XSqlExexutorServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(XSqlExexutorServiceApplication.class, args);
    }
}
