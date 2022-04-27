package com.ane56.xsql.common.enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 4:19 PM
 * @Desc:
 * @Version: v1.0
 */

public enum UltraColumnType {
    BIT(-7),
    TINYINT(-6),
    SMALLINT(5),
    INTEGER(4),
    BIGINT(-5),
    FLOAT(6),
    REAL(7),
    DOUBLE(8),
    NUMERIC(2),
    DECIMAL(3),
    CHAR(1),
    VARCHAR(12),
    LONGVARCHAR(-1),
    DATE(91),
    TIME(92),
    TIMESTAMP(93),
    BINARY(-2),
    VARBINARY(-3),
    LONGVARBINARY(-4),
    NULL(0),
    STRUCT(2002),
    ARRAY(2003),
    BOOLEAN(16),
    LONGNVARCHAR(-16),
    OTHER(1111);

    private static final Map<Integer, UltraColumnType> value2ultraColumnType = new HashMap<>();

    static {
        value2ultraColumnType.put(-7, BIT);
        value2ultraColumnType.put(-6, TINYINT);
        value2ultraColumnType.put(5, SMALLINT);
        value2ultraColumnType.put(4, INTEGER);
        value2ultraColumnType.put(-5, BIGINT);
        value2ultraColumnType.put(6, FLOAT);
        value2ultraColumnType.put(7, REAL);
        value2ultraColumnType.put(8, DOUBLE);
        value2ultraColumnType.put(2, NUMERIC);
        value2ultraColumnType.put(3, DECIMAL);
        value2ultraColumnType.put(1, CHAR);
        value2ultraColumnType.put(12, VARCHAR);
        value2ultraColumnType.put(-1, LONGVARCHAR);
        value2ultraColumnType.put(91, DATE);
        value2ultraColumnType.put(92, TIME);
        value2ultraColumnType.put(93, TIMESTAMP);
        value2ultraColumnType.put(-2, BINARY);
        value2ultraColumnType.put(-3, VARBINARY);
        value2ultraColumnType.put(-4, LONGVARBINARY);
        value2ultraColumnType.put(0, NULL);
        value2ultraColumnType.put(2002, STRUCT);
        value2ultraColumnType.put(2003, ARRAY);
        value2ultraColumnType.put(16, BOOLEAN);
        value2ultraColumnType.put(-16, LONGNVARCHAR);
        value2ultraColumnType.put(1111, OTHER);
    }

    private final int value;

    UltraColumnType(int value) {
        this.value = value;
    }

    public static UltraColumnType findByValue(int value) {
        return value2ultraColumnType.getOrDefault(value, UltraColumnType.OTHER);
    }
}
