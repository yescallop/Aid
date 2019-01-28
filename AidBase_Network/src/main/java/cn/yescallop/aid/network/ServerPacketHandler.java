package cn.yescallop.aid.network;

import cn.yescallop.aid.network.protocol.EchoPacket;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author Scallop Ye
 * A server-side abstract ChannelHandler implementation
 * for Packet handling with idle management.
 */
public abstract class ServerPacketHandler extends PacketHandler {

    private int idleCount = 0;

    @Override
    public final void channelRead(ChannelHandlerContext ctx, Object msg) {
        idleCount = 0;
        if (msg instanceof Packet && !(msg instanceof EchoPacket)) {
            this.packetReceived(ctx, (Packet) msg);
        }
    }

    @Override
    public final void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
            if (idleCount >= Network.MAXIMUM_TIMEOUT_COUNT) {
                state = ChannelState.CONNECTION_LOST;
                ctx.close();
            }
            ctx.channel().writeAndFlush(EchoPacket.INSTANCE_PING);
            idleCount++;
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
