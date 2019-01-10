package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

public class ClientHelloPacket extends Packet {

    @Override
    public int id() {
        return ID_CLIENT_HELLO;
    }

    @Override
    public void readFrom(ByteBuf in) {

    }

    @Override
    public void writeTo(ByteBuf out) {

    }
}
