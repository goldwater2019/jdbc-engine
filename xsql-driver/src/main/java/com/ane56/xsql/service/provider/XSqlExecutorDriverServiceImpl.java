package com.ane56.xsql.service.provider;

import com.ane56.xsql.common.api.XSqlDriverService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/25 1:48 PM
 * @Desc:
 * @Version: v1.0
 */

@Slf4j
@DubboService
public class XSqlExecutorDriverServiceImpl implements XSqlDriverService {
    @Override
    public String sayHello(String name) {
        log.info("hello world");
        return "hello " + name;
    }
}
