package cn.yescallop.aid.server;

import cn.yescallop.aid.console.CommandReader;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.Network;
import io.netty.channel.Channel;

/**
 * @author Scallop Ye
 */
public class ServerMain {

    public static final String HOST = "0.0.0.0"; //TODO: Move these arguments to a configuration file
    public static final int PORT = 9000;

    protected static Channel channel;

    public static void main(String[] args) {
        System.out.println("Starting server for Aid...");
        try {
            new CommandReader(new ServerCommandHandler(), "> ").start();
            channel = Network.startServer(HOST, PORT, new ServerHandler());
            Logger.info("Server started on " + HOST + ":" + PORT);
        } catch (Exception e) {
            Logger.logException(e);
        }
    }

    public static void stop() {
        if (channel != null) {
            Logger.info("Closing server channel...");
            try {
                channel.close().sync();
            } catch (Exception e) {
                //ignored
            }
        }
        Logger.info("Server stopped");
        System.exit(0);
    }
}
