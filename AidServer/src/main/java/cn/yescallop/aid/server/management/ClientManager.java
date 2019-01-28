package cn.yescallop.aid.server.management;

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

    public static boolean isRegistered(Channel channel) {
        return channelGroup.contains(channel);
    }

    public static void registerClient(Channel channel) {
        channelGroup.add(channel);
    }

    public static void unregisterClient(Channel channel) {
        channelGroup.remove(channel);
    }

    public static void batchPacket(Packet packet) {
        channelGroup.writeAndFlush(packet);
    }

}
