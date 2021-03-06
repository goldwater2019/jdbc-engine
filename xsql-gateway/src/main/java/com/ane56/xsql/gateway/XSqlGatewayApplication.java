package com.ane56.xsql.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 10:12 AM
 * @Desc:
 * @Version: v1.0
 */

@SpringBootApplication
@EnableDiscoveryClient
public class XSqlGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(XSqlGatewayApplication.class, args);
    }
}
