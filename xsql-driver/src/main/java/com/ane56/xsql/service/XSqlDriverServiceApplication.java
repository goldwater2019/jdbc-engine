package com.ane56.xsql.service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/25 1:22 PM
 * @Desc:
 * @Version: v1.0
 */


@SpringBootApplication
@EnableDubbo
public class XSqlDriverServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(XSqlDriverServiceApplication.class, args);
    }
}
