package cn.yescallop.aid.device.handler;

import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.device.DeviceMain;
import cn.yescallop.aid.network.ChannelState;
import cn.yescallop.aid.network.ClientPacketHandler;
import cn.yescallop.aid.network.protocol.DeviceHelloPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Scallop Ye
 */
public class DeviceHandler extends ClientPacketHandler {

    @Override
    protected void connectionEstablished(ChannelHandlerContext ctx) {
        DeviceHelloPacket p = new DeviceHelloPacket();
        p.name = "测试设备";
        p.localAddresses = DeviceMain.localAddresses();
        ctx.channel().writeAndFlush(p);
    }

    @Override
    protected void connectionClosed(ChannelHandlerContext ctx, ChannelState lastState, Throwable cause) {
        if (!DeviceMain.isStopping()) {
            if (lastState == ChannelState.FINE) {
                Logger.info("Server closed");
            } else {
                Logger.warning("Server unexpectedly closed the connection");
            }
            DeviceMain.attemptReconnecting();
        }
    }

    @Override
    protected void packetReceived(ChannelHandlerContext ctx, Packet packet) {
        Logger.info("From " + ctx.channel().remoteAddress() + ": " + packet);
    }
}
