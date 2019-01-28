package cn.yescallop.aid.client;

import cn.yescallop.aid.client.util.Util;
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
        Logger.info("From " + ctx.channel().remoteAddress() + ": " + packet);
        switch (packet.id()) {
            case Packet.ID_DEVICE_LIST:
                DeviceListPacket deviceListPacket = (DeviceListPacket) packet;
                if (deviceListPacket.deviceInfos.length == 0) {
                    Logger.info("No device is connected at present.");
                } else {
                    Util.logDeviceList(deviceListPacket.deviceInfos);
                }
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
            if (lastState == ChannelState.FINE) {
                Logger.info("Server closed");
            } else {
                Logger.warning("Server unexpectedly closed the connection");
            }
            ClientConsoleMain.stop();
        }
    }
}
