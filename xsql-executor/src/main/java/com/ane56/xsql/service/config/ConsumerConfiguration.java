package com.ane56.xsql.service.config;

import com.ane56.xsql.common.api.XSqlDriverService;
import com.ane56.xsql.common.api.XSqlExecutorService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zhangxinsen
 * @Date: 2022/5/4 3:02 PM
 * @Desc:
 * @Version: v1.0
 */

@Configuration
@EnableDubbo(scanBasePackages = "com.ane56.xsql.service.consumer")
@ComponentScan("com.ane56.xsql.service.consumer")
public class ConsumerConfiguration {
    @DubboReference(check = false)
    private XSqlDriverService xSqlDriverService;
    @DubboReference(check = false)
    private XSqlExecutorService xSqlExecutorService;
}

