package cn.yescallop.aid.server.util;

import cn.yescallop.aid.server.management.Device;

/**
 * @author Scallop Ye
 * A utility class for server-side.
 */
public class Util {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private Util() {
        //no instance
    }

    public static String createMessageForDeviceList(Device[] devices) {
        StringBuilder sb = new StringBuilder();
        int count = devices.length;
        sb.append("------ List of ").append(count).append(" device(s) ------").append(LINE_SEPARATOR);
        for (Device one : devices) {
            sb.append(String.format("[%d] %s: %s", one.id(), one.name(), one.channel().remoteAddress())).append(LINE_SEPARATOR);
        }
        return sb.toString();
    }
}
