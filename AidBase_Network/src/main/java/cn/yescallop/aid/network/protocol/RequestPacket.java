package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public class RequestPacket extends Packet {

    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_PREVIEW = 1;

    public int type;

    @Override
    public int id() {
        return ID_REQUEST;
    }

    @Override
    public void readFrom(ByteBuf in) {
        type = in.readByte() & 0xff;
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeByte(type);
    }
}
