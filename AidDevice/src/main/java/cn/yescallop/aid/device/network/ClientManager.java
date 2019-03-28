package cn.yescallop.aid.device.network;

import cn.yescallop.aid.device.hardware.ControlUtil;
import cn.yescallop.aid.network.protocol.Packet;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author Scallop Ye
 */
public class ClientManager {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private ClientManager() {
        //no instance
    }

    public static int size() {
        return channelGroup.size();
    }

    public static boolean isEmpty() {
        return channelGroup.isEmpty();
    }

    public static boolean isRegistered(Channel channel) {
        return channelGroup.contains(channel);
    }

    public static void registerClient(Channel channel) {
        channelGroup.add(channel);
        if (channelGroup.size() == 1) {
            ControlUtil.registerCameraServo();
        }
    }

    public static void unregisterClient(Channel channel) {
        channelGroup.remove(channel);
        if (channelGroup.size() == 0) {
            ControlUtil.unregisterCameraServo();
        }
    }

    public static void broadcastPacket(Packet packet) {
        channelGroup.writeAndFlush(packet);
    }

}
