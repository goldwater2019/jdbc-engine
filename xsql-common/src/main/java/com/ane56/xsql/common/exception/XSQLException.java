package com.ane56.xsql.common.exception;

public class XSQLException extends Exception {
    public XSQLException(String message, Throwable cause) {
        super(message, cause);
    }

    public XSQLException(String message) {
        super(message);
    }

    public XSQLException(Throwable cause) {
        super(cause);
    }
}
