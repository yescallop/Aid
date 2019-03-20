package cn.yescallop.aid.client;

import cn.yescallop.aid.client.network.ClientHandler;
import cn.yescallop.aid.client.network.DeviceHandler;
import cn.yescallop.aid.console.CommandReader;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.Network;
import cn.yescallop.aid.network.protocol.DeviceListPacket;
import cn.yescallop.aid.video.ffmpeg.util.Logging;
import io.netty.channel.Channel;

import java.net.Inet4Address;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Scallop Ye
 */
public class ClientConsoleMain {

    protected static Channel channel;
    protected static Channel deviceChannel;

    protected static Map<Integer, DeviceListPacket.DeviceInfo> deviceList = new LinkedHashMap<>();

    private static boolean stopping = false;

    public static void main(String[] args) {
        Logging.init(ClientLogCallback.INSTANCE);
        try {
//            Channel serverChannel = Network.startServer("0.0.0.0", 9001, new DeviceServerHandler());
            new CommandReader(new ClientCommandHandler(), "> ").start();
            channel = Network.startClient("127.0.0.1", 9000, new ClientHandler());
            Logger.info("Connected to " + channel.remoteAddress());
        } catch (Exception e) {
            Logger.info("Error while connecting to server");
            Logger.logException(e);
            System.exit(1);
        }
    }

    public static void stop() {
        stopping = true;
        if (channel != null) {
            Logger.info("Closing client channel...");
            try {
                channel.close().sync();
            } catch (Exception e) {
                //ignored
            }
        }
        if (deviceChannel != null) {
            Logger.info("Closing device channel...");
            try {
                deviceChannel.close().sync();
            } catch (Exception e) {
                //ignored
            }
        }
        Logger.info("Client stopped");
        System.exit(0);
    }

    public static Map<Integer, DeviceListPacket.DeviceInfo> deviceList() {
        return deviceList;
    }

    public static void updateDeviceList(int type, DeviceListPacket.DeviceInfo[] list) {
        if (type == DeviceListPacket.TYPE_FULL) {
            deviceList.clear();
            for (DeviceListPacket.DeviceInfo deviceInfo : list) {
                deviceList.put(deviceInfo.id, deviceInfo);
            }
        } else if (type == DeviceListPacket.TYPE_ADD) {
            for (DeviceListPacket.DeviceInfo deviceInfo : list) {
                deviceList.put(deviceInfo.id, deviceInfo);
            }
        } else if (type == DeviceListPacket.TYPE_REMOVE) {
            for (DeviceListPacket.DeviceInfo deviceInfo : list) {
                deviceList.remove(deviceInfo.id);
            }
        }
    }

    public static boolean isStopping() {
        return stopping;
    }

    public static void tryConnect(DeviceListPacket.DeviceInfo info) {
        for (Inet4Address addr : info.localAddresses.keySet()) {
            Logger.info("Trying " + addr.getHostAddress() + ":" + info.port);
            try {
                deviceChannel = Network.startClient(addr, info.port, new DeviceHandler());
            } catch (Exception e) {
                Logger.warning("Unable to connect to " + addr.getHostAddress() + ":" + info.port);
                continue;
            }
            return;
        }
        Logger.severe("Device " + info.id + " seems unreachable");
    }
}
