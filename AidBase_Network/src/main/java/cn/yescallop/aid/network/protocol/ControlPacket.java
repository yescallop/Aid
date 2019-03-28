package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class ControlPacket extends Packet {

    public static final int ACTION_START = 0;
    public static final int ACTION_STOP = 1;
    public static final int ACTION_LEFT = 2;
    public static final int ACTION_RIGHT = 3;
    public static final int ACTION_CAMERA_LEFT = 4;
    public static final int ACTION_CAMERA_RIGHT = 5;

    public int action;

    private static final Map<Integer, ControlPacket> MAP = new HashMap<>();

    static {
        for (int i = 0; i <= 5; i++) {
            ControlPacket p = new ControlPacket();
            p.action = i;
            MAP.put(i, p);
        }
    }

    public static ControlPacket byAction(int id) {
        return MAP.get(id);
    }

    @Override
    public int id() {
        return ID_CONTROL;
    }

    @Override
    public void readFrom(ByteBuf in) {
         action = in.readByte() & 0xff;
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeByte(action);
    }
}
