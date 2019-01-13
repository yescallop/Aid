package cn.yescallop.aid.server;

import cn.yescallop.aid.network.Network;
import io.netty.channel.Channel;

/**
 * @author Scallop Ye
 */
public class ServerMain {

    public static void main(String[] args) {
        try {
            Channel channel = Network.startServer("0.0.0.0", 9000, new ServerHandler());
            System.out.println("Server started on " + channel.localAddress());
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
