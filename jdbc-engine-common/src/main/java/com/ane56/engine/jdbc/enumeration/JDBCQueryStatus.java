package com.ane56.engine.jdbc.enumeration;

import com.ane56.engine.jdbc.thrit.enumeration.TJDBCQueryStatus;

public enum JDBCQueryStatus {
    OK(0),
    FAILED(1);

    private final int value;

    JDBCQueryStatus(int value) {
        this.value = value;
    }

    public static JDBCQueryStatus parseFromTJDBCQueryStatus(TJDBCQueryStatus tjdbcQueryStatus) {
        if (tjdbcQueryStatus == TJDBCQueryStatus.OK) {
            return JDBCQueryStatus.OK;
        }
        return JDBCQueryStatus.FAILED;
    }

    public TJDBCQueryStatus asTJDBCQueryStatus() {
        if (this.value == 0) {
            return TJDBCQueryStatus.OK;
        }
        return TJDBCQueryStatus.FAILED;
    }
}
