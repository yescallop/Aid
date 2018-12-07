package cn.yescallop.aid.client;

import cn.yescallop.aid.network.Network;

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
