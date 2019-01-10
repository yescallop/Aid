package cn.yescallop.aid.client;

import cn.yescallop.aid.network.protocol.LoginPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LoginPacket p = new LoginPacket();
        p.username = p.password = "测试UTF-8";
        ctx.channel().writeAndFlush(p);
        ctx.close();
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
