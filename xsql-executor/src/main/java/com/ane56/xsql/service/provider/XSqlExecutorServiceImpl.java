package com.ane56.xsql.service.provider;

import com.ane56.xsql.common.api.XSqlExecutorService;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 1:36 AM
 * @Desc:
 * @Version: v1.0
 */

@DubboService
public class XSqlExecutorServiceImpl implements XSqlExecutorService {
    @Override
    public List<String> showCatalogs() {
        List<String> result = new LinkedList<>();
        result.add("starrocks");
        result.add("tidb");
        result.add("mysql");
        return result;
    }
}
