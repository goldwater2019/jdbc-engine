package com.ane56.xsql.common.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 4:33 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class UltraCatalog implements Serializable {
    private static final long serialVersionUID = -8398701659259091582L;
    private Integer catalogId;

    /**
     * catalog名称
     */
    private String catalogName;

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
     * 驱动类名
     */
    private String driverClassName;

    /**
     * 是否可用
     */
    private Boolean isAvailable;

    /**
     * 是否禁用
     */
    private Boolean isForbidden;

    private Date createTime;

    private Date updateTime;

}
