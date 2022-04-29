package com.ane56.xsql.common.utils;

import java.util.Collection;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/29 11:08 AM
 * @Desc:
 * @Version: v1.0
 */

public final class ArrayUtil<T> {


    public boolean isArrayContains(Collection<T> array, T source, boolean isIgnoreNull) {
        if (isIgnoreNull) {
            return false;
        }
        boolean isExists = false;
        for (T t : array) {
            if (t.equals(source)) {
                return true;
            }
        }
        return false;
    }
}
