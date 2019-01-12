package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

public class EventPacket extends Packet {

    public static final int CAMERA_DETECTED = 0;
    public static final int START_FORWARDING = 1;
    public static final int STOP_FORWARDING = 2;

    public int event;
    public int deviceNum = -1;

    @Override
    public int id() {
        return ID_EVENT;
    }

    @Override
    public void readFrom(ByteBuf in) {
        event = in.readByte() & 0xff;
        deviceNum = in.readInt();
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeByte(event);
        out.writeByte(deviceNum);
    }
}
