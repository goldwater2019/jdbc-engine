package com.ane56.engine.jdbc.common;

import java.net.URI;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 4:11 PM
 * @Desc:
 * @Version: v1.0
 */

public interface QueryStatusInfo {
    String getId();

    URI getInfoUri();

    URI getPartialCancelUri();

    URI getNextUri();

    List<Column> getColumns();

    StatementStats getStats();

    QueryError getError();

    String getUpdateType();

    Long getUpdateCount();
}
