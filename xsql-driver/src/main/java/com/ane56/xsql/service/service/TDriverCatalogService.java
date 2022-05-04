package com.ane56.xsql.service.service;

import com.ane56.xsql.service.entity.TDriverCatalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * catalog管理表(TDriverCatalog)表服务接口
 *
 * @author xinsen
 * @since 2022-05-04 10:08:06
 */
public interface TDriverCatalogService {

    /**
     * 通过ID查询单条数据
     *
     * @param catalogId 主键
     * @return 实例对象
     */
    TDriverCatalog queryById(Integer catalogId);

    /**
     * 分页查询
     *
     * @param tDriverCatalog 筛选条件
     * @param pageRequest    分页对象
     * @return 查询结果
     */
    Page<TDriverCatalog> queryByPage(TDriverCatalog tDriverCatalog, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tDriverCatalog 实例对象
     * @return 实例对象
     */
    TDriverCatalog insert(TDriverCatalog tDriverCatalog);

    /**
     * 修改数据
     *
     * @param tDriverCatalog 实例对象
     * @return 实例对象
     */
    TDriverCatalog update(TDriverCatalog tDriverCatalog);

    /**
     * 通过主键删除数据
     *
     * @param catalogId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer catalogId);

}
