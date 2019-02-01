package cn.yescallop.aid.device;

import cn.yescallop.aid.console.CommandReader;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.device.handler.BluetoothHandler;
import cn.yescallop.aid.device.handler.DeviceHandler;
import cn.yescallop.aid.network.Network;
import cn.yescallop.aid.network.util.NetUtil;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
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

    protected static BluetoothHandler bluetooth;

    private static final long RECONNECTING_DELAY_MILLIS = 5000; //TODO: Move this to a configuration file

    static {
        try {
            addresses = NetUtil.getSiteLocalAddressesWithMAC();
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
            Logger.info("Connected to " + clientChannel.remoteAddress());
            bluetooth = new BluetoothHandler("COM1", 2000, 9600);
        } catch (NoSuchPortException e) {
            Logger.severe("Failed in connecting because of the wrong port");
            System.exit(1);
        } catch (PortInUseException e) {
            Logger.severe("Failed in connecting because the port is using");
            System.exit(1);
        } catch (Exception e) {
            Logger.severe("Error while connecting to server");
//            e.printStackTrace();
            System.exit(1);
        }
    }

    private static Runnable RECONNECTING_RUNNABLE = () -> {
        while (true) {
            Logger.info("Attempting reconnecting to server...");
            try {
                clientChannel = Network.startClient("127.0.0.1", 9000, new DeviceHandler());
            } catch (Exception e) {
                try {
                    Thread.sleep(RECONNECTING_DELAY_MILLIS);
                } catch (InterruptedException e1) {
                    return;
                }
                continue;
            }
            Logger.info("Reconnected to server");
            break;
        }
    };

    public static void attemptReconnecting() {
        new Thread(RECONNECTING_RUNNABLE, "Re-connector").start();
    }

    public static void stop() {
        stopping = true;
        if (clientChannel != null) {
            Logger.info("Closing client channel...");
            try {
                clientChannel.close().sync();
            } catch (Exception e) {
                //ignored
            }
        }
        Logger.info("Device stopped");
        System.exit(0);
    }

    public static boolean isStopping() {
        return stopping;
    }
}
