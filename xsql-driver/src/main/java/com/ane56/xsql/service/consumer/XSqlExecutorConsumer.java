package com.ane56.xsql.service.consumer;

import com.ane56.xsql.common.api.XSqlExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 1:44 AM
 * @Desc:
 * @Version: v1.0
 */

@Component("xSqlExecutorConsumer")
public class XSqlExecutorConsumer {
    @Autowired
    private XSqlExecutorService xSqlExecutorService;

    public List<String> getCatalogs() {
        return xSqlExecutorService.showCatalogs();
    }
}
