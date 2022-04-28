package com.ane56.xsql.service.manager;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.ane56.xsql.common.enumeration.UltraColumnType;
import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhangxinsen
 * @Date: 2022/3/29 11:20 PM
 * @Desc:
 * @Version: v1.0
 */

@Slf4j
@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PooledDataSourceManager {

    /**
     * 单例方法
     */
    private static volatile PooledDataSourceManager singleton;
    // name->catalog对象
    private Map<String, UltraCatalog> name2catalog = new HashMap<>();
    // name -> datasource对象
    private Map<String, DruidDataSource> name2source = new HashMap<>();

    ;

    private PooledDataSourceManager() {
    }

    public static PooledDataSourceManager getInstance() {
        if (singleton == null) {
            synchronized (PooledDataSourceManager.class) {
                if (singleton == null) {
                    singleton = new PooledDataSourceManager();
                }
            }
        }
        return singleton;
    }

    /**
     * 当更新完catalogs后调用, 检查每个catalog的datasource状态
     */
    public void checkDataSources() {
        for (UltraCatalog catalog : name2catalog.values()) {
            checkDataSource(catalog);
        }
    }

    /**
     * 是否需要更新连接池
     *
     * @param druidDataSource
     * @return
     */
    private boolean isNeedUpdate(DruidDataSource druidDataSource) {
        // TODO add checkout condition
        if (druidDataSource == null) {
            return true;
        }
        return false;
    }

    public void checkDataSource(UltraCatalog catalog) {
        DruidDataSource druidDataSource = name2source.get(catalog.getName());
        boolean needUpdate = isNeedUpdate(druidDataSource);
        if (needUpdate) {
            // 数据源配置
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl(catalog.getJdbcUrl());
            dataSource.setDriverClassName(catalog.getDriverClassName()); // 这个可以缺省的，会根据url自动识别
            dataSource.setUsername(catalog.getUsername());
            dataSource.setPassword(catalog.getPassword());

            // 下面都是可选的配置
            dataSource.setInitialSize(10);  // 初始连接数，默认0
            dataSource.setMaxActive(30);  // 最大连接数，默认8
            dataSource.setMinIdle(10);  // 最小闲置数
            dataSource.setMaxWait(2000);  // 获取连接的最大等待时间，单位毫秒
            dataSource.setPoolPreparedStatements(true); // 缓存PreparedStatement，默认false
            dataSource.setMaxOpenPreparedStatements(20); // 缓存PreparedStatement的最大数量，默认-1（不缓存）。大于0时会自动开启缓存PreparedStatement，所以可以省略上一句代码
            name2source.put(catalog.getName(), dataSource);
        }
    }

    /**
     * 将一个jdbcCatalog对象更新至内存中
     *
     * @param catalog
     */
    public void addCatalog(UltraCatalog catalog) {
        name2catalog.put(catalog.getName(), catalog);
    }

    /**
     * 将查询结果封装成一个List<UltraResultRow>对象
     *
     * @param catalogName
     * @param sqlStatement
     * @return
     */
    public List<UltraResultRow> query(String catalogName, String sqlStatement) throws SQLException, XSQLException {
        long startTime = System.currentTimeMillis();
        DruidDataSource dataSource = getName2source().get(catalogName);
        if (dataSource == null) {
            log.error("catalog: " + catalogName + " not exists, please initialize it before use");
            throw new XSQLException("catalog: " + catalogName + " not exists, please initialize it before use");
        }
        log.info("get data source costs: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        DruidPooledConnection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<UltraResultRow> result = new LinkedList<>();
        while (resultSet.next()) {
            List<Object> ultraResultSetData = new LinkedList<>();
            UltraResultSetMetaData ultraResultSetMetaData = UltraResultSetMetaData.builder().build();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 配置相应的columnCount
            ultraResultSetMetaData.setColumnCount(columnCount);
            List<UltraResultColumnMetaData> ultraResultColumnMetaDataList = new LinkedList<>();
            for (int i = 1; i <= columnCount; i++) {
                int nullable = metaData.isNullable(i);
                UltraResultColumnMetaData ultraResultColumnMetaData = UltraResultColumnMetaData.builder()
                        .autoIncrement(metaData.isAutoIncrement(i))
                        .caseSensitive(metaData.isCaseSensitive(i))
                        .searchable(metaData.isSearchable(i))
                        .currency(metaData.isCurrency(i))
                        .nullable(nullable)
                        .signed(metaData.isSigned(i))
                        .columnDisplaySize(metaData.getColumnDisplaySize(i))
                        .columnLabel(metaData.getColumnLabel(i))
                        .columnName(metaData.getColumnName(i))
                        .schemaName(metaData.getSchemaName(i))
                        .precision(metaData.getPrecision(i))
                        .scale(metaData.getScale(i))
                        .tableName(metaData.getTableName(i))
                        .catalogName(metaData.getCatalogName(i))
                        .columnType(metaData.getColumnType(i))
                        .columnTypeName(metaData.getColumnTypeName(i))
                        .readOnly(metaData.isReadOnly(i))
                        .writable(metaData.isWritable(i))
                        .definitelyWritable(metaData.isDefinitelyWritable(i))
                        .columnClassName(metaData.getColumnClassName(i))
                        .ultraColumnType(UltraColumnType.findByValue(metaData.getColumnType(i)))
                        .build();
                ultraResultColumnMetaDataList.add(ultraResultColumnMetaData);
                // TODO 新增数据
                ultraResultSetData.add(resultSet.getObject(i));
            }
            ultraResultSetMetaData.setColumnMetaDataList(ultraResultColumnMetaDataList);
            result.add(
                    UltraResultRow.builder()
                            .ultraResultSetData(ultraResultSetData)
                            .ultraResultSetMetaData(ultraResultSetMetaData)
                            .build()
            );
        }
        connection.close();
        log.info("query pool costs: " + (System.currentTimeMillis() - startTime));
        return result;
    }

    public void execute(String catalogName, String sqlStatement) throws XSQLException, SQLException {
        long startTime = System.currentTimeMillis();
        DruidDataSource dataSource = getName2source().get(catalogName);
        if (dataSource == null) {
            log.error("catalog: " + catalogName + " not exists, please initialize it before use");
            throw new XSQLException("catalog: " + catalogName + " not exists, please initialize it before use");
        }
        log.info("get data source costs: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        DruidPooledConnection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        boolean executeResult = preparedStatement.execute();
        connection.close();
        log.info("query pool costs: " + (System.currentTimeMillis() - startTime));
        return;
    }

    public UltraDatabaseMetaData getDatabaseMetaData(String catalogName) throws XSQLException, SQLException {
        long startTime = System.currentTimeMillis();
        DruidDataSource druidDataSource = getName2source().get(catalogName);
        if (druidDataSource == null) {
            log.error("catalog: " + catalogName + " not exists, please initialize it before use");
            throw new XSQLException("catalog: " + catalogName + " not exists, please initialize it before use");
        }
        log.info("get data source costs: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        DruidPooledConnection connection = druidDataSource.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        UltraDatabaseMetaData ultraDatabaseMetaData = new UltraDatabaseMetaData();
        connection.close();
        ultraDatabaseMetaData.setAllProceduresAreCallable(metaData.allProceduresAreCallable());
        ultraDatabaseMetaData.setAllTablesAreSelectable(metaData.allTablesAreSelectable());
        ultraDatabaseMetaData.setURL(metaData.getURL());
        ultraDatabaseMetaData.setUserName(metaData.getUserName());
        ultraDatabaseMetaData.setReadOnly(metaData.isReadOnly());
        ultraDatabaseMetaData.setNullsAreSortedHigh(metaData.nullsAreSortedHigh());
        ultraDatabaseMetaData.setNullsAreSortedLow(metaData.nullsAreSortedLow());
        ultraDatabaseMetaData.setNullsAreSortedAtStart(metaData.nullsAreSortedAtStart());
        ultraDatabaseMetaData.setNullsAreSortedAtEnd(metaData.nullsAreSortedAtEnd());
        ultraDatabaseMetaData.setDatabaseProductName(metaData.getDatabaseProductName());
        ultraDatabaseMetaData.setDatabaseProductVersion(metaData.getDatabaseProductVersion());
        ultraDatabaseMetaData.setDriverName(metaData.getDriverName());
        ultraDatabaseMetaData.setDriverVersion(metaData.getDriverVersion());
        ultraDatabaseMetaData.setDriverMajorVersion(metaData.getDriverMajorVersion());
        ultraDatabaseMetaData.setDriverMinorVersion(metaData.getDriverMinorVersion());
        ultraDatabaseMetaData.setUsesLocalFiles(metaData.usesLocalFiles());
        ultraDatabaseMetaData.setUsesLocalFilePerTable(metaData.usesLocalFilePerTable());
        ultraDatabaseMetaData.setSupportsMixedCaseIdentifiers(metaData.supportsMixedCaseIdentifiers());
        ultraDatabaseMetaData.setStoresUpperCaseIdentifiers(metaData.storesUpperCaseIdentifiers());
        ultraDatabaseMetaData.setStoresLowerCaseIdentifiers(metaData.storesLowerCaseIdentifiers());
        ultraDatabaseMetaData.setStoresMixedCaseIdentifiers(metaData.storesMixedCaseIdentifiers());
        ultraDatabaseMetaData.setSupportsMixedCaseQuotedIdentifiers(metaData.supportsMixedCaseQuotedIdentifiers());
        ultraDatabaseMetaData.setStoresUpperCaseQuotedIdentifiers(metaData.storesUpperCaseQuotedIdentifiers());
        ultraDatabaseMetaData.setStoresLowerCaseQuotedIdentifiers(metaData.storesLowerCaseQuotedIdentifiers());
        ultraDatabaseMetaData.setStoresMixedCaseQuotedIdentifiers(metaData.storesMixedCaseQuotedIdentifiers());
        ultraDatabaseMetaData.setIdentifierQuoteString(metaData.getIdentifierQuoteString());
        ultraDatabaseMetaData.setSqlKeywords(metaData.getSQLKeywords());
        ultraDatabaseMetaData.setNumericFunctions(metaData.getNumericFunctions());
        ultraDatabaseMetaData.setStringFunctions(metaData.getStringFunctions());
        ultraDatabaseMetaData.setSystemFunctions(metaData.getSystemFunctions());
        ultraDatabaseMetaData.setTimeDateFunctions(metaData.getTimeDateFunctions());
        ultraDatabaseMetaData.setSearchStringEscape(metaData.getSearchStringEscape());
        ultraDatabaseMetaData.setExtraNameCharacters(metaData.getExtraNameCharacters());
        ultraDatabaseMetaData.setSupportsAlterTableWithAddColumn(metaData.supportsAlterTableWithAddColumn());
        ultraDatabaseMetaData.setSupportsAlterTableWithDropColumn(metaData.supportsAlterTableWithDropColumn());
        ultraDatabaseMetaData.setSupportsColumnAliasing(metaData.supportsColumnAliasing());
        ultraDatabaseMetaData.setNullPlusNonNullIsNull(metaData.nullPlusNonNullIsNull());
        ultraDatabaseMetaData.setSupportsConvert(metaData.supportsConvert());
        ultraDatabaseMetaData.setSupportsTableCorrelationNames(metaData.supportsTableCorrelationNames());
        ultraDatabaseMetaData.setSupportsDifferentTableCorrelationNames(metaData.supportsDifferentTableCorrelationNames());
        ultraDatabaseMetaData.setSupportsExpressionsInOrderBy(metaData.supportsExpressionsInOrderBy());
        ultraDatabaseMetaData.setSupportsOrderByUnrelated(metaData.supportsOrderByUnrelated());
        ultraDatabaseMetaData.setSupportsGroupBy(metaData.supportsGroupBy());
        ultraDatabaseMetaData.setSupportsGroupByUnrelated(metaData.supportsGroupByUnrelated());
        ultraDatabaseMetaData.setSupportsGroupByBeyondSelect(metaData.supportsGroupByBeyondSelect());
        ultraDatabaseMetaData.setSupportsLikeEscapeClause(metaData.supportsLikeEscapeClause());
        ultraDatabaseMetaData.setSupportsMultipleResultSets(metaData.supportsMultipleResultSets());
        ultraDatabaseMetaData.setSupportsMultipleTransactions(metaData.supportsMultipleTransactions());
        ultraDatabaseMetaData.setSupportsNonNullableColumns(metaData.supportsNonNullableColumns());
        ultraDatabaseMetaData.setSupportsMinimumSQLGrammar(metaData.supportsMinimumSQLGrammar());
        ultraDatabaseMetaData.setSupportsCoreSQLGrammar(metaData.supportsCoreSQLGrammar());
        ultraDatabaseMetaData.setSupportsExtendedSQLGrammar(metaData.supportsExtendedSQLGrammar());
        ultraDatabaseMetaData.setSupportsANSI92EntryLevelSQL(metaData.supportsANSI92EntryLevelSQL());
        ultraDatabaseMetaData.setSupportsANSI92IntermediateSQL(metaData.supportsANSI92IntermediateSQL());
        ultraDatabaseMetaData.setSupportsANSI92FullSQL(metaData.supportsANSI92FullSQL());
        ultraDatabaseMetaData.setSupportsIntegrityEnhancementFacility(metaData.supportsIntegrityEnhancementFacility());
        ultraDatabaseMetaData.setSupportsOuterJoins(metaData.supportsOuterJoins());
        ultraDatabaseMetaData.setSupportsFullOuterJoins(metaData.supportsFullOuterJoins());
        ultraDatabaseMetaData.setSupportsLimitedOuterJoins(metaData.supportsLimitedOuterJoins());
        ultraDatabaseMetaData.setSchemaTerm(metaData.getSchemaTerm());
        ultraDatabaseMetaData.setProcedureTerm(metaData.getProcedureTerm());
        ultraDatabaseMetaData.setDatalogTerm(metaData.getCatalogTerm());
        ultraDatabaseMetaData.setCatalogAtStart(metaData.isCatalogAtStart());
        ultraDatabaseMetaData.setCatalogSeparator(metaData.getCatalogSeparator());
        ultraDatabaseMetaData.setSupportsSchemasInDataManipulation(metaData.supportsSchemasInDataManipulation());
        ultraDatabaseMetaData.setSupportsSchemasInProcedureCalls(metaData.supportsSchemasInProcedureCalls());
        ultraDatabaseMetaData.setSupportsSchemasInTableDefinitions(metaData.supportsSchemasInTableDefinitions());
        ultraDatabaseMetaData.setSupportsSchemasInIndexDefinitions(metaData.supportsSchemasInIndexDefinitions());
        ultraDatabaseMetaData.setSupportsSchemasInPrivilegeDefinitions(metaData.supportsSchemasInPrivilegeDefinitions());
        ultraDatabaseMetaData.setSupportsCatalogsInDataManipulation(metaData.supportsCatalogsInDataManipulation());
        ultraDatabaseMetaData.setSupportsCatalogsInProcedureCalls(metaData.supportsCatalogsInProcedureCalls());
        ultraDatabaseMetaData.setSupportsCatalogsInTableDefinitions(metaData.supportsCatalogsInTableDefinitions());
        ultraDatabaseMetaData.setSupportsCatalogsInIndexDefinitions(metaData.supportsCatalogsInIndexDefinitions());
        ultraDatabaseMetaData.setSupportsCatalogsInPrivilegeDefinitions(metaData.supportsCatalogsInPrivilegeDefinitions());
        ultraDatabaseMetaData.setSupportsPositionedDelete(metaData.supportsPositionedDelete());
        ultraDatabaseMetaData.setSupportsPositionedUpdate(metaData.supportsPositionedUpdate());
        ultraDatabaseMetaData.setSupportsSelectForUpdate(metaData.supportsSelectForUpdate());
        ultraDatabaseMetaData.setSupportsStoredProcedures(metaData.supportsStoredProcedures());
        ultraDatabaseMetaData.setSupportsSubqueriesInComparisons(metaData.supportsSubqueriesInComparisons());
        ultraDatabaseMetaData.setSupportsSubqueriesInExists(metaData.supportsSubqueriesInExists());
        ultraDatabaseMetaData.setSupportsSubqueriesInIns(metaData.supportsSubqueriesInIns());
        ultraDatabaseMetaData.setSupportsSubqueriesInQuantifieds(metaData.supportsSubqueriesInQuantifieds());
        ultraDatabaseMetaData.setSupportsCorrelatedSubqueries(metaData.supportsCorrelatedSubqueries());
        ultraDatabaseMetaData.setSupportsUnion(metaData.supportsUnion());
        ultraDatabaseMetaData.setSupportsUnionAll(metaData.supportsUnionAll());
        ultraDatabaseMetaData.setSupportsOpenCursorsAcrossCommit(metaData.supportsOpenCursorsAcrossCommit());
        ultraDatabaseMetaData.setSupportsOpenCursorsAcrossRollback(metaData.supportsOpenCursorsAcrossRollback());
        ultraDatabaseMetaData.setSupportsOpenStatementsAcrossCommit(metaData.supportsOpenStatementsAcrossCommit());
        ultraDatabaseMetaData.setSupportsOpenStatementsAcrossRollback(metaData.supportsOpenStatementsAcrossRollback());
        ultraDatabaseMetaData.setMaxBinaryLiteralLength(metaData.getMaxBinaryLiteralLength());
        ultraDatabaseMetaData.setMaxCharLiteralLength(metaData.getMaxCharLiteralLength());
        ultraDatabaseMetaData.setMaxColumnNameLength(metaData.getMaxColumnNameLength());
        ultraDatabaseMetaData.setMaxColumnsInGroupBy(metaData.getMaxColumnsInGroupBy());
        ultraDatabaseMetaData.setMaxColumnsInIndex(metaData.getMaxColumnsInIndex());
        ultraDatabaseMetaData.setMaxColumnsInOrderBy(metaData.getMaxColumnsInOrderBy());
        ultraDatabaseMetaData.setMaxColumnsInSelect(metaData.getMaxColumnsInSelect());
        ultraDatabaseMetaData.setMaxColumnsInTable(metaData.getMaxColumnsInTable());
        ultraDatabaseMetaData.setMaxConnections(metaData.getMaxConnections());
        ultraDatabaseMetaData.setMaxCursorNameLength(metaData.getMaxCursorNameLength());
        ultraDatabaseMetaData.setMaxIndexLength(metaData.getMaxIndexLength());
        ultraDatabaseMetaData.setMaxSchemaNameLength(metaData.getMaxSchemaNameLength());
        ultraDatabaseMetaData.setMaxProcedureNameLength(metaData.getMaxProcedureNameLength());
        ultraDatabaseMetaData.setMaxCatalogNameLength(metaData.getMaxCatalogNameLength());
        ultraDatabaseMetaData.setMaxRowSize(metaData.getMaxRowSize());
        ultraDatabaseMetaData.setDoesMaxRowSizeIncludeBlobs(metaData.doesMaxRowSizeIncludeBlobs());
        ultraDatabaseMetaData.setMaxStatementLength(metaData.getMaxStatementLength());
        ultraDatabaseMetaData.setMaxStatements(metaData.getMaxStatements());
        ultraDatabaseMetaData.setMaxTableNameLength(metaData.getMaxTableNameLength());
        ultraDatabaseMetaData.setMaxTablesInSelect(metaData.getMaxTablesInSelect());
        ultraDatabaseMetaData.setMaxUserNameLength(metaData.getMaxUserNameLength());
        ultraDatabaseMetaData.setDefaultTransactionIsolation(metaData.getDefaultTransactionIsolation());
        ultraDatabaseMetaData.setSupportsTransactions(metaData.supportsTransactions());
        ultraDatabaseMetaData.setSupportsDataDefinitionAndDataManipulationTransactions(metaData.supportsDataDefinitionAndDataManipulationTransactions());
        ultraDatabaseMetaData.setSupportsDataManipulationTransactionsOnly(metaData.supportsDataManipulationTransactionsOnly());
        ultraDatabaseMetaData.setDataDefinitionCausesTransactionCommit(metaData.dataDefinitionCausesTransactionCommit());
        ultraDatabaseMetaData.setDataDefinitionIgnoredInTransactions(metaData.dataDefinitionIgnoredInTransactions());
        ultraDatabaseMetaData.setSchemas(metaData.getSchemas());
        ultraDatabaseMetaData.setCatalogs(metaData.getCatalogs());
        ultraDatabaseMetaData.setTableTypes(metaData.getTableTypes());
        ultraDatabaseMetaData.setTypeInfo(metaData.getTypeInfo());
        ultraDatabaseMetaData.setSupportsBatchUpdates(metaData.supportsBatchUpdates());
        ultraDatabaseMetaData.setConnection(metaData.getConnection());
        ultraDatabaseMetaData.setSupportsSavepoints(metaData.supportsSavepoints());
        ultraDatabaseMetaData.setSupportsNamedParameters(metaData.supportsNamedParameters());
        ultraDatabaseMetaData.setSupportsMultipleOpenResults(metaData.supportsMultipleOpenResults());
        ultraDatabaseMetaData.setSupportsGetGeneratedKeys(metaData.supportsGetGeneratedKeys());
        ultraDatabaseMetaData.setResultSetHoldability(metaData.getResultSetHoldability());
        ultraDatabaseMetaData.setDatabaseMajorVersion(metaData.getDatabaseMajorVersion());
        ultraDatabaseMetaData.setDatabaseMinorVersion(metaData.getDatabaseMinorVersion());
        ultraDatabaseMetaData.setJdbcMajorVersion(metaData.getJDBCMajorVersion());
        ultraDatabaseMetaData.setJdbcMinorVersion(metaData.getJDBCMinorVersion());
        ultraDatabaseMetaData.setSqlStateType(metaData.getSQLStateType());
        ultraDatabaseMetaData.setLocatorsUpdateCopy(metaData.locatorsUpdateCopy());
        ultraDatabaseMetaData.setSupportsStatementPooling(metaData.supportsStatementPooling());
        ultraDatabaseMetaData.setRowIdLifetime(metaData.getRowIdLifetime());
        ultraDatabaseMetaData.setSupportsStoredFunctionsUsingCallSyntax(metaData.supportsStoredFunctionsUsingCallSyntax());
        ultraDatabaseMetaData.setAutoCommitFailureClosesAllResultSets(metaData.autoCommitFailureClosesAllResultSets());
        ultraDatabaseMetaData.setClientInfoProperties(metaData.getClientInfoProperties());
        ultraDatabaseMetaData.setGeneratedKeyAlwaysReturned(metaData.generatedKeyAlwaysReturned());
        return ultraDatabaseMetaData;
    }
}
