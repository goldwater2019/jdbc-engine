package com.ane56.engine.jdbc.utils;

import com.ane56.engine.jdbc.exception.JDBCEngineException;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/14 2:58 PM
 * @Desc:
 * @Version: v1.0
 */

@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
public class LogUtils {

    private static volatile LogUtils singleton;

    public static LogUtils getInstance() throws JDBCEngineException {
        if (singleton == null) {
            synchronized (LogUtils.class) {
                if (singleton == null) {
                    singleton = new LogUtils();
                }
            }
        }
        return singleton;
    }


    /**
     * 加载指定的properties文件作为日志格式配置
     * @param configDir
     * @param filename
     */
    private void loadLog4jPropertiesWithFileName(String configDir, String filename) {
        String log4jPropertiesPath = PathUtils.checkAndCombinePath(configDir, filename);
        File log4jPropertiesFile = new File(log4jPropertiesPath);
        if (log4jPropertiesFile.isFile()) {
            Properties properties = null;
            FileInputStream fs = null;
            try {
                // 从配置文件中读取配置信息
                properties = new Properties();
                fs = new FileInputStream(log4jPropertiesPath);
                properties.load(fs);
                PropertyConfigurator.configure(properties);
                fs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载日志格式
     * @param configDir
     */
    public void loadLog4jProperties(String configDir) {
        String log4jPropertiesPath = PathUtils.checkAndCombinePath(configDir, "log4j.properties");
        File log4jPropertiesFile = new File(log4jPropertiesPath);
        if (log4jPropertiesFile.exists() && log4jPropertiesFile.isFile()) {
            loadLog4jPropertiesWithFileName(configDir, "log4j.properties");
        } else {
            String log4jPropertiesTemplatePath = PathUtils.checkAndCombinePath(configDir, "log4j.properties.template");
            File log4jPropertiesTemplateFile = new File(log4jPropertiesTemplatePath);
            if (log4jPropertiesTemplateFile.exists() && log4jPropertiesTemplateFile.isFile()){
                loadLog4jPropertiesWithFileName(configDir, "log4j.properties.template");
            }
        }
    }
}
