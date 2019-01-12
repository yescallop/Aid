package cn.yescallop.aid.network;

import cn.yescallop.aid.network.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class AbstractHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Packet) {
            this.handlePacket(ctx, (Packet) msg);
        } else if (msg instanceof ByteBuf) {
            this.handleUnidentified(ctx, (ByteBuf) msg);
        }
    }

    protected abstract void handlePacket(ChannelHandlerContext ctx, Packet packet);

    protected void handleUnidentified(ChannelHandlerContext ctx, ByteBuf buf) {
        //默认忽略
    }
}
