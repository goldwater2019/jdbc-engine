package com.ane56.bigdata;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @Author: zhangxinsen
 * @Date: 2022/5/8 1:59 AM
 * @Desc:
 * @Version: v1.0
 */

public class SparkClickhouseOriginalMock {
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

        String year = properties.getProperty("year");
        int nYear = year != null ? Integer.parseInt(year) : 10;

        for (int i = 0; i < 365 * nYear; i++) {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDateStr = sdf.format(new Date(d.getTime() - i * 24L * 60L * 60L * 1000L));
            System.out.println(formattedDateStr);
            mockAndWriteData(spark, formattedDateStr, properties);
        }
        spark.close();
    }

    private static void mockAndWriteData(SparkSession spark,
                                         String formattedDateStr, Properties properties) {
        Dataset<Row> siteData = spark.read()
                .option("header", true)
                .csv("file://" + properties.getProperty("data"));
        siteData.registerTempTable("site_info");
        Dataset<Row> phaseOne = spark.sql("select int(site_id), site_name, round(random() * 10000, 3) as overall_calc_weight," +
                "0 as weight_interval_start," +
                "70 as weight_interval_end from site_info");

        phaseOne.registerTempTable("phaseOne");

        Dataset<Row> phaseTwo = spark.sql("select int(site_id), site_name, round(random() * 10000, 3) as overall_calc_weight," +
                "70 as weight_interval_start," +
                "800 as weight_interval_end from site_info");

        phaseTwo.registerTempTable("phaseTwo");

        Dataset<Row> phaseThree = spark.sql("select int(site_id), site_name, round(random() * 10000, 3) as overall_calc_weight," +
                "800 as weight_interval_start," +
                "-1 as weight_interval_end from site_info");

        phaseThree.registerTempTable("phaseThree");
        Dataset<Row> partitionRow = spark.sql("select * from (" +
                "(select * from phaseOne " +
                "union all select * from phaseTwo)" +
                " union all select * from phaseThree) ");
        partitionRow.registerTempTable("partitionRow");
        String querySql = "select site_id, " +
                "site_name, " +
                "weight_interval_start, " +
                "weight_interval_end, " +
                "overall_calc_weight, " +
                "to_date('" + formattedDateStr + "') as stat_date " +
                "from partitionRow " +
                "where site_id is not null";
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
                .jdbc(url, "ane.ads_management_daily_site_overall", properties);
    }
}
