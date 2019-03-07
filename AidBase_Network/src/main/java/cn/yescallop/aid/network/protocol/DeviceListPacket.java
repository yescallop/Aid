package cn.yescallop.aid.network.protocol;

import cn.yescallop.aid.network.util.NetUtil;
import io.netty.buffer.ByteBuf;

import java.net.Inet4Address;
import java.util.Map;

/**
 * @author Scallop Ye
 */
public class DeviceListPacket extends Packet {

    public DeviceInfo[] deviceInfos;

    @Override
    public int id() {
        return ID_DEVICE_LIST;
    }

    @Override
    public void readFrom(ByteBuf in) {
        int cnt = in.readInt();
        deviceInfos = new DeviceInfo[cnt];
        for (int i = 0; i < cnt; i++) {
            DeviceInfo info = new DeviceInfo();
            info.id = in.readInt();
            info.name = NetUtil.readUTF8(in);
            info.localAddresses = NetUtil.readLocalAddresses(in);
            info.port = in.readShort() & 0xffff;
            info.registerTime = in.readLong();
            deviceInfos[i] = info;
        }
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeInt(deviceInfos.length);
        for (DeviceInfo info : deviceInfos) {
            out.writeInt(info.id);
            NetUtil.writeUTF8(out, info.name);
            NetUtil.writeLocalAddresses(out, info.localAddresses);
            out.writeShort(info.port);
            out.writeLong(info.registerTime);
        }
    }

    public static class DeviceInfo {
        public int id;
        public String name;
        public Map<Inet4Address, byte[]> localAddresses;
        public int port;
        public long registerTime;
    }
}
