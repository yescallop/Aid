package cn.yescallop.aid.server;

import cn.yescallop.aid.network.Network;

/**
 * @author Scallop Ye
 */
public class Server {

    public static void main(String[] args) {
        try {
            Network.startServer(9000, new ServerHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
