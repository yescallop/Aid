package cn.yescallop.aid.server;

import cn.yescallop.aid.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Scallop Ye
 */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Connected: " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Disconnected: " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.print("From " + ctx.channel().remoteAddress() + ": ");
        if (msg instanceof Packet) {
            Packet packet = (Packet) msg;
            System.out.println(packet);
        } else if (msg instanceof ByteBuf) {
            System.out.println("unidentified buffer '" + ByteBufUtil.hexDump((ByteBuf) msg) + '\'');
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
