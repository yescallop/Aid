package cn.yescallop.aid.client;

import cn.yescallop.aid.console.CommandReader;
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
    public void channelInactive(ChannelHandlerContext ctx) {
        if (!ClientConsoleMain.isStopping()) {
            CommandReader.info("Server unexpected closed the connection");
            ClientConsoleMain.stop();
        }
    }

    @Override
    protected void connectionLost(ChannelHandlerContext ctx) {
        CommandReader.info("Connection lost: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void handle(ChannelHandlerContext ctx, Packet packet) {
        CommandReader.info("From " + ctx.channel().remoteAddress() + ": " + packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        CommandReader.info("Connection reset: " + ctx.channel().remoteAddress());
    }
}
