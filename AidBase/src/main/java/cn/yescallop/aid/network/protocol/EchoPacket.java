package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 * A simple hack?? for EchoPacket, 0 for Ping, 1 for Pong
 */
public abstract class EchoPacket extends Packet {

    public static final EchoPacket PING = new EchoPacket() {
        @Override
        public void writeTo(ByteBuf out) {
            out.writeBoolean(false);
        }
    };

    public static final EchoPacket PONG = new EchoPacket() {
        @Override
        public void writeTo(ByteBuf out) {
            out.writeBoolean(true);
        }
    };

    private EchoPacket() {
    }

    @Override
    public int id() {
        return ID_ECHO;
    }

    @Override
    public void readFrom(ByteBuf in) {
        //not implemented
    }
}
