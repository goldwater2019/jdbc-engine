package com.ane56.engine.jdbc.model;


import com.ane56.engine.jdbc.thrit.struct.TJDBCEngineExecutor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDBCEngineExecutorRef {
    private String host;
    private Integer port;
    private String prefixPath;
    private UUID executorRefId;
    private long latestHeartbeatTime;

    public static JDBCEngineExecutorRef parseFromTJDBCEngineDriver(TJDBCEngineExecutor engineExecutor) {
        return JDBCEngineExecutorRef.builder()
                .host(engineExecutor.getHost())
                .port(engineExecutor.getPort())
                .prefixPath(engineExecutor.getPrefix())
                .executorRefId(UUID.fromString(engineExecutor.getExecutorRefId()))
                .latestHeartbeatTime(0L)
                .build();
    }

    public TJDBCEngineExecutor asTJDBCEngineExecutor() {
        TJDBCEngineExecutor engineExecutor = new TJDBCEngineExecutor();
        engineExecutor.setHost(host);
        engineExecutor.setPort(port);
        engineExecutor.setPrefix(prefixPath);
        engineExecutor.setExecutorRefId(executorRefId.toString());
        return engineExecutor;
    }
}
