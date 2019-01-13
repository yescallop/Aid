package cn.yescallop.aid.network;

import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Scallop Ye
 * Base abstract class of ChannelHandler implementation
 * for Packet handling with idle management.
 */
public abstract class PacketHandler extends ChannelInboundHandlerAdapter {

    @Override
    public abstract void exceptionCaught(ChannelHandlerContext ctx, Throwable cause);

    protected abstract void connectionLost(ChannelHandlerContext ctx);

    protected abstract void handle(ChannelHandlerContext ctx, Packet packet);
}
