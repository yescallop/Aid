package cn.yescallop.aid.network;

import cn.yescallop.aid.network.protocol.EchoPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author Scallop Ye
 * A client-side abstract ChannelHandler implementation
 * for Packet handling with idle management.
 */
public abstract class ClientPacketHandler extends PacketHandler {

    private int idleCount = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        idleCount = 0;
        if (msg instanceof Packet) {
            if (msg instanceof EchoPacket) {
                if (msg == EchoPacket.PING) {
                    ctx.writeAndFlush(EchoPacket.PONG);
                    return;
                }
            }
            this.handle(ctx, (Packet) msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
            if (idleCount >= Network.MAXIMUM_TIMEOUT_COUNT) {
                ctx.close();
                connectionLost(ctx);
            }
            idleCount++;
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public abstract void exceptionCaught(ChannelHandlerContext ctx, Throwable cause);

    protected abstract void connectionLost(ChannelHandlerContext ctx);

    protected abstract void handle(ChannelHandlerContext ctx, Packet packet);
}
