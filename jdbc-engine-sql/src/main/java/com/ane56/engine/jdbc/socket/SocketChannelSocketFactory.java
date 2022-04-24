package com.ane56.engine.jdbc.socket;

import javax.net.SocketFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 12:36 AM
 * @Desc:
 * @Version: v1.0
 */

public class SocketChannelSocketFactory
        extends SocketFactory
{
    @Override
    public Socket createSocket()
            throws IOException
    {
        return SocketChannel.open().socket();
    }

    @Override
    public Socket createSocket(String host, int port)
            throws IOException
    {
        return SocketChannel.open(new InetSocketAddress(host, port)).socket();
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort)
            throws IOException
    {
        throw new SocketException("not supported");
    }

    @Override
    public Socket createSocket(InetAddress address, int port)
            throws IOException
    {
        return SocketChannel.open(new InetSocketAddress(address, port)).socket();
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
            throws IOException
    {
        throw new SocketException("not supported");
    }
}