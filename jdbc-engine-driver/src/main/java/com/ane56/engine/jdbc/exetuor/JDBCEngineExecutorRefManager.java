package com.ane56.engine.jdbc.exetuor;

import com.ane56.engine.jdbc.model.JDBCEngineExecutorRef;
import com.ane56.engine.jdbc.thrit.struct.TJDBCEngineExecutor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Builder
@Data
public class JDBCEngineExecutorRefManager {

    private static volatile JDBCEngineExecutorRefManager singleton;
    private Map<UUID, JDBCEngineExecutorRef> uuid2jdbcEngineExecutorRefs = new ConcurrentHashMap<>();

    private JDBCEngineExecutorRefManager() {
    }

    public static JDBCEngineExecutorRefManager getInstance() {
        if (singleton == null) {
            synchronized (JDBCEngineExecutorRefManager.class) {
                if (singleton == null) {
                    singleton = new JDBCEngineExecutorRefManager();
                }
            }
        }
        return singleton;
    }

    /**
     * 添加相应的executor
     *
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
     *
     * @param tjdbcEngineExecutor
     * @return
     */
    public boolean heartbeat(TJDBCEngineExecutor tjdbcEngineExecutor) {
        return upsertJDBCEngineExecutorRef(JDBCEngineExecutorRef.parseFromTJDBCEngineDriver(tjdbcEngineExecutor));
    }

    public JDBCEngineExecutorRef getEngineRef(UUID uuid) {
        return uuid2jdbcEngineExecutorRefs.get(uuid);
    }

    /**
     * TODO 添加负载均衡选举
     * 选择一个UUID
     *
     * @return
     */
    public UUID pickupUUID() {
        return randomPickup();
    }

    private UUID randomPickup() {
        int size = uuid2jdbcEngineExecutorRefs.size();
        Random random = new Random();
        int pickupIndex = Math.abs(random.nextInt()) % size;
        int index = 0;
        for (UUID uuid : uuid2jdbcEngineExecutorRefs.keySet()) {
            if (index == pickupIndex) {
                return uuid;
            }
            index += 1;
        }
        return null;
    }


    /**
     * 修改最新的接入时间
     * 开始query的时候和结束query的时候都接入
     *
     * @param jdbcEngineExecutorRef
     */
    public void accessJDBCEngineRef(JDBCEngineExecutorRef jdbcEngineExecutorRef) {
        UUID executorRefId = jdbcEngineExecutorRef.getExecutorRefId();
        JDBCEngineExecutorRef jdbcEngineExecutorRef1 = uuid2jdbcEngineExecutorRefs.get(executorRefId);
        if (jdbcEngineExecutorRef1 == null) {
            // TODO invalid engine
            return;
        }
        jdbcEngineExecutorRef1.setLastAccessTime(System.currentTimeMillis());
    }

}
