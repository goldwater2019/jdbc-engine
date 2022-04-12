package com.ane56.engine.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class JDBCQueryReq {
    private String querySql;
}
