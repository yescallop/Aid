package cn.yescallop.aid.client;

import cn.yescallop.aid.network.Network;
import cn.yescallop.aid.protocol.LoginPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Scallop Ye
 */
public class Client {

    public static void main(String[] args) {
        try {
            Network.startClient("127.0.0.1", 9000, new ClientHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
