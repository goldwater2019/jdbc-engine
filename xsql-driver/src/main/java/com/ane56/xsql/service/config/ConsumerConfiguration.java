package com.ane56.xsql.service.config;

import com.ane56.xsql.common.api.XSqlDriverService;
import com.ane56.xsql.common.api.XSqlExecutorService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 1:21 AM
 * @Desc:
 * @Version: v1.0
 */

@Configuration
@EnableDubbo(scanBasePackages = "com.ane56.xsql.service.consumer")
//@PropertySource("classpath:/dubbo-consumer.properties")
@ComponentScan("com.ane56.xsql.service.consumer")
public class ConsumerConfiguration {
    @DubboReference(check = false)
    private XSqlDriverService xSqlDriverService;
    @DubboReference(check = false)
    private XSqlExecutorService xSqlExecutorService;
}
