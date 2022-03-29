package com.ane56.engine.jdbc.model.thrift;

import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.thrift.transport.TTransport;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDBCEngineDriverServiceClientSuite {
    private JDBCEngineDriverService.Client client;
    private TTransport tTransport;
}
