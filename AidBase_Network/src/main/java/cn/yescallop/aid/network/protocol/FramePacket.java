package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author Scallop Ye
 */
public class FramePacket extends Packet {

    public int deviceId;
    public long time;
    public int size;
    public ByteBuf buf;

    @Override
    public int id() {
        return ID_VIDEO;
    }

    @Override
    public void readFrom(ByteBuf in) {
        deviceId = in.readInt();
        time = in.readLong();
        size = in.readInt();
        int i = in.readableBytes();
        buf = Unpooled.directBuffer(i, i);
        in.readBytes(buf);
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeInt(deviceId);
        out.writeLong(time);
        out.writeInt(size);
        out.writeBytes(buf);
    }
}
