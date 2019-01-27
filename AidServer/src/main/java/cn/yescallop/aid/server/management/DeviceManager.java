package cn.yescallop.aid.server.management;

import cn.yescallop.aid.network.protocol.DeviceHelloPacket;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Scallop Ye
 */
public class DeviceManager {

    private static int nextId = 0;
    private static Map<Integer, Device> deviceMap = new LinkedHashMap<>();
    private static Map<Channel, Integer> channelIndex = new HashMap<>();

    private DeviceManager() {
        //no instance
    }

    public static int count() {
        return deviceMap.size();
    }

    public static Device[] deviceArray() {
        return deviceMap.values().toArray(new Device[0]);
    }

    public static Device registerDevice(Channel channel, DeviceHelloPacket packet) {
        Device device = new Device(nextId, packet, channel);
        deviceMap.put(nextId, device);
        channelIndex.put(channel, nextId);
        nextId++;
        return device;
    }

    public static boolean unregisterDevice(int id) {
        return deviceMap.remove(id) != null;
    }

    public static boolean unregisterDevice(Channel channel) {
        Integer id = channelIndex.remove(channel);
        if (id == null) // no such channel registered
            return false;
        deviceMap.remove(id);
        return true;
    }

    public static Integer idByChannel(Channel channel) {
        return channelIndex.get(channel);
    }

    public static Device deviceById(int id) {
        return deviceMap.get(id);
    }

    public static Device deviceByChannel(Channel channel) {
        Integer id = channelIndex.get(channel);
        if (id == null) // no such channel registered
            return null;
        return deviceMap.get(id);
    }

}
