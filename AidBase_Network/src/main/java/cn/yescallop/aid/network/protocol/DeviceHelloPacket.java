package cn.yescallop.aid.network.protocol;

import cn.yescallop.aid.network.util.NetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.net.Inet4Address;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Scallop Ye
 */
public class DeviceHelloPacket extends Packet {

    public int id;
    public String name;
    public Map<Inet4Address, byte[]> localAddresses;
    public int port;
    public int codecId;

    @Override
    public int id() {
        return ID_DEVICE_HELLO;
    }

    @Override
    public void readFrom(ByteBuf in) {
        id = in.readInt();
        name = NetUtil.readUTF8(in);
        localAddresses = NetUtil.readLocalAddresses(in);
        port = in.readShort() & 0xffff;
        codecId = in.readInt();
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeInt(id);
        NetUtil.writeUTF8(out, name);
        NetUtil.writeLocalAddresses(out, localAddresses);
        out.writeShort(port);
        out.writeInt(codecId);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ");
        localAddresses.forEach((addr, mac) -> sj.add(addr.getHostAddress() + ":" + ByteBufUtil.hexDump(mac)));
        return "DeviceHelloPacket{" +
                "id=" + id +
                ",name='" + name + '\'' +
                ", addresses=[" + sj.toString() +
                "]}";
    }
}
