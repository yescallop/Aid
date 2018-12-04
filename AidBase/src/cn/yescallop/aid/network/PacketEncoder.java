package cn.yescallop.aid.network;

import cn.yescallop.aid.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Scallop Ye
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) {
        out.writeBytes(Network.MAGIC);
        out.writeByte(msg.id());
        msg.writeTo(out);
    }
}
