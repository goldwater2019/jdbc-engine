package com.ane56.engine.jdbc;

import com.ane56.xsql.common.model.UltraDatabaseMetaData;
import com.ane56.xsql.common.model.UltraResultColumnMetaData;
import com.ane56.xsql.common.model.UltraResultRow;
import com.ane56.xsql.common.model.UltraResultSetMetaData;
import com.google.common.collect.ImmutableList;
import lombok.Builder;
import org.joda.time.DateTimeZone;
import org.joda.time.format.*;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static java.math.RoundingMode.HALF_UP;
import static java.util.Locale.ENGLISH;
import static java.util.Objects.requireNonNull;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 2:30 PM
 * @Desc:
 * @Version: v1.0
 */

@Builder
public class UltraResultSet implements ResultSet {

    public static UltraResultSet parseFromListOfUltraResultRow(List<UltraResultRow> ultraResultRowList) {
        UltraResultSet ultraResultSet = new UltraResultSet();
        List<List<Object>> data = new LinkedList<>();
        for (UltraResultRow ultraResultRow : ultraResultRowList) {
            if (ultraResultSet.getResultSetMetaData() == null) {
                List<UltraResultColumnMetaData> columnMetaDataList = new LinkedList<>();
                UltraResultSetMetaData ultraResultSetMetaData = ultraResultRow.getUltraResultSetMetaData();
                List<UltraResultColumnMetaData> ultraColumnMetaDataList = ultraResultSetMetaData.getColumnMetaDataList();
                columnMetaDataList = ImmutableList.copyOf(ultraColumnMetaDataList);
                UltraResultSetMetaDataV2 ultraResultSetMetaDataV2 =new UltraResultSetMetaDataV2(columnMetaDataList);
                ultraResultSet.setResultSetMetaData(ultraResultSetMetaDataV2);
            }
            data.add(ultraResultRow.getUltraResultSetData());
        }
        ultraResultSet.setResults(data.iterator());
        return ultraResultSet;
    }

    static final DateTimeFormatter DATE_FORMATTER = ISODateTimeFormat.date();
    static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm:ss.SSS");
    static final DateTimeFormatter TIME_WITH_TIME_ZONE_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormat.forPattern("HH:mm:ss.SSS ZZZ").getPrinter(),
                    new DateTimeParser[]{
                            DateTimeFormat.forPattern("HH:mm:ss.SSS Z").getParser(),
                            DateTimeFormat.forPattern("HH:mm:ss.SSS ZZZ").getParser(),
                    })
            .toFormatter()
            .withOffsetParsed();

    static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
    static final DateTimeFormatter TIMESTAMP_WITH_TIME_ZONE_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS ZZZ").getPrinter(),
                    new DateTimeParser[]{
                            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS Z").getParser(),
                            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS ZZZ").getParser(),
                    })
            .toFormatter()
            .withOffsetParsed();

    private Statement statement;
    private StatementClient client;
    private Iterator<List<Object>> results;
    private UltraResultSetMetaDataV2 resultSetMetaData;
    private AtomicBoolean wasNull = new AtomicBoolean();
    private AtomicBoolean closed = new AtomicBoolean();

    private AtomicReference<Map<String, Integer>> fieldMap = new AtomicReference<>();

    private AtomicReference<List<Object>> row = new AtomicReference<>();


    /**
     * ?????????????????????????????????????????????????????????ultraResultSet
     *
     * @param statement
     * @param client
     * @param maxRows
     * @throws SQLException
     */
    public UltraResultSet(Statement statement, StatementClient client, long maxRows)
            throws SQLException {
        this.statement = requireNonNull(statement, "statement is null");
        this.client = requireNonNull(client, "client is null");


        this.resultSetMetaData = client.getUltraResultSetMetaDataV2();

        this.results = client.getCurrentData().getData().iterator();
        Map<String, Integer> temp = new LinkedHashMap<>();
        int columnCount = this.resultSetMetaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            temp.put(this.resultSetMetaData.getColumnLabel(i + 1).toLowerCase(ENGLISH), i + 1);
        }
        fieldMap.set(temp);
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public StatementClient getClient() {
        return client;
    }

    public void setClient(StatementClient client) {
        this.client = client;
    }

    public Iterator<List<Object>> getResults() {
        return results;
    }

    public void setResults(Iterator<List<Object>> results) {
        this.results = results;
    }

    public UltraResultSetMetaDataV2 getResultSetMetaData() {
        return resultSetMetaData;
    }

    public void setResultSetMetaData(UltraResultSetMetaDataV2 resultSetMetaData) {
        this.resultSetMetaData = resultSetMetaData;
    }

    public AtomicBoolean getWasNull() {
        return wasNull;
    }

    public void setWasNull(AtomicBoolean wasNull) {
        this.wasNull = wasNull;
    }

    public AtomicBoolean getClosed() {
        return closed;
    }

    public void setClosed(AtomicBoolean closed) {
        this.closed = closed;
    }

    public AtomicReference<Map<String, Integer>> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(AtomicReference<Map<String, Integer>> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public void setRow(AtomicReference<List<Object>> row) {
        this.row = row;
    }

    public UltraResultSet(Statement statement, StatementClient client, Iterator<List<Object>> results, UltraResultSetMetaDataV2 resultSetMetaData, AtomicBoolean wasNull, AtomicBoolean closed, AtomicReference<Map<String, Integer>> fieldMap, AtomicReference<List<Object>> row) {
        this.statement = statement;
        this.client = client;
        this.results = results;
        this.resultSetMetaData = resultSetMetaData;
        this.wasNull = wasNull;
        this.closed = closed;
        this.fieldMap = fieldMap;
        this.row = row;
    }

    public UltraResultSet() {
    }

    @Override
    public boolean next()
            throws SQLException {
        checkOpen();
        try {
            if (!results.hasNext()) {
                row.set(null);
                return false;
            }
            row.set(results.next());
            return true;
        } catch (RuntimeException e) {
            if (e.getCause() instanceof SQLException) {
                throw (SQLException) e.getCause();
            }
            throw new SQLException("Error fetching results", e);
        }
    }

    @Override
    public void close()
            throws SQLException {
        closed.set(true);
        // TODO ??????????????????
    }

    @Override
    public boolean wasNull()
            throws SQLException {
        return wasNull.get();
    }

    @Override
    public String getString(int columnIndex)
            throws SQLException {
        Object value = column(columnIndex);
        return (value != null) ? value.toString() : null;
    }

    @Override
    public boolean getBoolean(int columnIndex)
            throws SQLException {
        Object value = column(columnIndex);
        return (value != null) ? (Boolean) value : false;
    }

    @Override
    public byte getByte(int columnIndex)
            throws SQLException {
        return toNumber(column(columnIndex)).byteValue();
    }

    @Override
    public short getShort(int columnIndex)
            throws SQLException {
        return toNumber(column(columnIndex)).shortValue();
    }

    @Override
    public int getInt(int columnIndex)
            throws SQLException {
        return toNumber(column(columnIndex)).intValue();
    }

    @Override
    public long getLong(int columnIndex)
            throws SQLException {
        return toNumber(column(columnIndex)).longValue();
    }

    @Override
    public float getFloat(int columnIndex)
            throws SQLException {
        return toNumber(column(columnIndex)).floatValue();
    }

    @Override
    public double getDouble(int columnIndex)
            throws SQLException {
        return toNumber(column(columnIndex)).doubleValue();
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale)
            throws SQLException {
        BigDecimal bigDecimal = getBigDecimal(columnIndex);
        if (bigDecimal != null) {
            bigDecimal = bigDecimal.setScale(scale, HALF_UP);
        }
        return bigDecimal;
    }

    @Override
    public byte[] getBytes(int columnIndex)
            throws SQLException {
        return (byte[]) column(columnIndex);
    }

    @Override
    public Date getDate(int columnIndex)
            throws SQLException {
        return getDate(columnIndex, DateTimeZone.UTC);
    }

    private Date getDate(int columnIndex, DateTimeZone localTimeZone)
            throws SQLException {
        Object value = column(columnIndex);
        if (value == null) {
            return null;
        }

        try {
            return new Date(DATE_FORMATTER.withZone(localTimeZone).parseMillis(String.valueOf(value)));
        } catch (IllegalArgumentException e) {
            throw new SQLException("Invalid date from server: " + value, e);
        }
    }

    @Override
    public Time getTime(int columnIndex)
            throws SQLException {
        return getTime(columnIndex, DateTimeZone.UTC);
    }

    private Time getTime(int columnIndex, DateTimeZone localTimeZone)
            throws SQLException {
        Object value = column(columnIndex);
        if (value == null) {
            return null;
        }

        // TODO ?????????????????????????????????????????????
        String columnTypeName = resultSetMetaData.getColumnTypeName(columnIndex);
        if (columnTypeName.equalsIgnoreCase("time")) {
            try {
                return new Time(TIME_FORMATTER.withZone(localTimeZone).parseMillis(String.valueOf(value)));
            } catch (IllegalArgumentException e) {
                throw new SQLException("Invalid time from server: " + value, e);
            }
        }

        if (columnTypeName.equalsIgnoreCase("time with time zone")) {
            try {
                return new Time(TIME_WITH_TIME_ZONE_FORMATTER.parseMillis(String.valueOf(value)));
            } catch (IllegalArgumentException e) {
                throw new SQLException("Invalid time from server: " + value, e);
            }
        }

        throw new IllegalArgumentException("Expected column to be a time type but is " + columnTypeName);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex)
            throws SQLException {
        return getTimestamp(columnIndex, DateTimeZone.UTC);
    }

    private Timestamp getTimestamp(int columnIndex, DateTimeZone localTimeZone)
            throws SQLException {
        Object value = column(columnIndex);
        if (value == null) {
            return null;
        }

        // ColumnInfo columnInfo = columnInfo(columnIndex);
        // Column column = getColumns(this.client).get(columnIndex);
        // TODO ???????????????????????????????????????
        String columnTypeName = resultSetMetaData.getColumnTypeName(columnIndex);
        if (columnTypeName.equalsIgnoreCase("timestamp")) {
            try {
                return new Timestamp(TIMESTAMP_FORMATTER.withZone(localTimeZone).parseMillis(String.valueOf(value)));
            } catch (IllegalArgumentException e) {
                throw new SQLException("Invalid timestamp from server: " + value, e);
            }
        }

        if (columnTypeName.equalsIgnoreCase("timestamp with time zone")) {
            try {
                return new Timestamp(TIMESTAMP_WITH_TIME_ZONE_FORMATTER.parseMillis(String.valueOf(value)));
            } catch (IllegalArgumentException e) {
                throw new SQLException("Invalid timestamp from server: " + value, e);
            }
        }

        throw new IllegalArgumentException("Expected column to be a timestamp type but is " + columnTypeName);
    }

    @Override
    public InputStream getAsciiStream(int columnIndex)
            throws SQLException {
        throw new NotImplementedException("ResultSet", "getAsciiStream");
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getUnicodeStream");
    }

    @Override
    public InputStream getBinaryStream(int columnIndex)
            throws SQLException {
        throw new NotImplementedException("ResultSet", "getBinaryStream");
    }

    @Override
    public String getString(String columnLabel)
            throws SQLException {
        Object value = column(columnLabel);
        return (value != null) ? value.toString() : null;
    }

    @Override
    public boolean getBoolean(String columnLabel)
            throws SQLException {
        Object value = column(columnLabel);
        return (value != null) ? (Boolean) value : false;
    }

    @Override
    public byte getByte(String columnLabel)
            throws SQLException {
        return toNumber(column(columnLabel)).byteValue();
    }

    @Override
    public short getShort(String columnLabel)
            throws SQLException {
        return toNumber(column(columnLabel)).shortValue();
    }

    @Override
    public int getInt(String columnLabel)
            throws SQLException {
        return toNumber(column(columnLabel)).intValue();
    }

    @Override
    public long getLong(String columnLabel)
            throws SQLException {
        return toNumber(column(columnLabel)).longValue();
    }

    @Override
    public float getFloat(String columnLabel)
            throws SQLException {
        return toNumber(column(columnLabel)).floatValue();
    }

    @Override
    public double getDouble(String columnLabel)
            throws SQLException {
        return toNumber(column(columnLabel)).doubleValue();
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale)
            throws SQLException {
        return getBigDecimal(columnIndex(columnLabel), scale);
    }

    @Override
    public byte[] getBytes(String columnLabel)
            throws SQLException {
        return (byte[]) column(columnLabel);
    }

    @Override
    public Date getDate(String columnLabel)
            throws SQLException {
        return getDate(columnIndex(columnLabel));
    }

    @Override
    public Time getTime(String columnLabel)
            throws SQLException {
        return getTime(columnIndex(columnLabel));
    }

    @Override
    public Timestamp getTimestamp(String columnLabel)
            throws SQLException {
        return getTimestamp(columnIndex(columnLabel));
    }

    @Override
    public InputStream getAsciiStream(String columnLabel)
            throws SQLException {
        throw new NotImplementedException("ResultSet", "getAsciiStream");
    }

    @Override
    public InputStream getUnicodeStream(String columnLabel)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getUnicodeStream");
    }

    @Override
    public InputStream getBinaryStream(String columnLabel)
            throws SQLException {
        throw new NotImplementedException("ResultSet", "getBinaryStream");
    }

    @Override
    public SQLWarning getWarnings()
            throws SQLException {
        checkOpen();
        return null;
    }

    @Override
    public void clearWarnings()
            throws SQLException {
        checkOpen();
    }

    @Override
    public String getCursorName()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getCursorName");
    }

    @Override
    public ResultSetMetaData getMetaData()
            throws SQLException {
        return resultSetMetaData;
    }

    @Override
    public Object getObject(int columnIndex)
            throws SQLException {
//        List<Column> columns = getColumns(this.client);
//        Column column = columns.get(columnIndex);
        String columnClassName = resultSetMetaData.getColumnClassName(columnIndex);
        if (columnClassName.toLowerCase(Locale.ROOT).endsWith("localdatetime")) {
            return column(columnIndex);
        }
        return column(columnIndex);
//        Integer columnType = resultSetMetaData.getColumnType(columnIndex);
//        switch (columnType) {
//            case Types.DATE:
//                return getDate(columnIndex);
//            case Types.TIME:
//                return getTime(columnIndex);
//            case Types.TIMESTAMP:
//                return getTimestamp(columnIndex);
//            case Types.ARRAY:
//                return getArray(columnIndex);
//            case Types.DECIMAL:
//                return getBigDecimal(columnIndex);
//            case Types.JAVA_OBJECT:
//                throw new SQLException("un support data type, java object");
//        }
//        return column(columnIndex);
    }

    @Override
    public Object getObject(String columnLabel)
            throws SQLException {
        return getObject(columnIndex(columnLabel));
    }

    @Override
    public int findColumn(String columnLabel)
            throws SQLException {
        checkOpen();
        return columnIndex(columnLabel);
    }

    @Override
    public Reader getCharacterStream(int columnIndex)
            throws SQLException {
        throw new NotImplementedException("ResultSet", "getCharacterStream");
    }

    @Override
    public Reader getCharacterStream(String columnLabel)
            throws SQLException {
        throw new NotImplementedException("ResultSet", "getCharacterStream");
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex)
            throws SQLException {
        Object value = column(columnIndex);
        if (value == null) {
            return null;
        }

        return new BigDecimal(String.valueOf(value));
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel)
            throws SQLException {
        return getBigDecimal(columnIndex(columnLabel));
    }

    @Override
    public boolean isBeforeFirst()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("isBeforeFirst");
    }

    @Override
    public boolean isAfterLast()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("isAfterLast");
    }

    @Override
    public boolean isFirst()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("isFirst");
    }

    @Override
    public boolean isLast()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("isLast");
    }

    @Override
    public void beforeFirst()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("beforeFirst");
    }

    @Override
    public void afterLast()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("afterLast");
    }

    @Override
    public boolean first()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("first");
    }

    @Override
    public boolean last()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("last");
    }

    @Override
    public int getRow()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getRow");
    }

    @Override
    public boolean absolute(int row)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("absolute");
    }

    @Override
    public boolean relative(int rows)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("relative");
    }

    @Override
    public boolean previous()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("previous");
    }

    @Override
    public void setFetchDirection(int direction)
            throws SQLException {
        checkOpen();
        if (direction != FETCH_FORWARD) {
            throw new SQLException("Fetch direction must be FETCH_FORWARD");
        }
    }

    @Override
    public int getFetchDirection()
            throws SQLException {
        checkOpen();
        return FETCH_FORWARD;
    }

    @Override
    public void setFetchSize(int rows)
            throws SQLException {
        checkOpen();
        if (rows < 0) {
            throw new SQLException("Rows is negative");
        }
        // fetch size is ignored
    }

    @Override
    public int getFetchSize()
            throws SQLException {
        checkOpen();
        // fetch size is ignored
        return 0;
    }

    @Override
    public int getType()
            throws SQLException {
        checkOpen();
        return TYPE_FORWARD_ONLY;
    }

    @Override
    public int getConcurrency()
            throws SQLException {
        checkOpen();
        return CONCUR_READ_ONLY;
    }

    @Override
    public boolean rowUpdated()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("rowUpdated");
    }

    @Override
    public boolean rowInserted()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("rowInserted");
    }

    @Override
    public boolean rowDeleted()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("rowDeleted");
    }

    @Override
    public void updateNull(int columnIndex)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNull");
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBoolean");
    }

    @Override
    public void updateByte(int columnIndex, byte x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateByte");
    }

    @Override
    public void updateShort(int columnIndex, short x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateShort");
    }

    @Override
    public void updateInt(int columnIndex, int x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateInt");
    }

    @Override
    public void updateLong(int columnIndex, long x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateLong");
    }

    @Override
    public void updateFloat(int columnIndex, float x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateFloat");
    }

    @Override
    public void updateDouble(int columnIndex, double x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateDouble");
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBigDecimal");
    }

    @Override
    public void updateString(int columnIndex, String x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateString");
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBytes");
    }

    @Override
    public void updateDate(int columnIndex, Date x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateDate");
    }

    @Override
    public void updateTime(int columnIndex, Time x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateTime");
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateTimestamp");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateAsciiStream");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBinaryStream");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateCharacterStream");
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateObject");
    }

    @Override
    public void updateObject(int columnIndex, Object x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateObject");
    }

    @Override
    public void updateNull(String columnLabel)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNull");
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBoolean");
    }

    @Override
    public void updateByte(String columnLabel, byte x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateByte");
    }

    @Override
    public void updateShort(String columnLabel, short x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateShort");
    }

    @Override
    public void updateInt(String columnLabel, int x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateInt");
    }

    @Override
    public void updateLong(String columnLabel, long x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateLong");
    }

    @Override
    public void updateFloat(String columnLabel, float x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateFloat");
    }

    @Override
    public void updateDouble(String columnLabel, double x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateDouble");
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBigDecimal");
    }

    @Override
    public void updateString(String columnLabel, String x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateString");
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBytes");
    }

    @Override
    public void updateDate(String columnLabel, Date x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateDate");
    }

    @Override
    public void updateTime(String columnLabel, Time x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateTime");
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateTimestamp");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateAsciiStream");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBinaryStream");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, int length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateCharacterStream");
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateObject");
    }

    @Override
    public void updateObject(String columnLabel, Object x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateObject");
    }

    @Override
    public void insertRow()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("insertRow");
    }

    @Override
    public void updateRow()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateRow");
    }

    @Override
    public void deleteRow()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("deleteRow");
    }

    @Override
    public void refreshRow()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("refreshRow");
    }

    @Override
    public void cancelRowUpdates()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("cancelRowUpdates");
    }

    @Override
    public void moveToInsertRow()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("moveToInsertRow");
    }

    @Override
    public void moveToCurrentRow()
            throws SQLException {
        throw new SQLFeatureNotSupportedException("moveToCurrentRow");
    }

    @Override
    public Statement getStatement() {
        return statement;
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getObject");
    }

    @Override
    public Ref getRef(int columnIndex)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getRef");
    }

    @Override
    public Blob getBlob(int columnIndex)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getBlob");
    }

    @Override
    public Clob getClob(int columnIndex)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getClob");
    }

    @Override
    public Array getArray(int columnIndex)
            throws SQLException {
        Object value = column(columnIndex);
        if (value == null) {
            return null;
        }
        throw new SQLFeatureNotSupportedException("getArray");
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getObject");
    }

    @Override
    public Ref getRef(String columnLabel)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getRef");
    }

    @Override
    public Blob getBlob(String columnLabel)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getBlob");
    }

    @Override
    public Clob getClob(String columnLabel)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getClob");
    }

    @Override
    public Array getArray(String columnLabel)
            throws SQLException {
        return getArray(columnIndex(columnLabel));
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal)
            throws SQLException {
        return getDate(columnIndex, DateTimeZone.forTimeZone(cal.getTimeZone()));
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal)
            throws SQLException {
        return getDate(columnIndex(columnLabel), cal);
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal)
            throws SQLException {
        return getTime(columnIndex, DateTimeZone.forTimeZone(cal.getTimeZone()));
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal)
            throws SQLException {
        return getTime(columnIndex(columnLabel), cal);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal)
            throws SQLException {
        return getTimestamp(columnIndex, DateTimeZone.forTimeZone(cal.getTimeZone()));
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal)
            throws SQLException {
        return getTimestamp(columnIndex(columnLabel), cal);
    }

    @Override
    public URL getURL(int columnIndex)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getURL");
    }

    @Override
    public URL getURL(String columnLabel)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getURL");
    }

    @Override
    public void updateRef(int columnIndex, Ref x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateRef");
    }

    @Override
    public void updateRef(String columnLabel, Ref x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateRef");
    }

    @Override
    public void updateBlob(int columnIndex, Blob x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBlob");
    }

    @Override
    public void updateBlob(String columnLabel, Blob x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBlob");
    }

    @Override
    public void updateClob(int columnIndex, Clob x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateClob");
    }

    @Override
    public void updateClob(String columnLabel, Clob x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateClob");
    }

    @Override
    public void updateArray(int columnIndex, Array x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateArray");
    }

    @Override
    public void updateArray(String columnLabel, Array x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateArray");
    }

    @Override
    public RowId getRowId(int columnIndex)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getRowId");
    }

    @Override
    public RowId getRowId(String columnLabel)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getRowId");
    }

    @Override
    public void updateRowId(int columnIndex, RowId x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateRowId");
    }

    @Override
    public void updateRowId(String columnLabel, RowId x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateRowId");
    }

    @Override
    public int getHoldability()
            throws SQLException {
        checkOpen();
        return ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public boolean isClosed()
            throws SQLException {
        return closed.get();
    }

    @Override
    public void updateNString(int columnIndex, String nString)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNString");
    }

    @Override
    public void updateNString(String columnLabel, String nString)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNString");
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNClob");
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNClob");
    }

    @Override
    public NClob getNClob(int columnIndex)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getNClob");
    }

    @Override
    public NClob getNClob(String columnLabel)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getNClob");
    }

    @Override
    public SQLXML getSQLXML(int columnIndex)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getSQLXML");
    }

    @Override
    public SQLXML getSQLXML(String columnLabel)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getSQLXML");
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateSQLXML");
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateSQLXML");
    }

    @Override
    public String getNString(int columnIndex)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getNString");
    }

    @Override
    public String getNString(String columnLabel)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getNString");
    }

    @Override
    public Reader getNCharacterStream(int columnIndex)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getNCharacterStream");
    }

    @Override
    public Reader getNCharacterStream(String columnLabel)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getNCharacterStream");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNCharacterStream");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNCharacterStream");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateAsciiStream");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBinaryStream");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateCharacterStream");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateAsciiStream");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBinaryStream");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateCharacterStream");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBlob");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBlob");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateClob");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateClob");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNClob");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNClob");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNCharacterStream");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNCharacterStream");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateAsciiStream");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBinaryStream");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateCharacterStream");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateAsciiStream");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBinaryStream");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateCharacterStream");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBlob");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateBlob");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateClob");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateClob");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNClob");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("updateNClob");
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getObject");
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type)
            throws SQLException {
        throw new SQLFeatureNotSupportedException("getObject");
    }

    @SuppressWarnings("unchecked")
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


    private void checkOpen()
            throws SQLException {
        if (isClosed()) {
            throw new SQLException("ResultSet is closed");
        }
    }

    private void checkValidRow()
            throws SQLException {
        if (row.get() == null) {
            throw new SQLException("Not on a valid row");
        }
    }

    private Object column(int index)
            throws SQLException {
        checkOpen();
        checkValidRow();
        if ((index <= 0) || (index > resultSetMetaData.getColumnCount())) {
            throw new SQLException("Invalid column index: " + index);
        }
        Object value = row.get().get(index - 1);
        wasNull.set(value == null);
        return value;
    }

    private Object column(String label)
            throws SQLException {
        checkOpen();
        checkValidRow();
        Object value = row.get().get(columnIndex(label) - 1);
        wasNull.set(value == null);
        return value;
    }

    private int columnIndex(String label)
            throws SQLException {
        if (label == null) {
            throw new SQLException("Column label is null");
        }
        Integer index = fieldMap.get().get(label.toLowerCase(ENGLISH));
        if (index == null) {
            throw new SQLException("Invalid column label: " + label);
        }
        return index;
    }

    private static Number toNumber(Object value)
            throws SQLException {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return (Number) value;
        }
        if (value instanceof Boolean) {
            return ((Boolean) value) ? 1 : 0;
        }
        throw new SQLException("Value is not a number: " + value.getClass().getCanonicalName());
    }

//    private static List<Column> getColumns(StatementClient client)
//            throws SQLException {
//        while (client.isRunning()) {
//            QueryStatusInfo results = client.currentStatusInfo();
//            List<Column> columns = results.getColumns();
//            if (columns != null) {
//                return columns;
//            }
//            client.advance();
//        }
//
//        verify(client.isFinished());
//        QueryStatusInfo results = client.finalStatusInfo();
//        if (results.getError() == null) {
//            throw new SQLException(format("Query has no columns (#%s)", results.getId()));
//        }
//        throw resultsException(results);
//    }

//    private static class ResultsPageIterator
//            extends AbstractIterator<Iterable<List<Object>>> {
//        private StatementClient client;
//        private boolean isQuery;
//
//        private ResultsPageIterator(StatementClient client) {
//            this.client = requireNonNull(client, "client is null");
//            this.isQuery = isQuery(client);
//        }
//
//        private static boolean isQuery(StatementClient client) {
//            return client.isQuery();
//        }
//
//        @Override
//        protected Iterable<List<Object>> computeNext() {
//            if (isQuery) {
//            }
//            while (client.isRunning()) {
//                checkInterruption(null);
//
//                QueryStatusInfo results = client.currentStatusInfo();
//                Iterable<List<Object>> data = client.currentData().getData();
//
//                try {
//                    client.advance();
//                } catch (RuntimeException e) {
//                    checkInterruption(e);
//                    throw e;
//                }
//
//                if (data != null) {
//                    return data;
//                }
//            }
//
//            verify(client.isFinished());
//            QueryStatusInfo results = client.finalStatusInfo();
//            if (results.getError() != null) {
//                throw new RuntimeException(resultsException(results));
//            }
//
//            return endOfData();
//        }
//
//        private void checkInterruption(Throwable t) {
//            if (Thread.currentThread().isInterrupted()) {
//                // client.close();
//                throw new RuntimeException(new SQLException("ResultSet thread was interrupted", t));
//            }
//        }
//    }
//
//    public static SQLException resultsException(QueryStatusInfo results) {
//        QueryError error = requireNonNull(results.getError());
//        String message = format("Query failed (#%s): %s", results.getId(), error.getMessage());
//        return new SQLException(message);
//    }
//
//    private static Map<String, Integer> getFieldMap(List<Column> columns) {
//        Map<String, Integer> map = new HashMap<>();
//        for (int i = 0; i < columns.size(); i++) {
//            String name = columns.get(i).getColumnName().toLowerCase(ENGLISH);
//            if (!map.containsKey(name)) {
//                map.put(name, i + 1);
//            }
//        }
//        return ImmutableMap.copyOf(map);
//    }
}
