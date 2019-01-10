package cn.yescallop.aid.device;

import cn.yescallop.aid.device.util.Util;
import cn.yescallop.aid.network.protocol.ClientHelloPacket;
import cn.yescallop.aid.network.protocol.DeviceHelloPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DeviceHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        DeviceHelloPacket p = new DeviceHelloPacket();
        p.name = "测试设备";
        p.mac = Util.getMacAddress();
        ctx.channel().writeAndFlush(p);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.print("From " + ctx.channel().remoteAddress() + ": ");
        if (msg instanceof Packet) {
            Packet packet = (Packet) msg;
            System.out.println(packet);
        } else if (msg instanceof ByteBuf) {
            System.out.println("unidentified buffer");
            System.out.println(ByteBufUtil.prettyHexDump((ByteBuf) msg));
        }
    }
}
