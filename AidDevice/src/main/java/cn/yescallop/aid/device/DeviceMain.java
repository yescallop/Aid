package cn.yescallop.aid.device;

import cn.yescallop.aid.console.CommandReader;
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

    protected static Channel clientChannel;
    protected static Channel serverChannel;
    private static Map<Inet4Address, byte[]> addresses;
    private static boolean stopping = false;

    static {
        try {
            addresses = NetUtil.siteLocalAddressesWithMAC();
        } catch (SocketException e) {
            System.out.println("Unable to get network interface info");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Map<Inet4Address, byte[]> localAddresses() {
        return addresses;
    }

    public static void main(String[] args) {
        try {
//            Channel serverChannel = Network.startServer("0.0.0.0", 9001, new DeviceServerHandler());
            new CommandReader(new DeviceCommandHandler(), "> ").start();
            clientChannel = Network.startClient("127.0.0.1", 9000, new DeviceHandler());
            CommandReader.info("Connected to " + clientChannel.remoteAddress());
        } catch (Exception e) {
            CommandReader.info("Error while connecting to server");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void attemptReconnecting() {
        while (true) {
            CommandReader.info("Attempting reconnecting to server...");
            try {
                clientChannel = Network.startClient("127.0.0.1", 9000, new DeviceHandler());
            } catch (Exception e) {
                continue;
            }
            break;
        }
    }

    public static void stop() {
        stopping = true;
        CommandReader.info("Closing client channel...");
        try {
            clientChannel.close().sync();
        } catch (InterruptedException e) {
            //ignored
        }
        CommandReader.info("Device stopped");
        System.exit(0);
    }

    public static boolean isStopping() {
        return stopping;
    }
}
