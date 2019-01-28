package cn.yescallop.aid.client;

import cn.yescallop.aid.client.api.Factory;
import cn.yescallop.aid.network.ClientPacketHandler;
import cn.yescallop.aid.network.protocol.ClientHelloPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Scallop Ye
 */
public class ClientHandler extends ClientPacketHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        for (int i = 0; i < 5; i++) {
            ClientHelloPacket p = new ClientHelloPacket();
            ctx.channel().writeAndFlush(p);
        }
    }

    @Override
    protected void connectionLost(ChannelHandlerContext ctx) {
        Factory.println("Connection lost: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void handle(ChannelHandlerContext ctx, Packet packet) {
        Factory.println("From " + ctx.channel().remoteAddress() + ": ");
        Factory.println(packet.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Factory.println("Connection reset: " + ctx.channel().remoteAddress());
    }
}
