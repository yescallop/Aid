package cn.yescallop.aid.network;

import cn.yescallop.aid.network.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.Arrays;
import java.util.List;

/**
 * @author Scallop Ye
 */
public class PacketDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        //MAGIC + 包ID + 包内容长度 + 包内容
        byte[] header = new byte[4];
        in.readBytes(header);
        if (!Arrays.equals(header, Network.MAGIC)) {
            ctx.close(); //协议不匹配则断开连接
            return;
        }
        int id = in.readByte() & 0xff;
        ByteBuf data = in.readBytes(in.readInt());
        Packet packet = Packet.from(id, data);
        data.release();
        if (packet != null)
            out.add(packet);
    }
}
