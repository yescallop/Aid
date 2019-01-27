package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public class VideoPacket extends Packet {

    public long time;
    public byte[] data;

    @Override
    public int id() {
        return ID_VIDEO;
    }

    @Override
    public void readFrom(ByteBuf in) {
        time = in.readLong();
        data = new byte[in.readableBytes()];
        in.readBytes(data);
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeLong(time);
        out.writeBytes(data);
    }
}
