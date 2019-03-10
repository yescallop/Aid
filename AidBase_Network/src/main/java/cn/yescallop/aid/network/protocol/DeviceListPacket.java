package cn.yescallop.aid.network.protocol;

import cn.yescallop.aid.network.util.NetUtil;
import io.netty.buffer.ByteBuf;

import java.net.Inet4Address;
import java.util.Map;

/**
 * @author Scallop Ye
 */
public class DeviceListPacket extends Packet {

    public static final int TYPE_FULL = 0;
    public static final int TYPE_ADD = 1;
    public static final int TYPE_REMOVE = 2;

    public int type;
    public DeviceInfo[] list;

    @Override
    public int id() {
        return ID_DEVICE_LIST;
    }

    @Override
    public void readFrom(ByteBuf in) {
        type = in.readByte() & 0xff;
        int cnt = in.readInt();
        list = new DeviceInfo[cnt];
        for (int i = 0; i < cnt; i++) {
            DeviceInfo info = new DeviceInfo();
            info.id = in.readInt();
            if (type != TYPE_REMOVE) {
                info.name = NetUtil.readUTF8(in);
                info.localAddresses = NetUtil.readLocalAddresses(in);
                info.port = in.readShort() & 0xffff;
                info.registerTime = in.readLong();
            }
            list[i] = info;
        }
    }

    @Override
    public void writeTo(ByteBuf out) {
        out.writeByte(type);
        out.writeInt(list.length);
        for (DeviceInfo info : list) {
            out.writeInt(info.id);
            if (type != TYPE_REMOVE) {
                NetUtil.writeUTF8(out, info.name);
                NetUtil.writeLocalAddresses(out, info.localAddresses);
                out.writeShort(info.port);
                out.writeLong(info.registerTime);
            }
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
