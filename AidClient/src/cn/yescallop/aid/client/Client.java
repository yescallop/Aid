package cn.yescallop.aid.client;

import cn.yescallop.aid.network.Network;
import io.netty.channel.Channel;

/**
 * @author Scallop Ye
 */
public class Client {

    public static void main(String[] args) {
        try {
            Channel channel = Network.startClient("127.0.0.1", 9000, new ClientHandler());
            System.out.println("Connected to " + channel.remoteAddress());
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
