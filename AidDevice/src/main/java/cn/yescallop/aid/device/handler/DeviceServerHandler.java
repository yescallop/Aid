package cn.yescallop.aid.device.handler;

import cn.yescallop.aid.network.ChannelState;
import cn.yescallop.aid.network.ServerPacketHandler;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Scallop Ye
 * TODO: Finish Device Server part
 */
public class DeviceServerHandler extends ServerPacketHandler {

    @Override
    protected void connectionEstablished(ChannelHandlerContext ctx) {

    }

    @Override
    protected void packetReceived(ChannelHandlerContext ctx, Packet packet) {

    }

    @Override
    protected void runtimeExceptionCaught(ChannelHandlerContext ctx, RuntimeException re) {

    }

    @Override
    protected void connectionClosed(ChannelHandlerContext ctx, ChannelState lastState, Throwable cause) {

    }
}
