package cn.yescallop.aid.client;

import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.ChannelState;
import cn.yescallop.aid.network.ClientPacketHandler;
import cn.yescallop.aid.network.protocol.ClientHelloPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Scallop Ye
 */
public class ClientHandler extends ClientPacketHandler {

    @Override
    protected void connectionEstablished(ChannelHandlerContext ctx) {
        for (int i = 0; i < 5; i++) {
            ClientHelloPacket p = new ClientHelloPacket();
            ctx.channel().writeAndFlush(p);
        }
    }

    @Override
    protected void packetReceived(ChannelHandlerContext ctx, Packet packet) {
        Logger.info("From " + ctx.channel().remoteAddress() + ": " + packet);
    }

    @Override
    protected void connectionClosed(ChannelHandlerContext ctx, ChannelState lastState, Throwable cause) {
        if (!ClientConsoleMain.isStopping()) {
            if (lastState == ChannelState.FINE) {
                Logger.info("Server closed");
            } else {
                Logger.warning("Server unexpectedly closed the connection");
            }
            ClientConsoleMain.stop();
        }
    }
}
