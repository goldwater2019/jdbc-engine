package com.ane56.engine.jdbc.model;

import com.ane56.engine.jdbc.thrit.struct.TJDBCResultColumn;
import com.ane56.engine.jdbc.thrit.struct.TJDBCRsultRow;
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
public class JDBCResultRow {
    private List<JDBCResultColumn> columnList;

    public TJDBCRsultRow asTJDBCRsultRow() {
        TJDBCRsultRow tjdbcRsultRow = new TJDBCRsultRow();
        List<TJDBCResultColumn>columnList = new LinkedList<>();
        for (JDBCResultColumn jdbcResultColumn : getColumnList()) {
            columnList.add(jdbcResultColumn.asTJDBCResultColumn());
        }
        tjdbcRsultRow.setColumnList(columnList);
        return tjdbcRsultRow;
    }

    public static JDBCResultRow parseFromTJDBCRsultRow(TJDBCRsultRow tjdbcRsultRow) {
        List<TJDBCResultColumn> columnList = tjdbcRsultRow.getColumnList();
        List<JDBCResultColumn> jdbcResultColumns = new LinkedList<>();
        for (TJDBCResultColumn tjdbcResultColumn : columnList) {
            jdbcResultColumns.add(JDBCResultColumn.parseFromTJDBCResultColumn(tjdbcResultColumn));
        }
        return JDBCResultRow.builder()
                .columnList(jdbcResultColumns)
                .build();
    }
}
