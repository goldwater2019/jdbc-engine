package com.ane56.xsql.common.model;

import lombok.*;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 4:41 PM
 * @Desc: 每一行resultSet内对象的数据
 * @Version: v1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class UltraResultRow implements Serializable {
    private static final long serialVersionUID = -6439942002129746938L;
    private UltraResultSetMetaData ultraResultSetMetaData;
    private List<Object> ultraResultSetData;

    public static List<UltraResultRow> parseResultSet(ResultSet resultSet) throws SQLException {
        List<UltraResultRow> result = new LinkedList<>();

        while (resultSet.next()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<Object> rowData = new LinkedList<>();
            int columnCount = metaData.getColumnCount();
            List<UltraResultColumnMetaData> columnMetaDataList = new LinkedList<>();
            for (int i = 0; i < columnCount; i++) {
                int columnIndex = i + 1;
                columnMetaDataList.add(
                        UltraResultColumnMetaData.parseFromMetaData(metaData, columnIndex)
                );
            }
            UltraResultSetMetaData resultSetMetaData = UltraResultSetMetaData.builder()
                    .columnCount(columnCount)
                    .columnMetaDataList(columnMetaDataList)
                    .build();
            for (int i = 0; i < columnCount; i++) {
                int columnIndex = i + 1;
                rowData.add(resultSet.getObject(columnIndex));
            }
            result.add(
                    UltraResultRow.builder()
                            .ultraResultSetMetaData(resultSetMetaData)
                            .ultraResultSetData(rowData)
                            .build()
            );
        }

        return result;
    }
}
