package cn.yescallop.aid.server.util;

import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.server.management.Device;

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
}
