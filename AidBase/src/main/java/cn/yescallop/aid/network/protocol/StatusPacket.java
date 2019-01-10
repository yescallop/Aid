package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

public class StatusPacket extends Packet {

    public static final int RUNNING = 0;
    public static final int STOPPING = 1;
    public static final int CAMERA_DETECTED = 2;

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
