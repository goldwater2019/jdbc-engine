package com.ane56.engine.jdbc.model;


import com.ane56.engine.jdbc.enumeration.JDBCQueryStatus;
import com.ane56.engine.jdbc.thrit.enumeration.TJDBCQueryStatus;
import com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDBCResultRef {
    private JDBCOperationRef jdbcOperationRef;
    private JDBCResultSet jdbcResultSet;

    public static JDBCResultRef parseFromTJDBCResultRef(TJDBCResultRef tjdbcResultRef) {
        if (tjdbcResultRef.getOperationRef().getQueryStatus() == TJDBCQueryStatus.FAILED) {
            return JDBCResultRef.builder()
                    .jdbcResultSet(null)
                    .jdbcOperationRef(JDBCOperationRef.parseFromTJDBCOperationRef(tjdbcResultRef.getOperationRef()))
                    .build();
        }
        return JDBCResultRef.builder()
                .jdbcResultSet(JDBCResultSet.parseFromTJDBCResultSet(tjdbcResultRef.getResultSet()))
                .jdbcOperationRef(JDBCOperationRef.parseFromTJDBCOperationRef(tjdbcResultRef.getOperationRef()))
                .build();
    }

    public TJDBCResultRef asTJDBCResultRef() {
        TJDBCResultRef tjdbcResultRef = new TJDBCResultRef();
        tjdbcResultRef.setOperationRef(jdbcOperationRef.asTJDBCOperationRef());
        if (jdbcOperationRef.getQueryStatus() == JDBCQueryStatus.FAILED) {
            tjdbcResultRef.setResultSet(null);
        } else {
            tjdbcResultRef.setResultSet(jdbcResultSet.asTJDBCResultSet());
        }
        return tjdbcResultRef;
    }
}
