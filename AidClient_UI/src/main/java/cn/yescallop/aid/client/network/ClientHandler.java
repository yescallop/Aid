package cn.yescallop.aid.client.network;

import cn.yescallop.aid.client.api.Factory;
import cn.yescallop.aid.network.ChannelState;
import cn.yescallop.aid.network.ClientPacketHandler;
import cn.yescallop.aid.network.protocol.ClientHelloPacket;
import cn.yescallop.aid.network.protocol.DeviceListPacket;
import cn.yescallop.aid.network.protocol.EventPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandler extends ClientPacketHandler {
    @Override
    protected void connectionEstablished(ChannelHandlerContext ctx) {
        ClientHelloPacket p = new ClientHelloPacket();
        ctx.channel().writeAndFlush(p);
    }

    @Override
    protected void connectionClosed(ChannelHandlerContext ctx, ChannelState lastState, Throwable cause) {
        if (!Factory.Network.isStopping()) {
            if (lastState == ChannelState.ACTIVE) {
                Factory.UI.showDialog("Notice", "Server closed");
            } else {
                Factory.UI.showDialog("Notice", "Server unexpectedly closed the connection");
            }
            Factory.Network.stop();
        }
    }

    @Override
    protected void packetReceived(ChannelHandlerContext ctx, Packet packet) {
        //Factory.UI.println("From " + ctx.channel().remoteAddress() + ": " + packet.getClass().getSimpleName());
        switch (packet.id()) {
            case Packet.ID_DEVICE_LIST:
                DeviceListPacket deviceListPacket = (DeviceListPacket) packet;
                Factory.UI.updateDeviceList(deviceListPacket.type, deviceListPacket.list);
                break;
            case Packet.ID_EVENT:
                EventPacket eventPacket = (EventPacket) packet;
                Factory.UI.println("Event " + eventPacket.event + " from device " + eventPacket.deviceId);
                break;
        }
    }

    @Override
    protected void runtimeExceptionCaught(ChannelHandlerContext ctx, RuntimeException re) {
        Factory.UI.showDialog("RuntimeException", re.getMessage());
    }
}
