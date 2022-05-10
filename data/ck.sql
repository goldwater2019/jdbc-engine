select count(distinct stat_date)
from ane.ads_management_daily_site_overall;

select count(1)
from ane.ads_management_daily_site_overall;

select weight_interval_start, weight_interval_end, sum(overall_calc_weight)
from (select weight_interval_start, weight_interval_end, overall_calc_weight
      from ane.ads_management_daily_site_overall
      where stat_date > '2012-05-01'
        and stat_date < '2013-05-01'
        and site_id in
            (
             779048, 779785, 780081, 782512, 782524, 783017, 783293, 783642, 747993, 748239, 759549, 7758, 761280)
     ) as t
group by weight_interval_start, weight_interval_end;


drop table if exists ane.ads_management_daily_overall;
CREATE TABLE if not exists ane.ads_management_daily_overall
(
    fith_lvl_org_brnch_id      Int64 comment '片区ID',
    fith_lvl_org_brnch_nm      String comment '片区名称',
    foth_lvl_busi_org_brnch_id Int64 comment '分拨业务ID',
    foth_lvl_busi_org_brnch_nm String comment '分拨业务名称',
    foth_lvl_org_brnch_id      Int64 comment '分拨ID',
    foth_lvl_org_brnch_nm      String comment '分拨名称',
    fst_lvl_org_brnch_id       Int64 comment '总部ID',
    fst_lvl_org_brnch_nm       String comment '总部名称',
    org_brnch_custom_type      Int64 comment '阻止机构自定义类型',
    org_brnch_lvl              Int64 comment '组织机构等级',
    org_brnch_path             String comment '组织机构路径',
    overall_calc_weight        Float64 comment '结算重量汇总重量',
    product_type               Int32 comment '货物类型',
    secd_lvl_org_brnch_id      Int64 comment '省区ID',
    secd_lvl_org_brnch_nm      String comment '省区名称',
    svth_lvl_org_brnch_id      Int64 comment '二级网点ID',
    svth_lvl_org_brnch_nm      String comment '二级网点名称',
    sxth_lvl_org_brnch_id      Int64 comment '一级网点iD',
    sxth_lvl_org_brnch_nm      String comment '一级网点名称',
    thrd_lvl_org_brnch_id      Int64 comment '大区ID',
    thrd_lvl_org_brnch_nm      String comment '大区名称',
    weight_interval_end        Int32 comment '公斤段区间结束值',
    weight_interval_start      Int32 comment '公斤段区间开始值',
    `stat_date`                Date COMMENT '统计日期'
)
    ENGINE = MergeTree()
        primary key svth_lvl_org_brnch_id
--         ORDER BY (secd_lvl_org_brnch_id, thrd_lvl_org_brnch_id, foth_lvl_org_brnch_id, fith_lvl_org_brnch_id,
--                   sxth_lvl_org_brnch_id, svth_lvl_org_brnch_id)
        order by svth_lvl_org_brnch_id
        PARTITION BY stat_date;



drop table if exists ane.dim_second_site_info;
CREATE TABLE if not exists ane.dim_second_site_info
(
    fith_lvl_org_brnch_id      Int64 comment '片区ID',
    fith_lvl_org_brnch_nm      String comment '片区名称',
    foth_lvl_busi_org_brnch_id Int64 comment '分拨业务ID',
    foth_lvl_busi_org_brnch_nm String comment '分拨业务名称',
    foth_lvl_org_brnch_id      Int64 comment '分拨ID',
    foth_lvl_org_brnch_nm      String comment '分拨名称',
    fst_lvl_org_brnch_id       Int64 comment '总部ID',
    fst_lvl_org_brnch_nm       String comment '总部名称',
    org_brnch_custom_type      Int64 comment '阻止机构自定义类型',
    org_brnch_lvl              Int64 comment '组织机构等级',
    org_brnch_path             String comment '组织机构路径',
    secd_lvl_org_brnch_id      Int64 comment '省区ID',
    secd_lvl_org_brnch_nm      String comment '省区名称',
    svth_lvl_org_brnch_id      Int64 comment '二级网点ID',
    svth_lvl_org_brnch_nm      String comment '二级网点名称',
    sxth_lvl_org_brnch_id      Int64 comment '一级网点iD',
    sxth_lvl_org_brnch_nm      String comment '一级网点名称',
    thrd_lvl_org_brnch_id      Int64 comment '大区ID',
    thrd_lvl_org_brnch_nm      String comment '大区名称'
)
    ENGINE = MergeTree()
        order by (secd_lvl_org_brnch_id, thrd_lvl_org_brnch_id, foth_lvl_org_brnch_id, fith_lvl_org_brnch_id,
                  sxth_lvl_org_brnch_id, svth_lvl_org_brnch_id);



select count(distinct stat_date)
from ane.ads_management_daily_overall_dev;


CREATE TABLE if not exists ane.ads_management_daily_overall_dev_partition ENGINE = MergeTree()
    PARTITION BY (product_type, stat_date)
    ORDER BY (svth_lvl_org_brnch_id)
as
select fith_lvl_org_brnch_id,
       fith_lvl_org_brnch_nm,
       foth_lvl_busi_org_brnch_id,
       foth_lvl_busi_org_brnch_nm,
       foth_lvl_org_brnch_id,
       foth_lvl_org_brnch_nm,
       fst_lvl_org_brnch_id,
       fst_lvl_org_brnch_nm,
       org_brnch_custom_type,
       org_brnch_lvl,
       org_brnch_path,
       overall_calc_weight,
       product_type,
       secd_lvl_org_brnch_id,
       secd_lvl_org_brnch_nm,
       svth_lvl_org_brnch_id,
       svth_lvl_org_brnch_nm,
       sxth_lvl_org_brnch_id,
       sxth_lvl_org_brnch_nm,
       thrd_lvl_org_brnch_id,
       thrd_lvl_org_brnch_nm,
       weight_interval_end,
       weight_interval_start,
       stat_date
from ane.ads_management_daily_overall;

select fith_lvl_org_brnch_id,
       fith_lvl_org_brnch_nm,
       foth_lvl_busi_org_brnch_id,
       foth_lvl_busi_org_brnch_nm,
       foth_lvl_org_brnch_id,
       foth_lvl_org_brnch_nm,
       fst_lvl_org_brnch_id,
       fst_lvl_org_brnch_nm,
       org_brnch_custom_type,
       org_brnch_lvl,
       org_brnch_path,
       overall_calc_weight,
       product_type,
       secd_lvl_org_brnch_id,
       secd_lvl_org_brnch_nm,
       svth_lvl_org_brnch_id,
       svth_lvl_org_brnch_nm,
       sxth_lvl_org_brnch_id,
       sxth_lvl_org_brnch_nm,
       thrd_lvl_org_brnch_id,
       thrd_lvl_org_brnch_nm,
       weight_interval_end,
       weight_interval_start,
       stat_date
from ane.ads_management_daily_overall;

show create table ane.ads_management_daily_overall;

describe table ane.ads_management_daily_overall;

describe table ane.ads_management_daily_site_overall;


create table ane.dim_second_site_info engine = Memory() as
select distinct svth_lvl_org_brnch_id,
                fith_lvl_org_brnch_id,
                fith_lvl_org_brnch_nm,
                foth_lvl_busi_org_brnch_id,
                foth_lvl_busi_org_brnch_nm,
                foth_lvl_org_brnch_id,
                foth_lvl_org_brnch_nm,
                fst_lvl_org_brnch_id,
                fst_lvl_org_brnch_nm,
                org_brnch_custom_type,
                org_brnch_lvl,
                org_brnch_path,
                overall_calc_weight,
                secd_lvl_org_brnch_id,
                secd_lvl_org_brnch_nm,
                svth_lvl_org_brnch_nm,
                sxth_lvl_org_brnch_id,
                sxth_lvl_org_brnch_nm,
                thrd_lvl_org_brnch_id,
                thrd_lvl_org_brnch_nm
from ane.ads_management_daily_overall;



select sum(overall_calc_weight), weight_interval_start, weight_interval_end, fith_lvl_org_brnch_id
from (select overall_calc_weight, weight_interval_start, weight_interval_end, fith_lvl_org_brnch_id
      from ane.ads_management_daily_overall_dev
      where product_type = 0
        and stat_date > '2022-05-03'
        and stat_date < '2022-05-09'
     ) as t
group by fith_lvl_org_brnch_id, weight_interval_start, weight_interval_end;


select svth_lvl_org_brnch_nm, weight_interval_start, weight_interval_end, sum(overall_calc_weight)
from ane.dim_second_site_info l
         left join (select overall_calc_weight, weight_interval_start, weight_interval_end, svth_lvl_org_brnch_id
                    from ane.ads_management_daily_overall_dev
                    where product_type = 0
                      and stat_date > '2022-05-03'
                      and stat_date < '2022-05-09'
) r
                   on l.svth_lvl_org_brnch_id = r.svth_lvl_org_brnch_id
group by weight_interval_start, weight_interval_end, svth_lvl_org_brnch_nm;


-- 要看所有片区的数据

select fith_lvl_org_brnch_id,
       weight_interval_start,
       weight_interval_end,
       sum(overall_calc_weight)
from (
         select distinct fith_lvl_org_brnch_id, svth_lvl_org_brnch_id from ane.dim_second_site_info)
         as l
         left join (
    select weight_interval_start, weight_interval_end, overall_calc_weight, svth_lvl_org_brnch_id
    from ane.ads_management_daily_overall_dev
    where stat_date > '2021-04-01'
      and stat_date < '2021-04-08'
      and product_type = 0
) r on l.svth_lvl_org_brnch_id = r.svth_lvl_org_brnch_id
group by fith_lvl_org_brnch_id, weight_interval_start, weight_interval_end;



select fith_lvl_org_brnch_id, r.weight_interval_start, r.weight_interval_end, r.overall_calc_weight
from (select fith_lvl_org_brnch_id, svth_lvl_org_brnch_id from ane.dim_second_site_info) l
         left join (select weight_interval_start, weight_interval_end, svth_lvl_org_brnch_id, overall_calc_weight
                    from ane.ads_management_daily_overall_dev
                    where product_type = 0
                      and stat_date > '2021-05-01'
                      and stat_date < '2022-05-01'
) r
                   on l.svth_lvl_org_brnch_id = r.svth_lvl_org_brnch_id;