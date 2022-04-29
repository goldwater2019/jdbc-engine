package com.ane56.engine.jdbc;

import com.ane56.xsql.common.model.UltraDatabaseMetaData;

import java.sql.*;

import static com.ane56.engine.jdbc.UltraResultSet.parseFromListOfUltraResultRow;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 12:10 AM
 * @Desc:
 * @Version: v1.0
 */

public class UltraDatabaseMetaDataV2 implements DatabaseMetaData {
    private UltraDatabaseMetaData ultraDatabaseMetaData;
    private UltraConnection connection;

    public UltraDatabaseMetaDataV2(UltraDatabaseMetaData ultraDatabaseMetaData, UltraConnection connection) {
        this.ultraDatabaseMetaData = ultraDatabaseMetaData;
        this.connection = connection;
    }

    public UltraDatabaseMetaDataV2() {
    }

    private boolean isOK() {
        return ultraDatabaseMetaData != null;
    }

    @Override
    public boolean allProceduresAreCallable() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isAllProceduresAreCallable();
        }
        return false;
    }

    @Override
    public boolean allTablesAreSelectable() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isAllTablesAreSelectable();
        }
        return false;
    }

    @Override
    public String getURL() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getURL();
        }
        return null;
    }

    @Override
    public String getUserName() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getUserName();
        }
        return null;
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isReadOnly();
        }
        return false;
    }

    @Override
    public boolean nullsAreSortedHigh() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isNullsAreSortedHigh();
        }
        return false;
    }

    @Override
    public boolean nullsAreSortedLow() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isNullsAreSortedLow();
        }
        return false;
    }

    @Override
    public boolean nullsAreSortedAtStart() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isNullsAreSortedAtStart();
        }
        return false;
    }

    @Override
    public boolean nullsAreSortedAtEnd() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isNullsAreSortedAtEnd();
        }
        return false;
    }

    @Override
    public String getDatabaseProductName() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getDatabaseProductName();
        }
        return null;
    }

    @Override
    public String getDatabaseProductVersion() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getDatabaseProductVersion();
        }
        return null;
    }

    @Override
    public String getDriverName() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getDriverName();
        }
        return null;
    }

    @Override
    public String getDriverVersion() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getDriverVersion();
        }
        return null;
    }

    @Override
    public int getDriverMajorVersion() {
        if (isOK()) {
            return ultraDatabaseMetaData.getDriverMajorVersion();
        }
        return 0;
    }

    @Override
    public int getDriverMinorVersion() {

        if (isOK()) {
            return ultraDatabaseMetaData.getDriverMinorVersion();
        }
        return 0;
    }

    @Override
    public boolean usesLocalFiles() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isUsesLocalFiles();
        }
        return false;
    }

    @Override
    public boolean usesLocalFilePerTable() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isUsesLocalFilePerTable();
        }
        return false;
    }

    @Override
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isStoresMixedCaseIdentifiers();
        }
        return false;
    }

    @Override
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isStoresUpperCaseIdentifiers();
        }
        return false;
    }

    @Override
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isStoresLowerCaseIdentifiers();
        }
        return false;
    }

    @Override
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isStoresMixedCaseIdentifiers();
        }
        return false;
    }

    @Override
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isStoresMixedCaseQuotedIdentifiers();
        }
        return false;
    }

    @Override
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isStoresUpperCaseQuotedIdentifiers();
        }
        return false;
    }

    @Override
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isStoresLowerCaseQuotedIdentifiers();
        }
        return false;
    }

    @Override
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isStoresMixedCaseQuotedIdentifiers();
        }
        return false;
    }

    @Override
    public String getIdentifierQuoteString() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getIdentifierQuoteString();
        }
        return null;
    }

    @Override
    public String getSQLKeywords() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getSqlKeywords();
        }
        return null;
    }

    @Override
    public String getNumericFunctions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getNumericFunctions();
        }
        return null;
    }

    @Override
    public String getStringFunctions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getStringFunctions();
        }
        return null;
    }

    @Override
    public String getSystemFunctions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getSystemFunctions();
        }
        return null;
    }

    @Override
    public String getTimeDateFunctions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getTimeDateFunctions();
        }
        return null;
    }

    @Override
    public String getSearchStringEscape() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getSearchStringEscape();
        }
        return null;
    }

    @Override
    public String getExtraNameCharacters() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getExtraNameCharacters();
        }
        return null;
    }

    @Override
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsAlterTableWithAddColumn();
        }
        return false;
    }

    @Override
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsAlterTableWithDropColumn();
        }
        return false;
    }

    @Override
    public boolean supportsColumnAliasing() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsColumnAliasing();
        }
        return false;
    }

    @Override
    public boolean nullPlusNonNullIsNull() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isNullPlusNonNullIsNull();
        }
        return false;
    }

    @Override
    public boolean supportsConvert() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsConvert();
        }
        return false;
    }

    @Override
    public boolean supportsConvert(int fromType, int toType) throws SQLException {
        return false;
    }

    @Override
    public boolean supportsTableCorrelationNames() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsTableCorrelationNames();
        }
        return false;
    }

    @Override
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsDifferentTableCorrelationNames();
        }
        return false;
    }

    @Override
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsExpressionsInOrderBy();
        }
        return false;
    }

    @Override
    public boolean supportsOrderByUnrelated() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsOrderByUnrelated();
        }
        return false;
    }

    @Override
    public boolean supportsGroupBy() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsGroupBy();
        }
        return false;
    }

    @Override
    public boolean supportsGroupByUnrelated() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsGroupByUnrelated();
        }
        return false;
    }

    @Override
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsGroupByBeyondSelect();
        }
        return false;
    }

    @Override
    public boolean supportsLikeEscapeClause() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsLikeEscapeClause();
        }
        return false;
    }

    @Override
    public boolean supportsMultipleResultSets() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsMultipleResultSets();
        }
        return false;
    }

    @Override
    public boolean supportsMultipleTransactions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsMultipleTransactions();
        }
        return false;
    }

    @Override
    public boolean supportsNonNullableColumns() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsNonNullableColumns();
        }
        return false;
    }

    @Override
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsMinimumSQLGrammar();
        }
        return false;
    }

    @Override
    public boolean supportsCoreSQLGrammar() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsCoreSQLGrammar();
        }
        return false;
    }

    @Override
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsExtendedSQLGrammar();
        }
        return false;
    }

    @Override
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsANSI92EntryLevelSQL();
        }
        return false;
    }

    @Override
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsANSI92IntermediateSQL();
        }
        return false;
    }

    @Override
    public boolean supportsANSI92FullSQL() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsANSI92FullSQL();
        }
        return false;
    }

    @Override
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsIntegrityEnhancementFacility();
        }
        return false;
    }

    @Override
    public boolean supportsOuterJoins() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsOuterJoins();
        }
        return false;
    }

    @Override
    public boolean supportsFullOuterJoins() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsFullOuterJoins();
        }
        return false;
    }

    @Override
    public boolean supportsLimitedOuterJoins() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsLimitedOuterJoins();
        }
        return false;
    }

    @Override
    public String getSchemaTerm() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getSchemaTerm();
        }
        return null;
    }

    @Override
    public String getProcedureTerm() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getProcedureTerm();
        }
        return null;
    }

    @Override
    public String getCatalogTerm() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getCatalogTerm();
        }
        return null;
    }

    @Override
    public boolean isCatalogAtStart() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isCatalogAtStart();
        }
        return false;
    }

    @Override
    public String getCatalogSeparator() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getCatalogSeparator();
        }
        return null;
    }

    @Override
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsSchemasInDataManipulation();
        }
        return false;
    }

    @Override
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsSchemasInProcedureCalls();
        }
        return false;
    }

    @Override
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsSchemasInTableDefinitions();
        }
        return false;
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsSchemasInIndexDefinitions();
        }
        return false;
    }

    @Override
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsSchemasInPrivilegeDefinitions();
        }
        return false;
    }

    @Override
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsCatalogsInDataManipulation();
        }
        return false;
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsCatalogsInProcedureCalls();
        }
        return false;
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsCatalogsInTableDefinitions();
        }
        return false;
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsCatalogsInIndexDefinitions();
        }
        return false;
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsCatalogsInPrivilegeDefinitions();
        }
        return false;
    }

    @Override
    public boolean supportsPositionedDelete() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsPositionedDelete();
        }
        return false;
    }

    @Override
    public boolean supportsPositionedUpdate() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsPositionedUpdate();
        }
        return false;
    }

    @Override
    public boolean supportsSelectForUpdate() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsSelectForUpdate();
        }
        return false;
    }

    @Override
    public boolean supportsStoredProcedures() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsStoredProcedures();
        }
        return false;
    }

    @Override
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsSubqueriesInComparisons();
        }
        return false;
    }

    @Override
    public boolean supportsSubqueriesInExists() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsSubqueriesInExists();
        }
        return false;
    }

    @Override
    public boolean supportsSubqueriesInIns() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsSubqueriesInIns();
        }
        return false;
    }

    @Override
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsSubqueriesInQuantifieds();
        }
        return false;
    }

    @Override
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsCorrelatedSubqueries();
        }
        return false;
    }

    @Override
    public boolean supportsUnion() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsUnion();
        }
        return false;
    }

    @Override
    public boolean supportsUnionAll() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsUnionAll();
        }
        return false;
    }

    @Override
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        return false;
    }

    @Override
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsOpenCursorsAcrossRollback();
        }
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsOpenCursorsAcrossCommit();
        }
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsOpenStatementsAcrossRollback();
        }
        return false;
    }

    @Override
    public int getMaxBinaryLiteralLength() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxBinaryLiteralLength();
        }
        return 0;
    }

    @Override
    public int getMaxCharLiteralLength() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxCharLiteralLength();
        }
        return 0;
    }

    @Override
    public int getMaxColumnNameLength() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxColumnNameLength();
        }
        return 0;
    }

    @Override
    public int getMaxColumnsInGroupBy() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxColumnsInGroupBy();
        }
        return 0;
    }

    @Override
    public int getMaxColumnsInIndex() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxColumnsInIndex();
        }
        return 0;
    }

    @Override
    public int getMaxColumnsInOrderBy() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxColumnsInOrderBy();
        }
        return 0;
    }

    @Override
    public int getMaxColumnsInSelect() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxColumnsInSelect();
        }
        return 0;
    }

    @Override
    public int getMaxColumnsInTable() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxColumnsInTable();
        }
        return 0;
    }

    @Override
    public int getMaxConnections() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxConnections();
        }
        return 0;
    }

    @Override
    public int getMaxCursorNameLength() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxCursorNameLength();
        }
        return 0;
    }

    @Override
    public int getMaxIndexLength() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxIndexLength();
        }
        return 0;
    }

    @Override
    public int getMaxSchemaNameLength() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxSchemaNameLength();
        }
        return 0;
    }

    @Override
    public int getMaxProcedureNameLength() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxProcedureNameLength();
        }
        return 0;
    }

    @Override
    public int getMaxCatalogNameLength() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxCatalogNameLength();
        }
        return 0;
    }

    @Override
    public int getMaxRowSize() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxRowSize();
        }
        return 0;
    }

    @Override
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isDoesMaxRowSizeIncludeBlobs();
        }
        return false;
    }

    @Override
    public int getMaxStatementLength() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxStatementLength();
        }
        return 0;
    }

    @Override
    public int getMaxStatements() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxStatements();
        }
        return 0;
    }

    @Override
    public int getMaxTableNameLength() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxTableNameLength();
        }
        return 0;
    }

    @Override
    public int getMaxTablesInSelect() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxTablesInSelect();
        }
        return 0;
    }

    @Override
    public int getMaxUserNameLength() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getMaxUserNameLength();
        }
        return 0;
    }

    @Override
    public int getDefaultTransactionIsolation() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getDefaultTransactionIsolation();
        }
        return 0;
    }

    @Override
    public boolean supportsTransactions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsTransactions();
        }
        return false;
    }

    @Override
    public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
        return false;
    }

    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsDataDefinitionAndDataManipulationTransactions();
        }
        return false;
    }

    @Override
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsDataManipulationTransactionsOnly();
        }
        return false;
    }

    @Override
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isDataDefinitionCausesTransactionCommit();
        }
        return false;
    }

    @Override
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isDataDefinitionIgnoredInTransactions();
        }
        return false;
    }

    @Override
    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        if (isOK()) {
            return parseFromListOfUltraResultRow(ultraDatabaseMetaData.getSchemas());
        }
        return null;
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        if (isOK()) {
            return parseFromListOfUltraResultRow(ultraDatabaseMetaData.getCatalogs());
        }
        return null;
    }

    @Override
    public ResultSet getTableTypes() throws SQLException {
        if (isOK()) {
            return parseFromListOfUltraResultRow(ultraDatabaseMetaData.getTableTypes());
        }
        return null;
    }

    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getTypeInfo() throws SQLException {
        if (isOK()) {
            return parseFromListOfUltraResultRow(ultraDatabaseMetaData.getTypeInfo());
        }
        return null;
    }

    @Override
    public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        return null;
    }

    @Override
    public boolean supportsResultSetType(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
        return false;
    }

    @Override
    public boolean ownUpdatesAreVisible(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean ownDeletesAreVisible(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean ownInsertsAreVisible(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean othersUpdatesAreVisible(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean othersDeletesAreVisible(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean othersInsertsAreVisible(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean updatesAreDetected(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean deletesAreDetected(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean insertsAreDetected(int type) throws SQLException {
        return false;
    }

    @Override
    public boolean supportsBatchUpdates() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsBatchUpdates();
        }
        return false;
    }

    @Override
    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
        return null;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (isOK()) {
            return this.connection;
        }
        return null;
    }

    @Override
    public boolean supportsSavepoints() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsSavepoints();
        }
        return false;
    }

    @Override
    public boolean supportsNamedParameters() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsNamedParameters();
        }
        return false;
    }

    @Override
    public boolean supportsMultipleOpenResults() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsMultipleOpenResults();
        }
        return false;
    }

    @Override
    public boolean supportsGetGeneratedKeys() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsGetGeneratedKeys();
        }
        return false;
    }

    @Override
    public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
        return null;
    }

    @Override
    public boolean supportsResultSetHoldability(int holdability) throws SQLException {
        return false;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getResultSetHoldability();
        }
        return 0;
    }

    @Override
    public int getDatabaseMajorVersion() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getDatabaseMajorVersion();
        }
        return 0;
    }

    @Override
    public int getDatabaseMinorVersion() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getDatabaseMinorVersion();
        }
        return 0;
    }

    @Override
    public int getJDBCMajorVersion() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getJdbcMajorVersion();
        }
        return 0;
    }

    @Override
    public int getJDBCMinorVersion() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getJdbcMinorVersion();
        }
        return 0;
    }

    @Override
    public int getSQLStateType() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getSqlStateType();
        }
        return 0;
    }

    @Override
    public boolean locatorsUpdateCopy() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isLocatorsUpdateCopy();
        }
        return false;
    }

    @Override
    public boolean supportsStatementPooling() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsStatementPooling();
        }
        return false;
    }

    @Override
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.getRowIdLifetime();
        }
        return null;
    }

    @Override
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        if (isOK()) {
            return parseFromListOfUltraResultRow(ultraDatabaseMetaData.getSchemas());
        }
        return null;
    }

    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isSupportsStoredFunctionsUsingCallSyntax();
        }
        return false;
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isAutoCommitFailureClosesAllResultSets();
        }
        return false;
    }

    @Override
    public ResultSet getClientInfoProperties() throws SQLException {
        if (isOK()) {
            return parseFromListOfUltraResultRow(ultraDatabaseMetaData.getClientInfoProperties());
        }
        return null;
    }

    @Override
    public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
        return null;
    }

    @Override
    public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        return null;
    }

    @Override
    public boolean generatedKeyAlwaysReturned() throws SQLException {
        if (isOK()) {
            return ultraDatabaseMetaData.isGeneratedKeyAlwaysReturned();
        }
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
