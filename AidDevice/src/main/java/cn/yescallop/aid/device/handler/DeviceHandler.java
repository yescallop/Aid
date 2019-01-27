package cn.yescallop.aid.device.handler;

import cn.yescallop.aid.console.CommandReader;
import cn.yescallop.aid.device.DeviceMain;
import cn.yescallop.aid.network.ClientPacketHandler;
import cn.yescallop.aid.network.protocol.DeviceHelloPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Scallop Ye
 */
public class DeviceHandler extends ClientPacketHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        DeviceHelloPacket p = new DeviceHelloPacket();
        p.name = "测试设备";
        p.localAddresses = DeviceMain.localAddresses();
        ctx.channel().writeAndFlush(p);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (!DeviceMain.isStopping()) {
            CommandReader.warning("Server unexpectedly closed the connection");
            DeviceMain.attemptReconnecting();
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
