package com.ane56.engine.jdbc.model.thrift;

import com.ane56.engine.jdbc.thrit.service.JDBCEngineExecutorService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.thrift.transport.TTransport;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDBCEngineExecutorServiceClientSuite {
    private JDBCEngineExecutorService.Client client;
    private TTransport tTransport;
}
