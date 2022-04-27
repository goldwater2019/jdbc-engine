package com.ane56.engine.jdbc.resultset;

import com.ane56.engine.jdbc.common.Column;
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

public class UltraResultSetMetaData
        implements ResultSetMetaData {

    private List<Column> columnLists;

    public UltraResultSetMetaData(List<Column> columnList) {
        this.columnLists = ImmutableList.copyOf(requireNonNull(columnList, "column list is null"));
    }

    @Override
    public int getColumnCount() throws SQLException {
        return columnLists.size();
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        return columnLists.get(column).getAutoIncrement();
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        return columnLists.get(column).getCaseSensitive();
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        return columnLists.get(column).getSearchable();
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        return columnLists.get(column).getCurrency();
    }

    @Override
    public int isNullable(int column) throws SQLException {
        Integer nullable = columnLists.get(column).getNullable();
        if (nullable == 0) {
            return ResultSetMetaData.columnNoNulls;
        } else if (nullable == 1) {
            return ResultSetMetaData.columnNullable;
        }
        return ResultSetMetaData.columnNullableUnknown;
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        return columnLists.get(column).getSigned();
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        return columnLists.get(column).getColumnDisplaySize();
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        return columnLists.get(column).getColumnLabel();
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        return columnLists.get(column).getColumnName();
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        return columnLists.get(column).getSchemaName();
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        return columnLists.get(column).getPrecision();
    }

    @Override
    public int getScale(int column) throws SQLException {
        return columnLists.get(column).getScale();
    }

    @Override
    public String getTableName(int column) throws SQLException {
        return columnLists.get(column).getTableName();
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        return columnLists.get(column).getCatalogName();
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        return columnLists.get(column).getColumnType();
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        return columnLists.get(column).getColumnTypeName();
    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        return columnLists.get(column).getReadOnly();
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        return columnLists.get(column).getWritable();
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        return columnLists.get(column).getDefinitelyWritable();
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        return columnLists.get(column).getColumnClassName();
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
