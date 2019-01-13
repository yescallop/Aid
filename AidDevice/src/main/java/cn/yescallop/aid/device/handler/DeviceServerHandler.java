package cn.yescallop.aid.device.handler;

import cn.yescallop.aid.network.ServerPacketHandler;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Scallop Ye
 * TODO: Finish Device Server part
 */
public class DeviceServerHandler extends ServerPacketHandler {
    @Override
    protected void connectionLost(ChannelHandlerContext ctx) {

    }

    @Override
    protected void handle(ChannelHandlerContext ctx, Packet packet) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

    }
}
