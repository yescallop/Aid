package cn.yescallop.aid.device;

import cn.yescallop.aid.network.Network;
import cn.yescallop.aid.network.util.NetUtil;
import io.netty.channel.Channel;

import java.net.Inet4Address;
import java.net.SocketException;
import java.util.Map;

/**
 * @author Scallop Ye
 */
public class Device {

    protected static Map<Inet4Address, byte[]> addresses;

    static {
        try {
            addresses = NetUtil.siteLocalAddressesWithMAC();
        } catch (SocketException e) {
            System.out.println("Unable to get network interface info");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        try {
            Channel channel = Network.startClient("127.0.0.1", 9000, new DeviceHandler());
            System.out.println("Connected to " + channel.remoteAddress());
            System.in.read();
            channel.close().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
