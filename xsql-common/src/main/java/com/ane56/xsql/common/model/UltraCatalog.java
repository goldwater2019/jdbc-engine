package com.ane56.xsql.common.model;

import lombok.*;

import java.io.Serializable;

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
    private String name;
    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClassName;
    private Boolean isAvailable;  // 是否可用
    private Boolean isForbidden;  // 是否禁用

}
