package cn.yescallop.aid.client;

import cn.yescallop.aid.console.CommandHandler;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.protocol.RequestPacket;

/**
 * @author Scallop Ye
 */
public class ClientCommandHandler implements CommandHandler {

    @Override
    public void handle(String cmd, String[] args) {
        switch (cmd) {
            case "stop":
                ClientConsoleMain.stop();
                break;
            case "list":
                RequestPacket listRequest = new RequestPacket();
                listRequest.type = RequestPacket.TYPE_DEVICE_LIST;
                ClientConsoleMain.channel.writeAndFlush(listRequest);
                break;
            default:
                Logger.info("Invalid command");
        }
    }

    @Override
    public void userInterrupt() {
        ClientConsoleMain.stop();
    }
}
