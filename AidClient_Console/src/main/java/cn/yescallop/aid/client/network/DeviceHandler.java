package cn.yescallop.aid.client.network;

import cn.yescallop.aid.client.ClientConsoleMain;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.ChannelState;
import cn.yescallop.aid.network.ClientPacketHandler;
import cn.yescallop.aid.network.protocol.ClientHelloPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;

public class DeviceHandler extends ClientPacketHandler {

    private int frameCount = 0;
    private long lastTime = -1;

    @Override
    protected void connectionEstablished(ChannelHandlerContext ctx) {
        ClientHelloPacket p = new ClientHelloPacket();
        ctx.channel().writeAndFlush(p);
    }

    @Override
    protected void connectionClosed(ChannelHandlerContext ctx, ChannelState lastState, Throwable cause) {
        if (!ClientConsoleMain.isStopping()) {
            if (lastState == ChannelState.ACTIVE) {
                Logger.info("Connection to device closed");
            } else {
                Logger.warning("Device unexpectedly closed the connection");
            }
        }
    }

    @Override
    protected void packetReceived(ChannelHandlerContext ctx, Packet packet) {
        switch (packet.id()) {
            case Packet.ID_VIDEO:
                frameCount++;
                long curTime = System.currentTimeMillis();
                if (lastTime != -1) {
                    long dur = curTime - lastTime;
                    if (dur >= 1000) {
                        float fps = frameCount / (dur / 1000f);
                        Logger.info("FPS: " + String.format("%.1f", fps));
                        frameCount = 0;
                        lastTime = curTime;
                    }
                } else lastTime = curTime;


                break;
            default:
                Logger.info("From " + ctx.channel().remoteAddress() + ": " + packet);
        }
    }

    @Override
    protected void runtimeExceptionCaught(ChannelHandlerContext ctx, RuntimeException re) {
        Logger.logException(re);
    }
}
