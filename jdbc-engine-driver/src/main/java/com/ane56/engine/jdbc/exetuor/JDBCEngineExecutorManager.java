package com.ane56.engine.jdbc.exetuor;

import com.ane56.engine.jdbc.catalog.JDBCCatalog;
import com.ane56.engine.jdbc.thrift.struct.TJDBCCatalog;
import com.ane56.engine.jdbc.thrift.struct.TJDBCEngineExecutor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JDBCEngineExecutorManager {

    private static volatile JDBCEngineExecutorManager singleton;
    private JDBCEngineExecutorManager() {};
    public static JDBCEngineExecutorManager getInstance() {
        if (singleton == null) {
            synchronized (JDBCEngineExecutorManager.class) {
                if (singleton == null) {
                    singleton = new JDBCEngineExecutorManager();
                }
            }
        }
        return singleton;
    }

    private Map<UUID, JDBCEngineExecutorRef> uuid2jdbcEngineExecutorRefs = new ConcurrentHashMap<>();

    /**
     * 添加相应的executor
     * @param jdbcEngineExecutorRef
     * @return
     */
    public boolean upsertJDBCEngineExecutorRef(JDBCEngineExecutorRef jdbcEngineExecutorRef) {
        uuid2jdbcEngineExecutorRefs.put(jdbcEngineExecutorRef.getExecutorRefId(), jdbcEngineExecutorRef);
        jdbcEngineExecutorRef.setLatestHeartbeatTime(System.currentTimeMillis());
        return true;
    }

    /**
     * 处理心跳
     * @param tjdbcEngineExecutor
     * @return
     */
    public boolean heartbeat(TJDBCEngineExecutor tjdbcEngineExecutor) {
        return upsertJDBCEngineExecutorRef(JDBCEngineExecutorRef.parseFromTJDBCEngineDriver(tjdbcEngineExecutor));
    }

}
