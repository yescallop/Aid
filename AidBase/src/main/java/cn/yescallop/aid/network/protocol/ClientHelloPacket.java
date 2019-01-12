package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

public class ClientHelloPacket extends Packet {

    public int mtu;

    @Override
    public int id() {
        return ID_CLIENT_HELLO;
    }

    @Override
    public void readFrom(ByteBuf in) {
        //TODO: Calculate MTU
    }

    @Override
    public void writeTo(ByteBuf out) {
        //TODO: Write MTU testing bytes
    }
}
