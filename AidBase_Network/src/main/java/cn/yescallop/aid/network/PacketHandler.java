package cn.yescallop.aid.network;

import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Scallop Ye
 * Base abstract class of ChannelHandler implementation
 * for Packet handling with idle management.
 */
public abstract class PacketHandler extends ChannelInboundHandlerAdapter {

    ChannelState state = ChannelState.INACTIVE;
    private Throwable closeCause = null;

    @Override
    public final void channelActive(ChannelHandlerContext ctx) {
        state = ChannelState.ACTIVE;
        connectionEstablished(ctx);
    }

    /**
     * 判断该异常是否运行时异常，是则交由 runtimeExceptionCaught 处理，否则改变状态。
     */
    @Override
    public final void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof RuntimeException) {
            runtimeExceptionCaught(ctx, (RuntimeException) cause);
        } else {
            state = ChannelState.EXCEPTION_CAUGHT;
            closeCause = cause;
        }
    }

    @Override
    public final void channelInactive(ChannelHandlerContext ctx) {
        connectionClosed(ctx, state, closeCause);
    }

    protected abstract void connectionEstablished(ChannelHandlerContext ctx);

    /**
     * 在连接断开时触发
     *
     * @param lastState 连接断开前 Channel 的状态
     * @param cause     若 lastState 为 EXCEPTION_CAUGHT，为异常实例，其余为 null
     */
    protected abstract void connectionClosed(ChannelHandlerContext ctx, ChannelState lastState, Throwable cause);

    protected abstract void packetReceived(ChannelHandlerContext ctx, Packet packet);

    protected abstract void runtimeExceptionCaught(ChannelHandlerContext ctx, RuntimeException re);
}
