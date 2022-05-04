package com.ane56.xsql.service.service.impl;

import com.ane56.xsql.service.entity.TDriverCatalog;
import com.ane56.xsql.service.dao.TDriverCatalogDao;
import com.ane56.xsql.service.service.TDriverCatalogService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * catalog管理表(TDriverCatalog)表服务实现类
 *
 * @author xinsen
 * @since 2022-05-04 10:08:06
 */
@Service("tDriverCatalogService")
public class TDriverCatalogServiceImpl implements TDriverCatalogService {
    @Resource
    private TDriverCatalogDao tDriverCatalogDao;

    /**
     * 通过ID查询单条数据
     *
     * @param catalogId 主键
     * @return 实例对象
     */
    @Override
    public TDriverCatalog queryById(Integer catalogId) {
        return this.tDriverCatalogDao.queryById(catalogId);
    }

    /**
     * 分页查询
     *
     * @param tDriverCatalog 筛选条件
     * @param pageRequest    分页对象
     * @return 查询结果
     */
    @Override
    public Page<TDriverCatalog> queryByPage(TDriverCatalog tDriverCatalog, PageRequest pageRequest) {
        long total = this.tDriverCatalogDao.count(tDriverCatalog);
        return new PageImpl<>(this.tDriverCatalogDao.queryAllByLimit(tDriverCatalog, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tDriverCatalog 实例对象
     * @return 实例对象
     */
    @Override
    public TDriverCatalog insert(TDriverCatalog tDriverCatalog) {
        this.tDriverCatalogDao.insert(tDriverCatalog);
        return tDriverCatalog;
    }

    /**
     * 修改数据
     *
     * @param tDriverCatalog 实例对象
     * @return 实例对象
     */
    @Override
    public TDriverCatalog update(TDriverCatalog tDriverCatalog) {
        this.tDriverCatalogDao.update(tDriverCatalog);
        return this.queryById(tDriverCatalog.getCatalogId());
    }

    /**
     * 通过主键删除数据
     *
     * @param catalogId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer catalogId) {
        return this.tDriverCatalogDao.deleteById(catalogId) > 0;
    }
}
