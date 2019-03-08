package cn.yescallop.aid.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetAddress;

/**
 * @author Scallop Ye
 */
public class Network {

    public static final byte[] MAGIC = new byte[]{(byte) 0xA1, (byte) 0xD0, 0x03, (byte) 0xAA};
    public static final int IDLE_TIMEOUT = 5; //TODO: Configuration files
    public static final int MAXIMUM_TIMEOUT_COUNT = 2;

    private Network() {
        //no instance
    }

    /**
     * Runs a Netty server on host:port with specific handlers.
     */
    public static Channel startServer(String host, int port, ChannelHandler... handlers) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast("IdleStateHandler", new IdleStateHandler(IDLE_TIMEOUT, 0, 0))
                                .addLast("Decoder", new PacketDecoder())
                                .addLast("Encoder", new PacketEncoder())
                                .addLast(handlers);
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true);
        Channel channel = bootstrap.bind(host, port).sync().channel();
        channel.closeFuture().addListener(future -> {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        });
        return channel;
    }

    /**
     * Runs a Netty client on host:port with specific handlers.
     */
    public static Channel startClient(String host, int port, ChannelHandler... handlers) throws Exception {
        return startClient(InetAddress.getByName(host), port, handlers);
    }

    public static Channel startClient(InetAddress host, int port, ChannelHandler... handlers) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast("IdleStateHandler", new IdleStateHandler(IDLE_TIMEOUT, 0, 0))
                                .addLast("Decoder", new PacketDecoder())
                                .addLast("Encoder", new PacketEncoder())
                                .addLast(handlers);
                    }
                })
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);
        Channel channel = bootstrap.connect(host, port).sync().channel();
        channel.closeFuture().addListener(future -> workerGroup.shutdownGracefully());
        return channel;
    }
}
