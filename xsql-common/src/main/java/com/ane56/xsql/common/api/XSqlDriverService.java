package com.ane56.xsql.common.api;

import com.ane56.xsql.common.model.UltraCatalog;

import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/25 1:46 PM
 * @Desc:
 * @Version: v1.0
 */

public interface XSqlDriverService {
    String sayHello(String name);

    /**
     * 通过ID查询单条数据
     *
     * @param catalogId 主键
     * @return 实例对象
     */
    UltraCatalog queryById(Integer catalogId);

    // TODO 分页查询

    /**
     * 新增数据
     *
     * @param ultraCatalog 实例对象
     * @return 实例对象
     */
    UltraCatalog insert(UltraCatalog ultraCatalog);

    /**
     * 修改数据
     *
     * @param ultraCatalog 实例对象
     * @return 实例对象
     */
    UltraCatalog update(UltraCatalog ultraCatalog);

    /**
     * 通过主键删除数据
     *
     * @param catalogId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer catalogId);


    /**
     * 获得所有的catalog对象
     *
     * @return
     */
    List<UltraCatalog> getAllCatalogs();

    /**
     * 根据条件筛选相应的catalog对象
     *
     * @param isForbidden
     * @param isAvailable
     * @return
     */
    List<UltraCatalog> getAllCatalogsWithFilter(Boolean isForbidden, Boolean isAvailable);


    /**
     * 通过传入的ultraCatalog实例的catalogName字段查询实体
     *
     * @param ultraCatalog
     * @return
     */
    UltraCatalog queryByCatalogName(UltraCatalog ultraCatalog);

    /**
     * upsert一个实体
     * @param ultraCatalog
     * @return
     */
    Boolean upsertOneCatalog(UltraCatalog ultraCatalog);

    /**
     * 批量upsert实体
     * @param ultraCatalogList
     * @return
     */
    Boolean upsertBatchCatalogs(List<UltraCatalog> ultraCatalogList);
}
