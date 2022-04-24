package com.ane56.engine.jdbc.common;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 6:09 PM
 * @Desc:
 * @Version: v1.0
 */

public final class StandardTypes
{
    public static final String BIGINT = "bigint";
    public static final String INTEGER = "integer";
    public static final String SMALLINT = "smallint";
    public static final String TINYINT = "tinyint";
    public static final String BOOLEAN = "boolean";
    public static final String DATE = "date";
    public static final String DECIMAL = "decimal";
    public static final String REAL = "real";
    public static final String DOUBLE = "double";
    public static final String HYPER_LOG_LOG = "HyperLogLog";
    public static final String QDIGEST = "qdigest";
    public static final String TDIGEST = "tdigest";
    public static final String P4_HYPER_LOG_LOG = "P4HyperLogLog";
    public static final String INTERVAL_DAY_TO_SECOND = "interval day to second";
    public static final String INTERVAL_YEAR_TO_MONTH = "interval year to month";
    public static final String TIMESTAMP = "timestamp";
    public static final String TIMESTAMP_WITH_TIME_ZONE = "timestamp with time zone";
    public static final String TIME = "time";
    public static final String TIME_WITH_TIME_ZONE = "time with time zone";
    public static final String VARBINARY = "varbinary";
    public static final String VARCHAR = "varchar";
    public static final String CHAR = "char";
    public static final String ROW = "row";
    public static final String ARRAY = "array";
    public static final String MAP = "map";
    public static final String JSON = "json";
    public static final String IPADDRESS = "ipaddress";
    public static final String IPPREFIX = "ipprefix";
    public static final String GEOMETRY = "Geometry";
    public static final String BING_TILE = "BingTile";
    public static final String BIGINT_ENUM = "BigintEnum";
    public static final String VARCHAR_ENUM = "VarcharEnum";
    public static final String DISTINCT_TYPE = "DistinctType";
    public static final String UUID = "uuid";

    private StandardTypes() {}

    public static final Set<String> USER_DEFINED_TYPES = unmodifiableSet(new HashSet<>(asList(BIGINT_ENUM, VARCHAR_ENUM, DISTINCT_TYPE)));

    public static final Set<String> PARAMETRIC_TYPES = unmodifiableSet(new HashSet<>(asList(
            VARCHAR,
            CHAR,
            DECIMAL,
            ROW,
            ARRAY,
            MAP,
            QDIGEST,
            TDIGEST,
            BIGINT_ENUM,
            VARCHAR_ENUM,
            DISTINCT_TYPE)));
}

