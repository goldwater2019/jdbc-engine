package com.ane56.xsql.common.model;

import lombok.*;

import java.io.Serializable;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 11:03 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class UltraBaseStatement implements Serializable {

    private static final long serialVersionUID = 7818057974560259240L;

    private String catalogName;
    private String sql;
}
