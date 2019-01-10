package cn.yescallop.aid.device;

import cn.yescallop.aid.network.Network;
import io.netty.channel.Channel;

/**
 * @author Scallop Ye
 */
public class Device {

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
