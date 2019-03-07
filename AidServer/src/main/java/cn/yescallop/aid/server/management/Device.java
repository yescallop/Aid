package cn.yescallop.aid.server.management;

import cn.yescallop.aid.network.protocol.DeviceHelloPacket;
import cn.yescallop.aid.network.protocol.DeviceListPacket;
import io.netty.channel.Channel;

import java.net.Inet4Address;
import java.util.Map;

/**
 * @author Scallop Ye
 */
public class Device {

    private int id;
    private String name;
    private Channel channel;
    private Map<Inet4Address, byte[]> localAddresses;
    private int port;
    private long registerTime;

    Device(int id, DeviceHelloPacket packet, Channel channel) {
        this.id = id;
        this.name = packet.name;
        this.localAddresses = packet.localAddresses;
        this.channel = channel;
        this.port = packet.port;
        this.registerTime = System.currentTimeMillis();
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Channel channel() {
        return channel;
    }

    public Map<Inet4Address, byte[]> localAddresses() {
        return localAddresses;
    }

    public int port() {
        return port;
    }

    public long registerTime() {
        return registerTime;
    }

    public DeviceListPacket.DeviceInfo toDeviceInfo() {
        DeviceListPacket.DeviceInfo info = new DeviceListPacket.DeviceInfo();
        info.id = this.id;
        info.name = this.name;
        info.localAddresses = this.localAddresses;
        info.port = port;
        info.registerTime = this.registerTime;
        return info;
    }
}
