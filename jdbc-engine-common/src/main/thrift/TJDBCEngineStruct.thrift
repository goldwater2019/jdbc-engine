namespace java com.ane56.engine.jdbc.thrit.struct

struct TJDBCEngineExecutor {
    1: string host,
    2: i32 port,
    3: string prefix,
    4: string executorRefId
}

struct TJDBCCatalog {
    1: string name,
    2: string driver,
    3: string url,
    4: string username,
    5: string password
}