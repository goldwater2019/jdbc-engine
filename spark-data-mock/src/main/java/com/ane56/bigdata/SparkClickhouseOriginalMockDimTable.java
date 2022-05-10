package com.ane56.bigdata;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

/**
 * @Author: zhangxinsen
 * @Date: 2022/5/8 1:59 AM
 * @Desc:
 * @Version: v1.0
 */

public class SparkClickhouseOriginalMockDimTable {
    public static void main(String[] args) {
        Properties properties = new Properties();
        boolean isValue = false;
        String key = null;
        for (String arg : args) {
            arg = arg.trim();
            if (isValue) {
                properties.put(key, arg);
                isValue = false;
            }
            if (arg.startsWith("--")) {
                isValue = true;
                key = arg.replace("--", "");
            }
        }
        properties.put("driver", "com.github.housepower.jdbc.ClickHouseDriver");
        SparkSession spark = SparkSession
                .builder()
                .appName("Spark Clickhouse Data Mock")
                .master("local[*]")
                .getOrCreate();

        mockAndWriteData(spark, properties);
        spark.close();
    }

    private static void mockAndWriteData(SparkSession spark, Properties properties) {
        Dataset<Row> dimTable = spark.read()
                .option("header", true)
                .csv("file://" + properties.getProperty("data"));
        dimTable.registerTempTable("dimTable");
        dimTable.printSchema();
        String querySql = "select" +
                "    cast(fith_lvl_org_brnch_id as int), " +
                "    fith_lvl_org_brnch_nm, " +
                "    cast(foth_lvl_busi_org_brnch_id as int), " +
                "    foth_lvl_busi_org_brnch_nm, " +
                "    cast(foth_lvl_org_brnch_id as int), " +
                "    foth_lvl_org_brnch_nm, " +
                "    cast(fst_lvl_org_brnch_id as int), " +
                "    fst_lvl_org_brnch_nm, " +
                "    cast(org_brnch_custom_type as int), " +
                "    cast(org_brnch_lvl as int), " +
                "    org_brnch_path, " +
                "    cast(secd_lvl_org_brnch_id as int), " +
                "    secd_lvl_org_brnch_nm, " +
                "    cast(svth_lvl_org_brnch_id as int), " +
                "    svth_lvl_org_brnch_nm, " +
                "    cast(sxth_lvl_org_brnch_id as int), " +
                "    sxth_lvl_org_brnch_nm, " +
                "    cast(thrd_lvl_org_brnch_id as int), " +
                "    thrd_lvl_org_brnch_nm " +
                "    from dimTable";
        Dataset<Row> sql = spark.sql(querySql);
        Properties prop = new Properties();
        prop.put("driver", properties.getProperty("driver"));
        if (properties.getProperty("user") != null) {
            prop.put("user", properties.getProperty("user"));
        }
        if (properties.getProperty("password") != null) {
            prop.put("password", properties.getProperty("password"));
        }

        String url = "jdbc:clickhouse://127.0.0.1:9000";
        if (properties.getProperty("host") != null) {
            String host = properties.getProperty("host");
            if (properties.getProperty("port") != null) {
                url = "jdbc:clickhouse://" + host + ":" + properties.getProperty("port");
            } else {
                url = "jdbc:clickhouse://" + host + ":9000";
            }
        }
        sql.write()
                .mode(SaveMode.Append)
                .option("batchsize", "20000")
                .jdbc(url, "ane.dim_second_site_info", properties);
//        sql.sample(0.05).toJavaRDD().collect().forEach(System.out::println);
    }
}
