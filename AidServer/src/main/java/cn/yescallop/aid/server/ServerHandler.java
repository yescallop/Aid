package cn.yescallop.aid.server;

import cn.yescallop.aid.network.ServerPacketHandler;
import cn.yescallop.aid.network.protocol.Packet;
import cn.yescallop.aid.network.protocol.ServerHelloPacket;
import cn.yescallop.aid.network.protocol.StatusPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Scallop Ye
 */
@ChannelHandler.Sharable
public class ServerHandler extends ServerPacketHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Connected: " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Disconnected: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void connectionLost(ChannelHandlerContext ctx) {
        System.out.println("Connection lost: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void handle(ChannelHandlerContext ctx, Packet packet) {
        System.out.print("From " + ctx.channel().remoteAddress() + ": ");
        System.out.println(packet);
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
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Connection reset: " + ctx.channel().remoteAddress());
    }
}
