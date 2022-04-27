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

public class UltraResultSetMetaData
        implements ResultSetMetaData {

    private int countCount;
    private List<UltraResultColumnMetaData> columnMetaDataList;

    /**
     * 构造方法
     *
     * @param columnMetaDataList
     */
    public UltraResultSetMetaData(List<UltraResultColumnMetaData> columnMetaDataList) {
        this.countCount = columnMetaDataList.size();
        this.columnMetaDataList = ImmutableList.copyOf(requireNonNull(columnMetaDataList, "column list is null"));
    }

    @Override
    public int getColumnCount() throws SQLException {
        return columnMetaDataList.size();
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        return columnMetaDataList.get(column).getAutoIncrement();
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        return columnMetaDataList.get(column).getCaseSensitive();
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        return columnMetaDataList.get(column).getSearchable();
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        return columnMetaDataList.get(column).getCurrency();
    }

    @Override
    public int isNullable(int column) throws SQLException {
        Integer nullable = columnMetaDataList.get(column).getNullable();
        if (nullable == 0) {
            return ResultSetMetaData.columnNoNulls;
        } else if (nullable == 1) {
            return ResultSetMetaData.columnNullable;
        }
        return ResultSetMetaData.columnNullableUnknown;
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        return columnMetaDataList.get(column).getSigned();
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        return columnMetaDataList.get(column).getColumnDisplaySize();
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        return columnMetaDataList.get(column).getColumnLabel();
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        return columnMetaDataList.get(column).getColumnName();
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        return columnMetaDataList.get(column).getSchemaName();
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        return columnMetaDataList.get(column).getPrecision();
    }

    @Override
    public int getScale(int column) throws SQLException {
        return columnMetaDataList.get(column).getScale();
    }

    @Override
    public String getTableName(int column) throws SQLException {
        return columnMetaDataList.get(column).getTableName();
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        return columnMetaDataList.get(column).getCatalogName();
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        return columnMetaDataList.get(column).getColumnType();
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        return columnMetaDataList.get(column).getColumnTypeName();
    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        return columnMetaDataList.get(column).getReadOnly();
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        return columnMetaDataList.get(column).getWritable();
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        return columnMetaDataList.get(column).getDefinitelyWritable();
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        return columnMetaDataList.get(column).getColumnClassName();
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
