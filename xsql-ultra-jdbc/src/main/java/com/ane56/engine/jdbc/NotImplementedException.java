package com.ane56.engine.jdbc;

import java.sql.SQLNonTransientException;

import static java.lang.String.format;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 11:57 AM
 * @Desc:
 * @Version: v1.0
 */

public class NotImplementedException
        extends SQLNonTransientException
{
    public NotImplementedException(String reason)
    {
        super(reason);
    }

    public NotImplementedException(String clazz, String method)
    {
        this(format("Method %s.%s is not yet implemented", clazz, method));
    }
}
