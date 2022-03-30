package com.ane56.engine.jdbc.model;


import com.ane56.engine.jdbc.enumeration.JDBCQueryStatus;
import com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDBCOperationRef {
    long startTime;
    long endTime;
    String catalogName;
    UUID operationRefId;
    String sqlStatement;
    JDBCQueryStatus queryStatus;
    String message;

    public static JDBCOperationRef parseFromTJDBCOperationRef(TJDBCOperationRef operationRef) {
        return JDBCOperationRef.builder()
                .startTime(operationRef.getStartTime())
                .endTime(operationRef.getEndTime())
                .catalogName(operationRef.getCatalogName())
                .operationRefId(UUID.fromString(operationRef.getOperationRefId()))
                .sqlStatement(operationRef.getSqlStatement())
                .queryStatus(JDBCQueryStatus.parseFromTJDBCQueryStatus(operationRef.getQueryStatus()))
                .message(operationRef.getMessage())
                .build();
    }

    public TJDBCOperationRef asTJDBCOperationRef() {
        TJDBCOperationRef operationRef = new TJDBCOperationRef();
        operationRef.setStartTime(getStartTime());
        operationRef.setEndTime(getEndTime());
        operationRef.setCatalogName(getCatalogName());
        operationRef.setOperationRefId(getOperationRefId().toString());
        operationRef.setSqlStatement(getSqlStatement());
        operationRef.setQueryStatus(queryStatus.asTJDBCQueryStatus());
        operationRef.setMessage(getMessage());
        return operationRef;
    }
}
