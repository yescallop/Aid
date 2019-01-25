package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public class EchoPacket extends Packet {

    public static final int TYPE_PING = 0;
    public static final int TYPE_PONG = 1;

    public static final EchoPacket INSTANCE_PING = new EchoPacket(TYPE_PING);
    public static final EchoPacket INSTANCE_PONG = new EchoPacket(TYPE_PONG);

    public final int type;

    private EchoPacket(int type) {
        this.type = type;
    }

    @Override
    public int id() {
        return ID_ECHO;
    }

    @Override
    public void readFrom(ByteBuf in) {
        //not implemented, use INSTANCE_PING and INSTANCE_PONG instead
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeByte(type);
    }
}
