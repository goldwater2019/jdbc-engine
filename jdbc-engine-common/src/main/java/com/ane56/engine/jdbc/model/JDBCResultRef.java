package com.ane56.engine.jdbc.model;


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

    public TJDBCResultRef asTJDBCResultRef() {
        TJDBCResultRef tjdbcResultRef = new TJDBCResultRef();
        tjdbcResultRef.setResultSet(jdbcResultSet.asTJDBCResultSet());
        tjdbcResultRef.setOperationRef(jdbcOperationRef.asTJDBCOperationRef());
        return tjdbcResultRef;
    }

    public static JDBCResultRef parseFromTJDBCResultRef(TJDBCResultRef tjdbcResultRef) {
        return JDBCResultRef.builder()
                .jdbcResultSet(JDBCResultSet.parseFromTJDBCResultSet(tjdbcResultRef.getResultSet()))
                .jdbcOperationRef(JDBCOperationRef.parseFromTJDBCOperationRef(tjdbcResultRef.getOperationRef()))
                .build();
    }
}
