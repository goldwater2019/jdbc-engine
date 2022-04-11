package com.ane56.engine.jdbc.utils;

public class StringUtils {
    public static boolean isBlank(String content) {
        if (content == null) {
            return true;
        }
        if (content.length() == 0) {
            return true;
        }
        if (content.trim().length() == 0) {
            return true;
        }
        return false;
    }
}
