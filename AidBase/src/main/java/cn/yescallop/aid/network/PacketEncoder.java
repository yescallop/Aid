package cn.yescallop.aid.network;

import cn.yescallop.aid.network.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Scallop Ye
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) {
        //MAGIC + 包ID + 4空字节 + 包内容，随后在4空字节位置写入包内容长度。
        out.writeBytes(Network.MAGIC);
        out.writeByte(msg.id());
        out.writeZero(4);
        msg.writeTo(out);
        int total = out.writerIndex();
        out.setInt(Network.MAGIC.length + 1, total - Network.MAGIC.length - 5);
    }
}
