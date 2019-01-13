package cn.yescallop.aid.device;

import cn.yescallop.aid.device.handler.DeviceHandler;
import cn.yescallop.aid.network.Network;
import cn.yescallop.aid.network.util.NetUtil;
import io.netty.channel.Channel;

import java.net.Inet4Address;
import java.net.SocketException;
import java.util.Map;

/**
 * @author Scallop Ye
 */
public class DeviceMain {

    private static Map<Inet4Address, byte[]> addresses;

    static {
        try {
            addresses = NetUtil.siteLocalAddressesWithMAC();
        } catch (SocketException e) {
            System.out.println("Unable to get network interface info");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Map<Inet4Address, byte[]> addresses() {
        return addresses;
    }

    public static void main(String[] args) {
        try {
//            Channel serverChannel = Network.startServer("0.0.0.0", 9001, new DeviceServerHandler());
            Channel clientChannel = Network.startClient("127.0.0.1", 9000, new DeviceHandler());
            System.out.println("Connected to " + clientChannel.remoteAddress());
            System.in.read();
            clientChannel.close().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
