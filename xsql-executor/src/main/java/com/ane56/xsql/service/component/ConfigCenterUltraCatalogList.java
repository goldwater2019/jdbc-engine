package com.ane56.xsql.service.component;

import com.ane56.xsql.common.model.UltraCatalog;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/27 2:31 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "catalog")
public class ConfigCenterUltraCatalogList {
    private List<UltraCatalog> ultraCatalogList;
}
