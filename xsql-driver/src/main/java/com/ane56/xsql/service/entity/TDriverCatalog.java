package com.ane56.xsql.service.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * catalog管理表(TDriverCatalog)实体类
 *
 * @author xinsen
 * @since 2022-05-04 10:08:06
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class TDriverCatalog implements Serializable {
    private static final long serialVersionUID = 924410574121079529L;

    private Integer catalogId;
    /**
     * catalog名称
     */
    private String catalogName;
    /**
     * 驱动类名
     */
    private String driverClassName;
    /**
     * 连接串
     */
    private String jdbcUrl;
    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否禁用
     */
    private Integer isForbidden;
    /**
     * 是否可用
     */
    private Integer isAvailable;

    private Date createTime;

    private Date updateTime;

}

