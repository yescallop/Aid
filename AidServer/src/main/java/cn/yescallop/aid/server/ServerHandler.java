package cn.yescallop.aid.server;

import cn.yescallop.aid.console.CommandReader;
import cn.yescallop.aid.network.ServerPacketHandler;
import cn.yescallop.aid.network.protocol.DeviceHelloPacket;
import cn.yescallop.aid.network.protocol.Packet;
import cn.yescallop.aid.network.protocol.ServerHelloPacket;
import cn.yescallop.aid.network.protocol.StatusPacket;
import cn.yescallop.aid.server.management.DeviceManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Scallop Ye
 */
@ChannelHandler.Sharable
public class ServerHandler extends ServerPacketHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        CommandReader.info("Connected: " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        DeviceManager.unregisterDevice(ctx.channel());
        CommandReader.info("Disconnected: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void connectionLost(ChannelHandlerContext ctx) {
        CommandReader.info("Connection lost: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void handle(ChannelHandlerContext ctx, Packet packet) {
        CommandReader.info("From " + ctx.channel().remoteAddress() + ": " + packet);
        Channel channel = ctx.channel();
        switch (packet.id()) {
            case Packet.ID_CLIENT_HELLO:
                ctx.writeAndFlush(new ServerHelloPacket());
                break;
            case Packet.ID_DEVICE_HELLO:
                DeviceManager.registerDevice(channel, (DeviceHelloPacket) packet);
                ctx.writeAndFlush(new ServerHelloPacket());
                break;
            case Packet.ID_STATUS:
                System.out.println(((StatusPacket) packet).status);
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        CommandReader.info("Connection reset: " + ctx.channel().remoteAddress());
    }
}
