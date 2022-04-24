package com.ane56.engine.jdbc.common;

import lombok.*;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 4:06 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class StatementStats {
    private String state;
    private boolean waitingForPrerequisites;
    private boolean queued;
    private boolean scheduled;
    private int nodes;
}
