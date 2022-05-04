package com.ane56.xsql.service.provider;

import com.ane56.xsql.common.api.XSqlDriverService;
import com.ane56.xsql.common.model.UltraCatalog;
import com.ane56.xsql.service.dao.TDriverCatalogDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

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

    @Autowired
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

    /**
     * 获得所有的catalog对象
     *
     * @return
     */
    @Override
    public List<UltraCatalog> getAllCatalogs() {
        return this.tDriverCatalogDao.getAllCatalogs();
    }

    /**
     * 根据条件筛选相应的catalog对象
     *
     * @param isForbidden
     * @param isAvailable
     * @return
     */
    @Override
    public List<UltraCatalog> getAllCatalogsWithFilter(Boolean isForbidden, Boolean isAvailable) {
        // TODO 根据条件筛选数据
        return this.tDriverCatalogDao.getAllCatalogsWithFilter(isForbidden, isAvailable);
    }

    /**
     * @param ultraCatalog
     * @return
     */
    @Override
    public UltraCatalog queryByCatalogName(UltraCatalog ultraCatalog) {
        return this.tDriverCatalogDao.queryByCatalogName(ultraCatalog.getCatalogName());
    }

    /**
     * upsert一个实体
     *
     * @param ultraCatalog
     * @return
     */
    @Override
    public Boolean upsertOneCatalog(UltraCatalog ultraCatalog) {
        Assert.isTrue(StringUtils.hasLength(ultraCatalog.getCatalogName()), "catalogName is missing");
        UltraCatalog catalog = this.tDriverCatalogDao.queryByCatalogName(ultraCatalog.getCatalogName());
        if (catalog == null) {
            Assert.isTrue(StringUtils.hasLength(ultraCatalog.getJdbcUrl()), "jdbc url is missing, error");
            Assert.isTrue(StringUtils.hasLength(ultraCatalog.getUsername()), "username is missing, error");
            // Assert.isTrue(StringUtils.hasLength(ultraCatalog.getPassword()), "password is missing, error");
            Assert.isTrue(StringUtils.hasLength(ultraCatalog.getDriverClassName()), "driver class name is " +
                    "missing, error");
            Boolean isForbidden = ultraCatalog.getIsForbidden();
            if (isForbidden == null) {
                ultraCatalog.setIsForbidden(false);
            }
            Boolean isAvailable = ultraCatalog.getIsAvailable();
            if (isAvailable == null) {
                ultraCatalog.setIsAvailable(true);
            }
            this.tDriverCatalogDao.insert(ultraCatalog);
        } else {
            // update
            ultraCatalog.setCatalogId(catalog.getCatalogId());
            this.tDriverCatalogDao.update(ultraCatalog);
        }
        return true;
    }

    /**
     * 批量upsert实体
     *
     * @param ultraCatalogList
     * @return
     */
    @Override
    public Boolean upsertBatchCatalogs(List<UltraCatalog> ultraCatalogList) {
        for (UltraCatalog ultraCatalog : ultraCatalogList) {
            upsertOneCatalog(ultraCatalog);
        }
        return true;
    }
}
