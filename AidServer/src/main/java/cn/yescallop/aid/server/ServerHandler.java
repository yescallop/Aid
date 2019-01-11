package cn.yescallop.aid.server;

import cn.yescallop.aid.network.AbstractHandler;
import cn.yescallop.aid.network.protocol.Packet;
import cn.yescallop.aid.network.protocol.ServerHelloPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Scallop Ye
 */
@ChannelHandler.Sharable
public class ServerHandler extends AbstractHandler {

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
        super.channelRead(ctx, msg);
    }

    @Override
    protected void handlePacket(ChannelHandlerContext ctx, Packet packet) {
        switch (packet.id()) {
            case Packet.ID_CLIENT_HELLO:
                ctx.writeAndFlush(new ServerHelloPacket());
                break;
            case Packet.ID_DEVICE_HELLO:
                ctx.writeAndFlush(new ServerHelloPacket());
                break;



        }
        System.out.println(packet);
    }

    @Override
    protected void handleUnidentified(ChannelHandlerContext ctx, ByteBuf buf) {
        System.out.println("unidentified buffer");
        System.out.println(ByteBufUtil.prettyHexDump(buf));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
