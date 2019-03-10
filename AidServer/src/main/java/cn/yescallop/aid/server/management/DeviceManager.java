package cn.yescallop.aid.server.management;

import cn.yescallop.aid.network.protocol.DeviceHelloPacket;
import cn.yescallop.aid.network.protocol.DeviceListPacket;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Scallop Ye
 */
public class DeviceManager {

    private static Map<Integer, Device> idDeviceMap = new LinkedHashMap<>();
    private static Map<Channel, Device> channelDeviceMap = new HashMap<>();

    private DeviceManager() {
        //no instance
    }

    public static int count() {
        return idDeviceMap.size();
    }

    public static Device[] deviceArray() {
        return idDeviceMap.values().toArray(new Device[0]);
    }

    public static boolean isRegistered(Channel channel) {
        return channelDeviceMap.containsKey(channel);
    }

    public static Device registerDevice(Channel channel, DeviceHelloPacket packet) {
        if (idDeviceMap.containsKey(packet.id))
            return null;
        Device device = new Device(packet.id, packet, channel);
        idDeviceMap.put(packet.id, device);
        channelDeviceMap.put(channel, device);
        return device;
    }

    public static boolean unregisterDevice(int id) {
        Device dev = idDeviceMap.remove(id);
        if (dev == null)
            return false;
        channelDeviceMap.remove(dev.channel());
        updateListRemoval(dev);
        return true;
    }

    public static boolean unregisterDevice(Channel channel) {
        Device dev = channelDeviceMap.remove(channel);
        if (dev == null)
            return false;
        idDeviceMap.remove(dev.id());
        updateListRemoval(dev);
        return true;
    }

    private static void updateListRemoval(Device dev) {
        DeviceListPacket p = new DeviceListPacket();
        p.type = DeviceListPacket.TYPE_REMOVE;
        p.list = new DeviceListPacket.DeviceInfo[] { dev.toDeviceInfo() };
        ClientManager.broadcastPacket(p);
    }

    public static int idByChannel(Channel channel) {
        return channelDeviceMap.get(channel).id();
    }

    public static Device deviceById(int id) {
        return idDeviceMap.get(id);
    }

    public static Device deviceByChannel(Channel channel) {
        return channelDeviceMap.get(channel);
    }

}
