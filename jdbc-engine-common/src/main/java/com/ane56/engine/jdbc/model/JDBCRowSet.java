package com.ane56.engine.jdbc.model;

import com.ane56.engine.jdbc.enumeration.JDBCColumnType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDBCRowSet {
    private List<String> valueList;
}
