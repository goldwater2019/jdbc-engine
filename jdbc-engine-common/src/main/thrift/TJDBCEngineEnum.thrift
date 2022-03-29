namespace java com.ane56.engine.jdbc.thrit.enum

enum TJDBCQueryStatus {
    OK = 0,
    FAILED = 1
}

enum TJDBCColumnType {
    BIT =  -7,
    TINYINT =  -6,
    SMALLINT    =   5,
    INTEGER =   4,
    BIGINT  =  -5,
    FLOAT   =   6,
    REAL    =   7,
    DOUBLE  =   8,
    NUMERIC =   2,
    DECIMAL =   3,
    CHAR    =   1,
    VARCHAR =  12,
    LONGVARCHAR =  -1,
    DATE    =  91,
    TIME    =  92,
    TIMESTAMP   =  93,
    BINARY  =  -2,
    VARBINARY   =  -3,
    LONGVARBINARY   =  -4,
    NULL    =   0,
    STRUCT  = 2002,
    ARRAY   = 2003,
    BOOLEAN = 16,
    LONGNVARCHAR    = -16,
    OTHER   = 1111
}