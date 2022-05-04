package com.ane56.xsql.service.provider;

import com.ane56.xsql.common.api.XSqlDriverService;
import com.ane56.xsql.common.model.UltraCatalog;
import com.ane56.xsql.service.dao.TDriverCatalogDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/25 1:48 PM
 * @Desc:
 * @Version: v1.0
 */

@Slf4j
@DubboService
public class XSqlDriverServiceImpl implements XSqlDriverService {
    @Override
    public String sayHello(String name) {
        log.info("hello world");
        return "hello " + name;
    }

    @Resource
    private TDriverCatalogDao tDriverCatalogDao;

    /**
     * 通过ID查询单条数据
     *
     * @param catalogId 主键
     * @return 实例对象
     */
    @Override
    public UltraCatalog queryById(Integer catalogId) {
        return this.tDriverCatalogDao.queryById(catalogId);
    }

    /**
     * 新增数据
     *
     * @param ultraCatalog 实例对象
     * @return 实例对象
     */
    @Override
    public UltraCatalog insert(UltraCatalog ultraCatalog) {
        this.tDriverCatalogDao.insert(ultraCatalog);
        return ultraCatalog;
    }

    /**
     * 修改数据
     *
     * @param ultraCatalog 实例对象
     * @return 实例对象
     */
    @Override
    public UltraCatalog update(UltraCatalog ultraCatalog) {
        this.tDriverCatalogDao.update(ultraCatalog);
        return this.queryById(ultraCatalog.getCatalogId());
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
