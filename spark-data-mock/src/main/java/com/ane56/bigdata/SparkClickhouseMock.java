package com.ane56.bigdata;

/**
 * @Author: zhangxinsen
 * @Date: 2022/5/7 5:02 PM
 * @Desc:
 * @Version: v1.0
 */

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

/**
 * 从HDFS上读取相应的数据, 并写入到clickhouse中
 */
public class SparkClickhouseMock {
    public static void main(String[] args) {
        String url = "jdbc:clickhouse://127.0.0.1:9000";
        String driver = "com.github.housepower.jdbc.ClickHouseDriver";

        SparkSession spark = SparkSession
                .builder()
                .appName("Spark Presto Data Mock")
                .master("local[*]")
                .enableHiveSupport()
                .getOrCreate();

        Dataset<Row> dataset = spark.sql("select site_id,site_name,weight_interval_start,weight_interval_end," +
                "overall_calc_weight, stat_date from dev_ads.ads_management_daily_site_overall " +
                "where site_id is not null");
        // dataset.toJavaRDD().collect().forEach(System.out::println);

        Properties properties = new Properties();
        properties.put("driver", driver);


        dataset.write()
                .mode(SaveMode.Append)
                .option("batchsize", "20000")
                .jdbc(url, "ane.ads_management_daily_site_overall", properties);
        spark.close();
    }
}
