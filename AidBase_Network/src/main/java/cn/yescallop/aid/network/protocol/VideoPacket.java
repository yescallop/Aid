package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author Scallop Ye
 */
public class VideoPacket extends Packet {

    public long time;
    public int size;
    public ByteBuf buf;

    @Override
    public int id() {
        return ID_VIDEO;
    }

    @Override
    public void readFrom(ByteBuf in) {
        time = in.readLong();
        size = in.readInt();
        int i = in.readableBytes();
        buf = Unpooled.directBuffer(i, i);
        in.readBytes(buf);
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeLong(time);
        out.writeInt(size);
        out.writeBytes(buf);
    }
}
