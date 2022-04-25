package com.ane56.xsql.service.controller;

import com.ane56.xsql.service.consumer.XSqlDriverConsumer;
import com.ane56.xsql.service.consumer.XSqlExecutorConsumer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 1:20 AM
 * @Desc:
 * @Version: v1.0
 */

@RestController()
@RequestMapping("catalog")
public class CatalogController {
    @Resource
    private XSqlDriverConsumer xSqlDriverConsumer;
    @Resource
    private XSqlExecutorConsumer xSqlExecutorConsumer;

    @GetMapping("echo")
    public String echo() {
        return xSqlDriverConsumer.echo();
    }

    @GetMapping("show")
    public List<String> showCatalogs() {
        return xSqlExecutorConsumer.getCatalogs();
    }

}
