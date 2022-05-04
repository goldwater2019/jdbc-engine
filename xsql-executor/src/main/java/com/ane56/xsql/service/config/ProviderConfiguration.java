package com.ane56.xsql.service.config;

import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 1:33 AM
 * @Desc:
 * @Version: v1.0
 */

@Configuration
@EnableDubbo(scanBasePackages = "com.ane56.xsql.service.provider")
//@PropertySource("classpath:/dubbo-provider.properties")
//@PropertySource("classpath:/bootstrap.yml")
public class ProviderConfiguration {
    @Bean
    public ProviderConfig providerConfig() {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(1000);
        return providerConfig;
    }
}
