package cn.yescallop.aid.client;

import cn.yescallop.aid.network.PacketHandler;
import cn.yescallop.aid.network.protocol.ClientHelloPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandler extends PacketHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        for (int i = 0; i < 5; i++) {
            ClientHelloPacket p = new ClientHelloPacket();
            ctx.channel().writeAndFlush(p);
        }
    }

    @Override
    protected void handle(ChannelHandlerContext ctx, Packet packet) {
        System.out.print("From " + ctx.channel().remoteAddress() + ": ");
        System.out.println(packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
