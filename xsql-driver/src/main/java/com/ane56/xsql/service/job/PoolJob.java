package com.ane56.xsql.service.job;

import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.service.consumer.XSqlExecutorConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/27 3:48 PM
 * @Desc:
 * @Version: v1.0
 */

@Component
@Slf4j
public class PoolJob {
    @Autowired
    private XSqlExecutorConsumer xSqlExecutorConsumer;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    public void ping() throws SQLException, XSQLException {
        long startTime = System.currentTimeMillis();
        xSqlExecutorConsumer.init();
        xSqlExecutorConsumer.ping();
        log.info("scheduled task finished, it elapsed " + (System.currentTimeMillis() - startTime) + " ms");
    }
}
