package cn.yescallop.aid.network.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public abstract class Packet {

    public static final int ID_SERVER_HELLO = 0x00;
    public static final int ID_CLIENT_HELLO = 0x01;
    public static final int ID_DEVICE_HELLO = 0x02;
    public static final int ID_ECHO = 0x03;
    public static final int ID_STATUS = 0x04;
    public static final int ID_EVENT = 0x05;
    public static final int ID_VIDEO = 0x06;
    public static final int ID_DEVICE_LIST = 0x07;


    public static Packet from(int id, ByteBuf in) {
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
            case ID_ECHO:
                packet = in.readBoolean() ? EchoPacket.INSTANCE_PONG : EchoPacket.INSTANCE_PING;
                return packet;
            case ID_STATUS:
                packet = new StatusPacket();
                break;
            case ID_EVENT:
                packet = new EventPacket();
                break;
            case ID_VIDEO:
                packet = new VideoPacket();
                break;
            case ID_DEVICE_LIST:
                packet = new DeviceListPacket();
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
