package cn.yescallop.aid.client;

import cn.yescallop.aid.console.CommandHandler;
import cn.yescallop.aid.console.Logger;

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
            default:
                Logger.info("Invalid command");
        }
    }

    @Override
    public void userInterrupt() {
        ClientConsoleMain.stop();
    }
}
