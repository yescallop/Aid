package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public abstract class Packet {

    public static final int ID_LOGIN = 0x00;

    public static Packet from(ByteBuf in) {
        int id = in.readUnsignedByte();

        Packet packet;
        switch (id) {
            case ID_LOGIN:
                packet = new LoginPacket();
                break;
            default:
                return null;
        }

        packet.readFrom(in);
        return packet;
    }

    public abstract int id();

    public abstract void readFrom(ByteBuf in);

    public abstract void writeTo(ByteBuf out);
}
