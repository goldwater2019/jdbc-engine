package com.ane56.xsql.service.dao;

import com.ane56.xsql.common.model.UltraCatalog;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * catalog管理表(TDriverCatalog)表数据库访问层
 *
 * @author xinsen
 * @since 2022-05-04 10:08:06
 */
public interface TDriverCatalogDao {

    /**
     * 通过ID查询单条数据
     *
     * @param catalogId 主键
     * @return 实例对象
     */
    UltraCatalog queryById(Integer catalogId);

    /**
     * 查询指定行数据
     *
     * @param ultraCatalog 查询条件
     * @param pageable     分页对象
     * @return 对象列表
     */
    List<UltraCatalog> queryAllByLimit(UltraCatalog ultraCatalog, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param ultraCatalog 查询条件
     * @return 总行数
     */
    long count(UltraCatalog ultraCatalog);

    /**
     * 新增数据
     *
     * @param ultraCatalog 实例对象
     * @return 影响行数
     */
    int insert(UltraCatalog ultraCatalog);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<UltraCatalog> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<UltraCatalog> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TDriverCatalog> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<UltraCatalog> entities);

    /**
     * 修改数据
     *
     * @param ultraCatalog 实例对象
     * @return 影响行数
     */
    int update(UltraCatalog ultraCatalog);

    /**
     * 通过主键删除数据
     *
     * @param catalogId 主键
     * @return 影响行数
     */
    int deleteById(Integer catalogId);

}

