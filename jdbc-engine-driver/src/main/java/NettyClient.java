import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        for (int i = 0; i < 1000; i++) {
            NIOClientRunnable nioClientRunnable = new NIOClientRunnable();
            Future<?> submit = executorService.submit(nioClientRunnable);
            submit.get();
        }

        System.out.println(System.currentTimeMillis() - startTime);
    }

    public static class NIOClientRunnable implements Runnable {

        @Override
        public void run() {
            NioEventLoopGroup group = new NioEventLoopGroup(1);
            Bootstrap bootstrap = new Bootstrap();
            ChannelFuture connect = bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            ChannelPipeline pipeline = nioSocketChannel.pipeline();
                            pipeline.addLast(new NettyClientSync.MyInHandler());
                        }
                    }).connect(new InetSocketAddress("127.0.0.1", 9090));
            Channel client = connect.channel();
            ByteBuf buf = Unpooled.copiedBuffer("Hello Server".getBytes());
            ChannelFuture channelFuture = client.writeAndFlush(buf);
            try {
                channelFuture.sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
