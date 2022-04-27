package com.ane56.engine.jdbc.common;

import lombok.*;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 6:00 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ColumnTypeSignature {
    private static final Pattern PATTERN = Pattern.compile(".*[<>,].*");
    private String rawType;
    private List<ColumnTypeSignatureParameter> arguments;
}
