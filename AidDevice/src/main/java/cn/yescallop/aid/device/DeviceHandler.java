package cn.yescallop.aid.device;

import cn.yescallop.aid.network.PacketHandler;
import cn.yescallop.aid.network.protocol.DeviceHelloPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Scallop Ye
 */
public class DeviceHandler extends PacketHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        DeviceHelloPacket p = new DeviceHelloPacket();
        p.name = "测试设备";
        p.addresses = Device.addresses;
        ctx.channel().writeAndFlush(p);
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
