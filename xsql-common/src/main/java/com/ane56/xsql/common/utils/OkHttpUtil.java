package com.ane56.xsql.common.utils;

import com.google.common.net.HostAndPort;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/27 9:25 PM
 * @Desc: OkHttpUtil工具类
 * @Version: v1.0
 */

public final class OkHttpUtil {
    private OkHttpUtil() {
    }

    public static class NullCallback
            implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
        }

        @Override
        public void onResponse(Call call, Response response) {
        }
    }

    /**
     * 设置相应的超时时间
     *
     * @param clientBuilder
     * @param timeout
     * @param unit
     */
    public static void setupTimeouts(OkHttpClient.Builder clientBuilder, int timeout, TimeUnit unit) {
        clientBuilder
                .connectTimeout(timeout, unit)
                .readTimeout(timeout, unit)
                .writeTimeout(timeout, unit);
    }

    private static InetSocketAddress toUnresolvedAddress(HostAndPort address) {
        return InetSocketAddress.createUnresolved(address.getHost(), address.getPort());
    }

}