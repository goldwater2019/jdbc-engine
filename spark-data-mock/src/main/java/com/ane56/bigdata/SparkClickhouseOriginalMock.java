package com.ane56.bigdata;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
        int nYear = year != null ? Integer.parseInt(year) : 1;

        for (int i = 0; i < 365 * nYear; i++) {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDateStr = sdf.format(new Date(d.getTime() - i * 24L * 60L * 60L * 1000L));
            System.out.println(formattedDateStr);
            mockAndWriteData(spark, formattedDateStr, properties);
            String isBreakable = properties.getProperty("break");
            if (isBreakable != null && isBreakable.equals("true")) {
                break;
            }
        }
        spark.close();
    }

    private static void mockAndWriteData(SparkSession spark,
                                         String formattedDateStr, Properties properties) {
        Dataset<Row> siteData = spark.read()
//                .option("header", true)
                .json("file://" + properties.getProperty("data"));
        siteData.registerTempTable("site_info");
        Dataset<Row> phaseOne = spark.sql("select *, round(random() * 10000, 3) as overall_calc_weight," +
                "0 as weight_interval_start," +
                "70 as weight_interval_end from site_info");

        phaseOne.registerTempTable("phaseOne");

        Dataset<Row> phaseTwo = spark.sql("select *, round(random() * 10000, 3) as overall_calc_weight," +
                "70 as weight_interval_start," +
                "800 as weight_interval_end from site_info");

        phaseTwo.registerTempTable("phaseTwo");

        Dataset<Row> phaseThree = spark.sql("select *, round(random() * 10000, 3) as overall_calc_weight," +
                "800 as weight_interval_start," +
                "-1 as weight_interval_end from site_info");

        phaseThree.registerTempTable("phaseThree");
        Dataset<Row> partitionRow = spark.sql("select * from (" +
                "(select * from phaseOne " +
                "union all select * from phaseTwo)" +
                " union all select * from phaseThree) ");
        partitionRow.registerTempTable("partitionRow");
        String querySql = "select " +
//                "fith_lvl_org_brnch_cd, " +
                "fith_lvl_org_brnch_id, " +
                "fith_lvl_org_brnch_nm, " +
//                "foth_lvl_busi_org_brnch_cd, " +
                "foth_lvl_busi_org_brnch_id, " +
                "foth_lvl_busi_org_brnch_nm, " +
//                "foth_lvl_org_brnch_cd, " +
                "foth_lvl_org_brnch_id, " +
                "foth_lvl_org_brnch_nm, " +
//                "fst_lvl_org_brnch_cd, " +
                "fst_lvl_org_brnch_id, " +
                "fst_lvl_org_brnch_nm, " +
                "org_brnch_custom_type," +
                " org_brnch_lvl, " +
                "org_brnch_path," +
                " overall_calc_weight, " +
                "product_type, " +
//                "secd_lvl_org_brnch_cd, " +
                "secd_lvl_org_brnch_id, " +
                "secd_lvl_org_brnch_nm, " +
//                "svth_lvl_org_brnch_cd, " +
                "svth_lvl_org_brnch_id, " +
                "svth_lvl_org_brnch_nm, " +
//                "sxth_lvl_org_brnch_cd, " +
                "sxth_lvl_org_brnch_id, " +
                "sxth_lvl_org_brnch_nm, " +
//                "thrd_lvl_org_brnch_cd, " +
                "thrd_lvl_org_brnch_id, " +
                "thrd_lvl_org_brnch_nm, " +
                "weight_interval_end, " +
                "weight_interval_start, " +
                "to_date('" + formattedDateStr + "') as stat_date " +
                "from partitionRow ";
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
                .jdbc(url, "ane.ads_management_daily_overall", properties);
//        sql.sample(0.05).toJavaRDD().collect().forEach(System.out::println);
    }
}
