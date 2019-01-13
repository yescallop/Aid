package cn.yescallop.aid.network.protocol;

import cn.yescallop.aid.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

/**
 * @author Scallop Ye
 */
public class DeviceHelloPacket extends Packet {

    public String name;
    public byte[][] macs;

    @Override
    public int id() {
        return ID_DEVICE_HELLO;
    }

    @Override
    public void readFrom(ByteBuf in) {
        name = PacketUtil.readUTF8(in);
        int cnt = in.readInt();
        macs = new byte[cnt][6];
        for (int i = 0; i < cnt; i++) {
            in.readBytes(macs[i]);
        }
    }

    @Override
    public void writeTo(ByteBuf out) {
        PacketUtil.writeUTF8(out, name);
        out.writeInt(macs.length);
        for (byte[] mac : macs) {
            out.writeBytes(mac);
        }
    }
}
