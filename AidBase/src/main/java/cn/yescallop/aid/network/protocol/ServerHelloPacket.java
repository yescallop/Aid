package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public class ServerHelloPacket extends Packet {

    @Override
    public int id() {
        return ID_SERVER_HELLO;
    }

    @Override
    public void readFrom(ByteBuf in) {
    }

    @Override
    public void writeTo(ByteBuf out) {
    }
}
