package com.ane56.engine.jdbc.utils;

public class PathUtils {
    public static String checkAndCombinePath(String... pathList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String path : pathList) {
            path = path.replace("\\+", "/");
            path = path.replace("/+", "/");
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            stringBuilder.append(path);
        }
        return stringBuilder.toString();
    }
}
