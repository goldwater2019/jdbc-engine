package com.ane56.engine.jdbc.common;

import lombok.*;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 5:50 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class QueryError {
    private String message;
    private String sqlState;
    private int errorCode;
    private String errorName;
    private String errorType;
    private boolean retriable;
//    private ErrorLocation errorLocation;
//    private FailureInfo failureInfo;
}
