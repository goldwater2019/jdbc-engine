package com.ane56.engine.jdbc.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {
    public static Map<String, String> loadAsMap(String propertiesFilePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(propertiesFilePath);
        Properties properties = new Properties();
        properties.load(inputStream);
        Map<String, String> map = new HashMap<String, String>((Map) properties);
        return map;
    }
}