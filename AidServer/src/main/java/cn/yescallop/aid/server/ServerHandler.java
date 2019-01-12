package cn.yescallop.aid.server;

import cn.yescallop.aid.network.PacketHandler;
import cn.yescallop.aid.network.protocol.Packet;
import cn.yescallop.aid.network.protocol.ServerHelloPacket;
import cn.yescallop.aid.network.protocol.StatusPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Scallop Ye
 */
@ChannelHandler.Sharable
public class ServerHandler extends PacketHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Connected: " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Disconnected: " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.print("From " + ctx.channel().remoteAddress() + ": ");
        super.channelRead(ctx, msg);
    }

    @Override
    protected void handle(ChannelHandlerContext ctx, Packet packet) {
        switch (packet.id()) {
            case Packet.ID_CLIENT_HELLO:
                ctx.writeAndFlush(new ServerHelloPacket());
                break;
            case Packet.ID_DEVICE_HELLO:
                ctx.writeAndFlush(new ServerHelloPacket());
                break;
            case Packet.ID_STATUS:
                System.out.println(((StatusPacket) packet).status);
                break;
        }
        System.out.println(packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
