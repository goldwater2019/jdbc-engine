package com.ane56.engine.jdbc.model;


import com.ane56.engine.jdbc.enumeration.JDBCQueryStatus;
import com.ane56.engine.jdbc.thrit.enumeration.TJDBCQueryStatus;
import com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDBCResultRef {
    private JDBCOperationRef jdbcOperationRef;
    private JDBCResultSet jdbcResultSet;

    private List<JDBCColumn> jdbcColumnList;

    private List<JDBCRowSet> jdbcRowSetList;

    public static JDBCResultRef parseFromTJDBCResultRef(TJDBCResultRef tjdbcResultRef) {
        if (tjdbcResultRef.getOperationRef().getQueryStatus() == TJDBCQueryStatus.FAILED) {
            return JDBCResultRef.builder()
                    .jdbcResultSet(null)
                    .jdbcColumnList(null)
                    .jdbcRowSetList(null)
                    .jdbcOperationRef(JDBCOperationRef.parseFromTJDBCOperationRef(tjdbcResultRef.getOperationRef()))
                    .build();
        }


        JDBCResultRef jdbcResultRef = JDBCResultRef.builder()
                .jdbcResultSet(JDBCResultSet.parseFromTJDBCResultSet(tjdbcResultRef.getResultSet()))
                .jdbcOperationRef(JDBCOperationRef.parseFromTJDBCOperationRef(tjdbcResultRef.getOperationRef()))
                .build();
        jdbcResultRef.checkContent();
        return jdbcResultRef;
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

    public void checkContent() {
        JDBCResultSet resultSet = getJdbcResultSet();
        List<JDBCResultRow> resultRowList = resultSet.getResultRowList();
        List<JDBCColumn> jdbcColumns = new LinkedList<>();
        if (resultRowList != null && resultRowList.size() > 0) {
            List<JDBCRowSet> jdbcRowSetList = new LinkedList<>();
            boolean isSchemaFullFilled = false;
            for (JDBCResultRow jdbcResultRow : resultRowList) {
                List<JDBCResultColumn> columnList = jdbcResultRow.getColumnList();
                if (!isSchemaFullFilled) {
                    for (JDBCResultColumn jdbcResultColumn : columnList) {
                        jdbcColumns.add(
                                JDBCColumn.builder()
                                        .jdbcColumnType(jdbcResultColumn.getColumnType())
                                        .jdbcColumnName(jdbcResultColumn.getColumnName())
                                        .build()
                        );
                    }
                    setJdbcColumnList(jdbcColumns);
                }
                isSchemaFullFilled = true;
                List<String> valueList =  new LinkedList<>();
                for (JDBCResultColumn jdbcResultColumn : jdbcResultRow.getColumnList()) {
                    valueList.add(jdbcResultColumn.getColumnValue());
                }
                JDBCRowSet jdbcRowSet = JDBCRowSet.builder()
                        .valueList(valueList)
                        .build();
                jdbcRowSetList.add(jdbcRowSet);
            }
            setJdbcRowSetList(jdbcRowSetList);
        }
    }
}
