import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class NettyClientSync {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        NioSocketChannel client = new NioSocketChannel();
        eventExecutors.register(client);
        ChannelPipeline pipeline = client.pipeline();
        pipeline.addLast(new MyInHandler());
        ChannelFuture connect = client.connect(new InetSocketAddress("127.0.0.1", 9090));
        ChannelFuture sync = connect.sync();
        ByteBuf buf = Unpooled.copiedBuffer("hello server".getBytes());
        ChannelFuture send = client.writeAndFlush(buf);
        send.sync();
        sync.channel().closeFuture().sync();
        System.out.println("client over");
    }

    static class MyInHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("client registered ...");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("client activated ...");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;
            CharSequence str = buf.getCharSequence(0, buf.readableBytes(), CharsetUtil.UTF_8);
//            System.out.println(str);
            ctx.writeAndFlush(buf);
        }
    }
}
