package cn.yescallop.aid.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Scallop Ye
 */
public class Network {

    public static final byte[] MAGIC = new byte[]{(byte) 0xA1, (byte) 0xD0, 0x03, (byte) 0xAA};

    private Network() {
        //no instance
    }

    public static void startServer(int port, ChannelInboundHandlerAdapter handler) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast("Decoder", new PacketDecoder())
                                .addLast("Encoder", new PacketEncoder())
                                .addLast(handler);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true);
        try {
            bootstrap.bind(port).sync()
                    .channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void startClient(String host, int port, ChannelInboundHandlerAdapter handler) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast("Decoder", new PacketDecoder())
                                .addLast("Encoder", new PacketEncoder())
                                .addLast(handler);
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);
        try {
            bootstrap.connect(host, port).sync()
                    .channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
