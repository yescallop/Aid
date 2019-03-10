package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public class EventPacket extends Packet {

    public static final int EVENT_CAMERA_DETECTED = 0;

    public int event;
    public int deviceId = -1;

    @Override
    public int id() {
        return ID_EVENT;
    }

    @Override
    public void readFrom(ByteBuf in) {
        event = in.readByte() & 0xff;
        deviceId = in.readInt();
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeByte(event);
        out.writeInt(deviceId);
    }
}
