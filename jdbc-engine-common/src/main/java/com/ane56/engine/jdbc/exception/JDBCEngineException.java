package com.ane56.engine.jdbc.exception;

public class JDBCEngineException extends Exception{
    public JDBCEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public JDBCEngineException(String message) {
        super(message);
    }

    public JDBCEngineException(Throwable cause) {
        super(cause);
    }
}
