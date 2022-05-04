package com.ane56.xsql.service.consumer;

import com.ane56.xsql.common.api.XSqlDriverService;
import com.ane56.xsql.common.model.UltraCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 1:17 AM
 * @Desc:
 * @Version: v1.0
 */

@Component("xSqlDriverConsumer")
public class XSqlDriverConsumer {
    @Autowired
    private XSqlDriverService xSqlDriverService;

    public String echo() {
        return xSqlDriverService.sayHello("ALIBABA");
    }


    /**
     * 获得所有可用并且未被禁用的catalog
     * @return
     */
    public List<UltraCatalog> getAvailableAndUnForbiddenCatalogs() {
        return xSqlDriverService.getAllCatalogsWithFilter(false, true);
    }
}
