package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * @author Scallop Ye
 */
public abstract class Packet {

    public static final int ID_SERVER_HELLO = 0x00;
    public static final int ID_CLIENT_HELLO = 0x01;
    public static final int ID_DEVICE_HELLO = 0x02;
    public static final int ID_STATUS = 0x03;

    public static Packet from(ByteBuf in) {
        int id = in.readUnsignedByte();

        Packet packet;
        switch (id) {
            case ID_SERVER_HELLO:
                packet = new ServerHelloPacket();
                break;
            case ID_CLIENT_HELLO:
                packet = new ClientHelloPacket();
                break;
            case ID_DEVICE_HELLO:
                packet = new DeviceHelloPacket();
                break;
            case ID_STATUS:
                packet = new StatusPacket();
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


    public static String readUTF8(ByteBuf in) {
        byte[] buf = new byte[in.readInt()];
        in.readBytes(buf);
        return new String(buf, StandardCharsets.UTF_8);
    }

    public static void writeUTF8(ByteBuf out, String str) {
        byte[] buf = str.getBytes(StandardCharsets.UTF_8);
        out.writeInt(buf.length);
        out.writeBytes(buf);
    }
}
