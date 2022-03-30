package com.ane56.engine.jdbc.utils;


import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: zhangxinsen
 * @Date: 2022/3/30 12:04 AM
 * @Desc:
 * @Version: v1.0
 */

public class NetUtils {

    public static String getInetHostAddress() {
        InetAddress ip4 = null;
        try {
            ip4 = Inet4Address.getLocalHost();
            return ip4.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }
}