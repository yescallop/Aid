package cn.yescallop.aid.server.management;

import cn.yescallop.aid.network.protocol.DeviceHelloPacket;
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

    protected Device(int id, DeviceHelloPacket packet, Channel channel) {
        this.id = id;
        this.name = packet.name;
        this.localAddresses = packet.localAddresses;
        this.channel = channel;
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
}
