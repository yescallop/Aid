package cn.yescallop.aid.server;

import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.ChannelState;
import cn.yescallop.aid.network.ServerPacketHandler;
import cn.yescallop.aid.network.protocol.*;
import cn.yescallop.aid.server.management.ClientManager;
import cn.yescallop.aid.server.management.Device;
import cn.yescallop.aid.server.management.DeviceManager;
import cn.yescallop.aid.server.util.Util;
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
    protected void connectionEstablished(ChannelHandlerContext ctx) {
        Logger.info("Connected: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void packetReceived(ChannelHandlerContext ctx, Packet packet) {
        Logger.info("From " + ctx.channel().remoteAddress() + ": " + packet.getClass().getSimpleName());
        Channel channel = ctx.channel();
        int type = Util.identifyChannel(channel);
        if (type == -1) { //Unregistered
            switch (packet.id()) {
                case Packet.ID_CLIENT_HELLO:
                    ClientManager.registerClient(channel);
                    channel.writeAndFlush(new ServerHelloPacket());
                    channel.writeAndFlush(Util.createFullDeviceListPacket());
                    break;
                case Packet.ID_DEVICE_HELLO:
                    DeviceHelloPacket deviceHelloPacket = (DeviceHelloPacket) packet;
                    Device device = DeviceManager.registerDevice(channel, deviceHelloPacket);
                    if (device != null) {
                        Logger.info(String.format("Device [%s] %s: %s registered", deviceHelloPacket.id, deviceHelloPacket.name, channel.remoteAddress()));
                        channel.writeAndFlush(new ServerHelloPacket());

                        DeviceListPacket deviceListPacket = new DeviceListPacket();
                        deviceListPacket.type = DeviceListPacket.TYPE_ADD;
                        deviceListPacket.list = new DeviceListPacket.DeviceInfo[]{ device.toDeviceInfo() };
                        ClientManager.broadcastPacket(deviceListPacket);
                    } else {
                        Logger.warning(String.format("Device %s's attempt to register with an existing id %d is refused.", deviceHelloPacket.name, deviceHelloPacket.id));
                        StatusPacket idOccupiedStatus = new StatusPacket();
                        idOccupiedStatus.status = StatusPacket.STATUS_ID_OCCUPIED;
                        channel.writeAndFlush(idOccupiedStatus);
                    }
                    break;
                default:
                    Logger.warning("Unexpected packet before hello from " + channel.remoteAddress());
            }
        } else if (type == 0) { //Device
            switch (packet.id()) {
                case Packet.ID_EVENT:
                    EventPacket eventPacket = (EventPacket) packet;
                    eventPacket.deviceId = DeviceManager.idByChannel(channel);
                    ClientManager.broadcastPacket(eventPacket);
                    Logger.info("Event " + eventPacket.event + " from device " + eventPacket.deviceId);
                    break;
            }
        } else if (type == 1) { //Client
            switch (packet.id()) {
                case Packet.ID_REQUEST:
                    RequestPacket requestPacket = (RequestPacket) packet;
                    if (requestPacket.type == RequestPacket.TYPE_DEVICE_LIST) {
                        DeviceListPacket deviceListPacket = Util.createFullDeviceListPacket();
                        channel.writeAndFlush(deviceListPacket);
                    } else {
                        //TODO
                    }
            }
        }
    }

    @Override
    protected void connectionClosed(ChannelHandlerContext ctx, ChannelState lastState, Throwable cause) {
        DeviceManager.unregisterDevice(ctx.channel());
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

    @Override
    protected void runtimeExceptionCaught(ChannelHandlerContext ctx, RuntimeException re) {
        Logger.logException(re);
    }
}
