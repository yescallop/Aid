package cn.yescallop.aid.client;

import cn.yescallop.aid.console.CommandReader;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.Network;
import io.netty.channel.Channel;

/**
 * @author Scallop Ye
 */
public class ClientConsoleMain {

    protected static Channel channel;

    private static boolean stopping = false;

    public static void main(String[] args) {
        try {
//            Channel serverChannel = Network.startServer("0.0.0.0", 9001, new DeviceServerHandler());
            new CommandReader(new ClientCommandHandler(), "> ").start();
            channel = Network.startClient("127.0.0.1", 9000, new ClientHandler());
            Logger.info("Connected to " + channel.remoteAddress());
        } catch (Exception e) {
            Logger.info("Error while connecting to server");
            e.printStackTrace();
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
        Logger.info("Client stopped");
        System.exit(0);
    }

    public static boolean isStopping() {
        return stopping;
    }
}
