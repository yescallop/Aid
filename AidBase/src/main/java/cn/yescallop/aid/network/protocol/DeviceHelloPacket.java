package cn.yescallop.aid.network.protocol;

import cn.yescallop.aid.network.util.NetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Scallop Ye
 */
public class DeviceHelloPacket extends Packet {

    public String name;
    public Map<Inet4Address, byte[]> addresses;

    @Override
    public int id() {
        return ID_DEVICE_HELLO;
    }

    @Override
    public void readFrom(ByteBuf in) {
        name = NetUtil.readUTF8(in);
        int cnt = in.readInt();
        addresses = new HashMap<>(cnt);
        for (int i = 0; i < cnt; i++) {
            byte[] addr = new byte[4];
            byte[] mac = new byte[6];
            in.readBytes(addr);
            in.readBytes(mac);
            try {
                addresses.put((Inet4Address) Inet4Address.getByAddress(addr), mac);
            } catch (UnknownHostException e) {
                //ignored
            }
        }
    }

    @Override
    public void writeTo(ByteBuf out) {
        NetUtil.writeUTF8(out, name);
        out.writeInt(addresses.size());
        addresses.forEach((addr, mac) -> {
            out.writeBytes(addr.getAddress());
            out.writeBytes(mac);
        });
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ");
        addresses.forEach((addr, mac) -> sj.add(addr.getHostAddress() + ":" + ByteBufUtil.hexDump(mac)));
        return "DeviceHelloPacket{" +
                "name='" + name + '\'' +
                ", addresses=[" + sj.toString() +
                "]}";
    }
}
