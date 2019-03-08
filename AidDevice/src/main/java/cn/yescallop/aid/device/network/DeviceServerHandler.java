package cn.yescallop.aid.device.network;

import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.device.DeviceMain;
import cn.yescallop.aid.network.ChannelState;
import cn.yescallop.aid.network.ServerPacketHandler;
import cn.yescallop.aid.network.protocol.DeviceHelloPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;

/**
 * @author Scallop Ye
 * TODO: Finish Device Server part
 */
@ChannelHandler.Sharable
public class DeviceServerHandler extends ServerPacketHandler {

    @Override
    protected void connectionEstablished(ChannelHandlerContext ctx) {
        Logger.info("Connected: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void packetReceived(ChannelHandlerContext ctx, Packet packet) {
        Logger.info("From " + ctx.channel().remoteAddress() + ": " + packet);
        Channel channel = ctx.channel();
        if (!ClientManager.isRegistered(channel)) {
            switch (packet.id()) {
                case Packet.ID_CLIENT_HELLO:
                    ClientManager.registerClient(channel);
                    DeviceHelloPacket p = new DeviceHelloPacket();
                    p.id = DeviceMain.ID;
                    p.name = DeviceMain.NAME;
                    p.localAddresses = DeviceMain.localAddresses();
                    p.port = DeviceMain.PORT;
                    channel.writeAndFlush(p);
                    break;
                default:
                    Logger.warning("Unexpected packet before hello from " + channel.remoteAddress());
            }
        }
    }

    @Override
    protected void runtimeExceptionCaught(ChannelHandlerContext ctx, RuntimeException re) {
        Logger.logException(re);
    }

    @Override
    protected void connectionClosed(ChannelHandlerContext ctx, ChannelState lastState, Throwable cause) {
        ClientManager.unregisterClient(ctx.channel());

        SocketAddress addr = ctx.channel().remoteAddress();
        switch (lastState) {
            case ACTIVE:
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
