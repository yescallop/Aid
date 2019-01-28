package cn.yescallop.aid.server.util;

import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.protocol.DeviceListPacket;
import cn.yescallop.aid.server.management.ClientManager;
import cn.yescallop.aid.server.management.Device;
import cn.yescallop.aid.server.management.DeviceManager;
import io.netty.channel.Channel;

/**
 * @author Scallop Ye
 * A utility class for server-side.
 */
public class Util {

    private Util() {
        //no instance
    }

    public static void logDeviceList(Device[] devices) {
        int count = devices.length;
        Logger.info("List of " + count + " device(s):");
        for (Device one : devices) {
            Logger.info(String.format("[%d] %s: %s", one.id(), one.name(), one.channel().remoteAddress()));
        }
    }

    /**
     * @param channel the channel to identify
     * @return 0 if device, 1 if client, or else -1
     */
    public static int identifyChannel(Channel channel) {
        if (DeviceManager.isRegistered(channel))
            return 0;
        if (ClientManager.isRegistered(channel))
            return 1;
        return -1;
    }

    public static DeviceListPacket createDeviceListPacket() {
        DeviceListPacket res = new DeviceListPacket();
        Device[] devices = DeviceManager.deviceArray();
        res.deviceInfos = new DeviceListPacket.DeviceInfo[devices.length];
        for (int i = 0; i < devices.length; i++) {
            res.deviceInfos[i] = devices[i].toDeviceInfo();
        }
        return res;
    }
}
