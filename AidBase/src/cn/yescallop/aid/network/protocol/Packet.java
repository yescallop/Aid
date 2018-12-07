package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public abstract class Packet {

    private int id;

    public Packet(int id) {
        this.id = id;
    }

    public static Packet from(ByteBuf in) {
        int id = in.readUnsignedByte();

        Packet packet;
        switch (id) {
            case LoginPacket.ID:
                packet = new LoginPacket();
                break;
            default:
                return null;
        }

        packet.readFrom(in);
        return packet;
    }

    public int id() {
        return id;
    }

    public abstract void readFrom(ByteBuf in);

    public abstract void writeTo(ByteBuf out);
}
