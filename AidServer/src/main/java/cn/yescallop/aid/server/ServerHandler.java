package cn.yescallop.aid.server;

import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.ChannelState;
import cn.yescallop.aid.network.ServerPacketHandler;
import cn.yescallop.aid.network.protocol.DeviceHelloPacket;
import cn.yescallop.aid.network.protocol.Packet;
import cn.yescallop.aid.network.protocol.ServerHelloPacket;
import cn.yescallop.aid.network.protocol.StatusPacket;
import cn.yescallop.aid.server.management.DeviceManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;

/**
 * @author Scallop Ye
 */
@ChannelHandler.Sharable
public class ServerHandler extends ServerPacketHandler {

    @Override
    public void connectionEstablished(ChannelHandlerContext ctx) {
        Logger.info("Connected: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void packetReceived(ChannelHandlerContext ctx, Packet packet) {
        Logger.info("From " + ctx.channel().remoteAddress() + ": " + packet);
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
    protected void connectionClosed(ChannelHandlerContext ctx, ChannelState lastState, Throwable cause) {
        SocketAddress addr = ctx.channel().remoteAddress();
        switch (state) {
            case FINE:
                Logger.info("Disconnected: " + addr);
                break;
            case EXCEPTION_CAUGHT:
                Logger.warning("Connection reset: " + addr);
                break;
            case CONNECTION_LOST:
                Logger.warning("Connection lost:" + addr);
                break;
        }
    }
}
