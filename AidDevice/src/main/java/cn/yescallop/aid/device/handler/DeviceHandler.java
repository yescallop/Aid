package cn.yescallop.aid.device.handler;

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
        p.addresses = DeviceMain.addresses();
        ctx.channel().writeAndFlush(p);
    }

    @Override
    protected void connectionLost(ChannelHandlerContext ctx) {
        System.out.println("Connection lost: " + ctx.channel().remoteAddress());
    }

    @Override
    protected void handle(ChannelHandlerContext ctx, Packet packet) {
        System.out.print("From " + ctx.channel().remoteAddress() + ": ");
        System.out.println(packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Connection reset: " + ctx.channel().remoteAddress());
    }
}
