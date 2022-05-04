package com.ane56.xsql.common.model;

import lombok.*;

import java.io.Serializable;
import java.sql.RowIdLifetime;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/28 10:10 AM
 * @Desc: TODOLIST
 * 1. TODO 增加Connection对象
 * 2. TODO resultSet转换
 * @Version: v1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class UltraDatabaseMetaData implements Serializable {
    private static final long serialVersionUID = -3651916755546911965L;

//    private boolean supportsTransactionIsolationLevel;
    private boolean allProceduresAreCallable;

    private String catalogTerm;

    public boolean allTablesAreSelectable;

    public String URL;

    public String userName;

    public boolean isReadOnly;

    public boolean nullsAreSortedHigh;

    public boolean nullsAreSortedLow;

    public boolean nullsAreSortedAtStart;

    public boolean nullsAreSortedAtEnd;

    public String databaseProductName;

    public String databaseProductVersion;

    public String driverName;

    public String driverVersion;

    public int driverMajorVersion;

    public int driverMinorVersion;

    public boolean usesLocalFiles;

    public boolean usesLocalFilePerTable;

    public boolean supportsMixedCaseIdentifiers;

    public boolean storesUpperCaseIdentifiers;

    public boolean storesLowerCaseIdentifiers;

    public boolean storesMixedCaseIdentifiers;

    public boolean supportsMixedCaseQuotedIdentifiers;

    public boolean storesUpperCaseQuotedIdentifiers;

    public boolean storesLowerCaseQuotedIdentifiers;

    public boolean storesMixedCaseQuotedIdentifiers;

    public String identifierQuoteString;

    public String sqlKeywords;

    public String numericFunctions;

    public String stringFunctions;

    public String systemFunctions;

    public String timeDateFunctions;

    public String searchStringEscape;

    public String extraNameCharacters;

    public boolean supportsAlterTableWithAddColumn;

    public boolean supportsAlterTableWithDropColumn;

    public boolean supportsColumnAliasing;

    public boolean nullPlusNonNullIsNull;

    public boolean supportsConvert;

    public boolean supportsTableCorrelationNames;

    public boolean supportsDifferentTableCorrelationNames;

    public boolean supportsExpressionsInOrderBy;

    public boolean supportsOrderByUnrelated;

    public boolean supportsGroupBy;

    public boolean supportsGroupByUnrelated;

    public boolean supportsGroupByBeyondSelect;

    public boolean supportsLikeEscapeClause;

    public boolean supportsMultipleResultSets;

    public boolean supportsMultipleTransactions;

    public boolean supportsNonNullableColumns;

    public boolean supportsMinimumSQLGrammar;

    public boolean supportsCoreSQLGrammar;

    public boolean supportsExtendedSQLGrammar;

    public boolean supportsANSI92EntryLevelSQL;

    public boolean supportsANSI92IntermediateSQL;

    public boolean supportsANSI92FullSQL;

    public boolean supportsIntegrityEnhancementFacility;

    public boolean supportsOuterJoins;

    public boolean supportsFullOuterJoins;

    public boolean supportsLimitedOuterJoins;

    public String schemaTerm;

    public String procedureTerm;

    public String datalogTerm;

    public boolean isCatalogAtStart;

    public String catalogSeparator;

    public boolean supportsSchemasInDataManipulation;

    public boolean supportsSchemasInProcedureCalls;

    public boolean supportsSchemasInTableDefinitions;

    public boolean supportsSchemasInIndexDefinitions;

    public boolean supportsSchemasInPrivilegeDefinitions;

    public boolean supportsCatalogsInDataManipulation;

    public boolean supportsCatalogsInProcedureCalls;

    public boolean supportsCatalogsInTableDefinitions;

    public boolean supportsCatalogsInIndexDefinitions;

    public boolean supportsCatalogsInPrivilegeDefinitions;

    public boolean supportsPositionedDelete;

    public boolean supportsPositionedUpdate;

    public boolean supportsSelectForUpdate;

    public boolean supportsStoredProcedures;

    public boolean supportsSubqueriesInComparisons;

    public boolean supportsSubqueriesInExists;

    public boolean supportsSubqueriesInIns;

    public boolean supportsSubqueriesInQuantifieds;

    public boolean supportsCorrelatedSubqueries;

    public boolean supportsUnion;

    public boolean supportsUnionAll;

    public boolean supportsOpenCursorsAcrossCommit;

    public boolean supportsOpenCursorsAcrossRollback;

    public boolean supportsOpenStatementsAcrossCommit;

    public boolean supportsOpenStatementsAcrossRollback;

    public int maxBinaryLiteralLength;

    public int maxCharLiteralLength;

    public int maxColumnNameLength;

    public int maxColumnsInGroupBy;

    public int maxColumnsInIndex;

    public int maxColumnsInOrderBy;

    public int maxColumnsInSelect;

    public int maxColumnsInTable;

    public int maxConnections;

    public int maxCursorNameLength;

    public int maxIndexLength;

    public int maxSchemaNameLength;

    public int maxProcedureNameLength;

    public int maxCatalogNameLength;

    public int maxRowSize;

    public boolean doesMaxRowSizeIncludeBlobs;

    public int maxStatementLength;

    public int maxStatements;

    public int maxTableNameLength;

    public int maxTablesInSelect;

    public int maxUserNameLength;

    public int defaultTransactionIsolation;

    public boolean supportsTransactions;

    public boolean supportsDataDefinitionAndDataManipulationTransactions;

    public boolean supportsDataManipulationTransactionsOnly;

    public boolean dataDefinitionCausesTransactionCommit;

    public boolean dataDefinitionIgnoredInTransactions;


    public List<UltraResultRow> schemas;

    public List<UltraResultRow> catalogs;

    public List<UltraResultRow> tableTypes;

    public List<UltraResultRow> typeInfo;

    public boolean supportsBatchUpdates;

    public boolean supportsSavepoints;

    public boolean supportsNamedParameters;

    public boolean supportsMultipleOpenResults;

    public boolean supportsGetGeneratedKeys;

    public int resultSetHoldability;

    public int databaseMajorVersion;

    public int databaseMinorVersion;

    public int jdbcMajorVersion;

    public int jdbcMinorVersion;

    public int sqlStateType;

    public boolean locatorsUpdateCopy;

    public boolean supportsStatementPooling;

    public RowIdLifetime rowIdLifetime;

    public boolean supportsStoredFunctionsUsingCallSyntax;

    public boolean autoCommitFailureClosesAllResultSets;

    public List<UltraResultRow> clientInfoProperties;

    public boolean generatedKeyAlwaysReturned;
}
