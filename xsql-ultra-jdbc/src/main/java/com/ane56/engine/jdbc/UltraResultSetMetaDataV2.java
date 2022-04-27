package com.ane56.engine.jdbc;

import com.ane56.xsql.common.model.UltraResultColumnMetaData;
import com.google.common.collect.ImmutableList;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * TODO 完善此处的数据
 *
 * @Author: zhangxinsen
 * @Date: 2022/4/24 9:50 PM
 * @Desc:
 * @Version: v1.0
 */

public class UltraResultSetMetaDataV2
        implements ResultSetMetaData {

    private int countCount;
    private List<UltraResultColumnMetaData> columnMetaDataList;

    /**
     * 构造方法
     *
     * @param columnMetaDataList
     */
    public UltraResultSetMetaDataV2(List<UltraResultColumnMetaData> columnMetaDataList) {
        this.countCount = columnMetaDataList.size();
        this.columnMetaDataList = ImmutableList.copyOf(requireNonNull(columnMetaDataList, "column list is null"));
    }

    @Override
    public int getColumnCount() throws SQLException {
        return columnMetaDataList.size();
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getAutoIncrement();
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getCaseSensitive();
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getSearchable();
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getCurrency();
    }

    @Override
    public int isNullable(int column) throws SQLException {
        Integer nullable = columnMetaDataList.get(column - 1).getNullable();
        if (nullable == 0) {
            return ResultSetMetaData.columnNoNulls;
        } else if (nullable == 1) {
            return ResultSetMetaData.columnNullable;
        }
        return ResultSetMetaData.columnNullableUnknown;
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getSigned();
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getColumnDisplaySize();
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getColumnLabel();
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getColumnName();
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getSchemaName();
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getPrecision();
    }

    @Override
    public int getScale(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getScale();
    }

    @Override
    public String getTableName(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getTableName();
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getCatalogName();
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getColumnType();
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getColumnTypeName();
    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getReadOnly();
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getWritable();
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getDefinitelyWritable();
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        return columnMetaDataList.get(column - 1).getColumnClassName();
    }

    @Override
    public <T> T unwrap(Class<T> iface)
            throws SQLException {
        if (isWrapperFor(iface)) {
            return (T) this;
        }
        throw new SQLException("No wrapper for " + iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface)
            throws SQLException {
        return iface.isInstance(this);
    }


}
