package cn.yescallop.aid.client;

import cn.yescallop.aid.console.CommandHandler;

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
                System.out.println("Invalid command");
        }
    }

    @Override
    public void userInterrupt() {
        ClientConsoleMain.stop();
    }
}
