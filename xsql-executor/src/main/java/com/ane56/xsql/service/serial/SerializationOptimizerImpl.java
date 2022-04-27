package com.ane56.xsql.service.serial;

import com.ane56.xsql.common.model.UltraCatalog;
import com.ane56.xsql.common.model.UltraResultColumnMetaData;
import com.ane56.xsql.common.model.UltraResultRow;
import com.ane56.xsql.common.model.UltraResultSetMetaData;
import org.apache.dubbo.common.serialize.support.SerializationOptimizer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 10:42 PM
 * @Desc:
 * @Version: v1.0
 */

public class SerializationOptimizerImpl implements SerializationOptimizer {
    @Override
    public Collection<Class<?>> getSerializableClasses() {
        List<Class<?>> classes = new LinkedList<>();
        classes.add(UltraCatalog.class);
        classes.add(UltraResultColumnMetaData.class);
        classes.add(UltraResultRow.class);
        classes.add(UltraResultSetMetaData.class);
        return classes;
    }
}
