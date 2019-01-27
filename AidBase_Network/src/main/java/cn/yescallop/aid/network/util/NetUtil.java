package cn.yescallop.aid.network.util;

import io.netty.buffer.ByteBuf;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Scallop Ye
 * A utility class for network operations.
 */
public class NetUtil {

    private NetUtil() {
        //no instance
    }

    /**
     * 从 ByteBuf 内读取一个以 int 标长的 String，使用 UTF-8
     */
    public static String readUTF8(ByteBuf in) {
        byte[] buf = new byte[in.readInt()];
        in.readBytes(buf);
        return new String(buf, StandardCharsets.UTF_8);
    }

    /**
     * 向 ByteBuf 内写入一个以 int 标长的 String，使用 UTF-8
     */
    public static void writeUTF8(ByteBuf out, String str) {
        byte[] buf = str.getBytes(StandardCharsets.UTF_8);
        out.writeInt(buf.length);
        out.writeBytes(buf);
    }

    /**
     * 获取所有局域网 IPv4 网络适配器的 IP 地址与其对应的 MAC 地址
     */
    public static Map<Inet4Address, byte[]> siteLocalAddressesWithMAC() throws SocketException {
        Map<Inet4Address, byte[]> res = new HashMap<>();
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        nis:
        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            if (ni.isUp() && !ni.isLoopback() && !ni.isVirtual() && !ni.isPointToPoint()) {
                Enumeration<InetAddress> addrs = ni.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    if (addr instanceof Inet4Address && addr.isSiteLocalAddress()) {
                        res.put((Inet4Address) addr, ni.getHardwareAddress());
                        continue nis;
                    }
                }
            }
        }
        return res;
    }
}
