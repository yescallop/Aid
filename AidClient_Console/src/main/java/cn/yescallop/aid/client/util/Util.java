package cn.yescallop.aid.client.util;

import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.protocol.DeviceListPacket;
import io.netty.buffer.ByteBufUtil;

import java.util.StringJoiner;

/**
 * @author Scallop Ye
 */
public class Util {

    private Util() {
        //no instance
    }

    public static void logDeviceList(DeviceListPacket.DeviceInfo[] deviceInfos) {
        int count = deviceInfos.length;
        Logger.info("List of " + count + " device(s):");
        for (DeviceListPacket.DeviceInfo one : deviceInfos) {
            StringJoiner sj = new StringJoiner(", ");
            one.localAddresses.forEach((addr, mac) -> sj.add(addr.getHostAddress() + ":" + ByteBufUtil.hexDump(mac)));
            Logger.info(String.format("[%d] %s: %s: {%s}: %d", one.id, one.name, one.registerTime, sj, one.port));
        }
    }

    public static void tryConnect(DeviceListPacket.DeviceInfo info) {

    }
}
