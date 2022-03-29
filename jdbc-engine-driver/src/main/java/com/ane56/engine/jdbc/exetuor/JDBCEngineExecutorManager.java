package com.ane56.engine.jdbc.exetuor;

import com.ane56.engine.jdbc.model.JDBCEngineExecutorRef;
import com.ane56.engine.jdbc.thrit.struct.TJDBCEngineExecutor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Builder
@Data
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
