package cn.yescallop.aid.client;

import cn.yescallop.aid.network.AbstractHandler;
import cn.yescallop.aid.network.protocol.ClientHelloPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandler extends AbstractHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ClientHelloPacket p = new ClientHelloPacket();
        ctx.channel().writeAndFlush(p);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.print("From " + ctx.channel().remoteAddress() + ": ");
        super.channelRead(ctx, msg);
    }

    @Override
    protected void handlePacket(ChannelHandlerContext ctx, Packet packet) {
        System.out.println(packet);
    }

    @Override
    protected void handleUnidentified(ChannelHandlerContext ctx, ByteBuf buf) {
        System.out.println("unidentified buffer");
        System.out.println(ByteBufUtil.prettyHexDump(buf));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
