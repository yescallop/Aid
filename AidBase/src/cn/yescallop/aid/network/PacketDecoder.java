package cn.yescallop.aid.network;

import cn.yescallop.aid.network.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.Arrays;
import java.util.List;

/**
 * @author Scallop Ye
 */
public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() <= Network.MAGIC.length) {
            out.add(in.retain());
        } else {
            byte[] header = new byte[Network.MAGIC.length];
            in.readBytes(header);
            if (Arrays.equals(header, Network.MAGIC)) {
                Packet packet = Packet.from(in);
                out.add(packet == null ? in.retain() : packet);
            } else out.add(in.retain());
        }
    }
}
