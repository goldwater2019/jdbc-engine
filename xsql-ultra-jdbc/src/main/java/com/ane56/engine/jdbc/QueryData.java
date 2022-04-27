package com.ane56.engine.jdbc;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class QueryData {
    private List<List<Object>> data;

    public Iterable<List<Object>> getData() {
        return data;
    }
}
