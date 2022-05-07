package com.ane56.bigdata;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: zhangxinsen
 * @Date: 2022/5/6 3:59 PM
 * @Desc:
 * @Version: v1.0
 */

public class SparkPrestoMock {
    public static void main(String[] args) {
        String filepath = args[0];
        SparkSession spark = SparkSession
                .builder()
                .appName("Spark Presto Data Mock")
                .master("local[*]")
                .enableHiveSupport()
                .getOrCreate();

        for (int i = 0; i < 365 * 10; i++) {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDateStr = sdf.format(new Date(d.getTime() - i * 24L * 60L * 60L * 1000L));
            System.out.println(formattedDateStr);
            mockAndWriteData(spark, filepath, formattedDateStr);
        }
        spark.close();
    }

    private static void mockAndWriteData(SparkSession spark, String filepath, String formattedDateStr) {
        Dataset<Row> siteData = spark.read()
                .option("header", true)
                .csv("file://" + filepath);
        siteData.printSchema();
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
        spark.sql("insert into dev_ads.ads_management_daily_site_overall partition(stat_date='" + formattedDateStr
                + "') " +
                "(site_id, site_name, weight_interval_start, weight_interval_end, overall_calc_weight) " +
                " select site_id, site_name,weight_interval_start, weight_interval_end, overall_calc_weight" +
                "  from partitionRow distribute by 1");
    }
}
