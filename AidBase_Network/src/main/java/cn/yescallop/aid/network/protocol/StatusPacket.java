package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public class StatusPacket extends Packet {

    public static final int STATUS_ID_OCCUPIED = 0;
    public static final int STATUS_ERROR = 1;
    public static final int STATUS_DISCONNECTING = 2;
    public static final int STATUS_UPNP_UNREACHABLE = 3;

    public int status;

    @Override
    public int id() {
        return ID_STATUS;
    }

    @Override
    public void readFrom(ByteBuf in) {
        status = in.readByte() & 0xff;
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeByte(status);
    }
}
