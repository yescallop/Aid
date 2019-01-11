package cn.yescallop.aid.network.protocol;

import cn.yescallop.aid.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

public class DeviceHelloPacket extends Packet {

    public String name;
    public byte[] mac;

    @Override
    public int id() {
        return ID_DEVICE_HELLO;
    }

    @Override
    public void readFrom(ByteBuf in) {
        name = PacketUtil.readUTF8(in);
        mac = new byte[6];
        in.readBytes(mac);
    }

    @Override
    public void writeTo(ByteBuf out) {
        PacketUtil.writeUTF8(out, name);
        out.writeBytes(mac);
    }
}