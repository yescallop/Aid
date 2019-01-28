package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public class RequestPacket extends Packet {

    public static final int TYPE_DEVICE_LIST = 0;
    public static final int TYPE_START_FORWARDING = 1;
    public static final int TYPE_STOP_FORWARDING = 2;

    public int type;
    public int deviceId = -1;

    @Override
    public int id() {
        return ID_REQUEST;
    }

    @Override
    public void readFrom(ByteBuf in) {
        type = in.readByte() & 0xff;
        deviceId = in.readInt();
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeByte(type);
        out.writeInt(deviceId);
    }
}
