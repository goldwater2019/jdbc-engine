package com.ane56.engine.jdbc.model;


import com.ane56.engine.jdbc.thrit.struct.TJDBCResultSet;
import com.ane56.engine.jdbc.thrit.struct.TJDBCRsultRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * JDBC查询结果封装对象
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDBCResultSet {
    private List<JDBCResultRow> resultRowList;

    public TJDBCResultSet asTJDBCResultSet() {
        TJDBCResultSet tjdbcResultSet = new TJDBCResultSet();
        List<TJDBCRsultRow> resultRowList = new LinkedList<>();
        for (JDBCResultRow jdbcResultRow : getResultRowList()) {
            resultRowList.add(jdbcResultRow.asTJDBCRsultRow());
        }
        tjdbcResultSet.setResultRowList(resultRowList);
        return tjdbcResultSet;
    }

    public static JDBCResultSet parseFromTJDBCResultSet(TJDBCResultSet tjdbcResultSet) {
        List<TJDBCRsultRow> resultRowList = tjdbcResultSet.getResultRowList();
        List<JDBCResultRow> resultRows = new LinkedList<>();
        for (TJDBCRsultRow tjdbcRsultRow : resultRowList) {
            resultRows.add(JDBCResultRow.parseFromTJDBCRsultRow(tjdbcRsultRow));
        }
        return JDBCResultSet.builder()
                .resultRowList(resultRows)
                .build();
    }
}
