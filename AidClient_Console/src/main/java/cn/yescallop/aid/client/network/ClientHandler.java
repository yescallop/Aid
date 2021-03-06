package cn.yescallop.aid.client.network;

import cn.yescallop.aid.client.ClientConsoleMain;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.ChannelState;
import cn.yescallop.aid.network.ClientPacketHandler;
import cn.yescallop.aid.network.protocol.ClientHelloPacket;
import cn.yescallop.aid.network.protocol.DeviceListPacket;
import cn.yescallop.aid.network.protocol.EventPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Scallop Ye
 */
public class ClientHandler extends ClientPacketHandler {

    @Override
    protected void connectionEstablished(ChannelHandlerContext ctx) {
        ClientHelloPacket p = new ClientHelloPacket();
        ctx.channel().writeAndFlush(p);
    }

    @Override
    protected void packetReceived(ChannelHandlerContext ctx, Packet packet) {
        Logger.info("From " + ctx.channel().remoteAddress() + ": " + packet.getClass().getSimpleName());
        switch (packet.id()) {
            case Packet.ID_DEVICE_LIST:
                DeviceListPacket deviceListPacket = (DeviceListPacket) packet;
                ClientConsoleMain.updateDeviceList(deviceListPacket.type, deviceListPacket.list);
                break;
            case Packet.ID_EVENT:
                EventPacket eventPacket = (EventPacket) packet;
                Logger.info("Event " + eventPacket.event + " from device " + eventPacket.deviceId);
                break;
        }
    }

    @Override
    protected void connectionClosed(ChannelHandlerContext ctx, ChannelState lastState, Throwable cause) {
        if (!ClientConsoleMain.isStopping()) {
            if (lastState == ChannelState.ACTIVE) {
                Logger.info("Server closed");
            } else {
                Logger.warning("Server unexpectedly closed the connection");
            }
            ClientConsoleMain.stop();
        }
    }

    @Override
    protected void runtimeExceptionCaught(ChannelHandlerContext ctx, RuntimeException re) {
        Logger.logException(re);
    }
}
