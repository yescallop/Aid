package cn.yescallop.aid.client.util;

import cn.yescallop.aid.client.ClientConsoleMain;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.protocol.DeviceListPacket;
import io.netty.buffer.ByteBufUtil;

import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Scallop Ye
 */
public class Util {

    private Util() {
        //no instance
    }

    public static void logDeviceList() {
        Map<Integer, DeviceListPacket.DeviceInfo> deviceList = ClientConsoleMain.deviceList();
        int count = deviceList.size();
        if (count == 0) {
            Logger.info("No device is connected at present");
            return;
        }
        Logger.info("List of " + count + " device(s):");
        for (DeviceListPacket.DeviceInfo one : deviceList.values()) {
            StringJoiner sj = new StringJoiner(", ");
            one.localAddresses.forEach((addr, mac) -> sj.add(addr.getHostAddress() + ":" + ByteBufUtil.hexDump(mac)));
            Logger.info(String.format("[%d] %s: %s: {%s}: %d", one.id, one.name, one.registerTime, sj, one.port));
        }
    }

}
