package com.ane56.engine.jdbc.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
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

    public static Map<String, String> loadConfAsMap(String confFilePath) {
        Map<String, String> configMap = new HashMap<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    confFilePath
            ));
            String line = reader.readLine();
            while (line != null) {
                line = line.trim();
                if (!line.startsWith("#")) {
                    line = line.replaceAll("\\s+", "=");
                    line = line.replaceAll("=+", "=");
                    String[] split = line.split("=");
                    if (split.length == 2) {
                        configMap.put(split[0], split[1]);
                    }
                }
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configMap;
    }

    public static void main(String[] args) {
        Map<String, String> stringStringMap = loadConfAsMap("C:/workspace/jdbc-engine/conf/jdbc-engine-defualt.conf");
        for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
            System.out.println("key: " + stringStringEntry.getKey() + ", value: " + stringStringEntry.getValue());
        }
    }
}