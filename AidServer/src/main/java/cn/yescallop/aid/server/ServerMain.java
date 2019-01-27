package cn.yescallop.aid.server;

import cn.yescallop.aid.console.CommandReader;
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
            CommandReader.info("Server started on " + HOST + ":" + PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        CommandReader.info("Closing server channel...");
        try {
            channel.close().sync();
        } catch (InterruptedException e) {
            //ignored
        }
        CommandReader.info("Server stopped");
        System.exit(0);
    }
}
