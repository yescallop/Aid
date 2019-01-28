package cn.yescallop.aid.client;

import cn.yescallop.aid.client.api.Factory;
import cn.yescallop.aid.network.ChannelState;
import cn.yescallop.aid.network.ClientPacketHandler;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandler extends ClientPacketHandler {
    @Override
    protected void connectionEstablished(ChannelHandlerContext ctx) {

    }

    @Override
    protected void connectionClosed(ChannelHandlerContext ctx, ChannelState lastState, Throwable cause) {

    }

    @Override
    protected void packetReceived(ChannelHandlerContext ctx, Packet packet) {
        Factory.println("From " + ctx.channel().remoteAddress() + ": " + packet);
    }
}
