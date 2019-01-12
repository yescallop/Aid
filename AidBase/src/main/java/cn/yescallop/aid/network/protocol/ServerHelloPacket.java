package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public class ServerHelloPacket extends Packet {

    public int mtu;

    @Override
    public int id() {
        return ID_SERVER_HELLO;
    }

    @Override
    public void readFrom(ByteBuf in) {
        mtu = in.readShort() & 0xffff;
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeShort(mtu);
    }
}
