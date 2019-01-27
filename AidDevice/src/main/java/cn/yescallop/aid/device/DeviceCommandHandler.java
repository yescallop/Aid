package cn.yescallop.aid.device;

import cn.yescallop.aid.console.CommandHandler;

/**
 * @author Scallop Ye
 */
public class DeviceCommandHandler implements CommandHandler {
    @Override
    public void handle(String cmd, String[] args) {
        switch (cmd) {
            case "stop":
                DeviceMain.stop();
                break;
            default:
                System.out.println("Invalid command");
        }
    }

    @Override
    public void userInterrupt() {
        DeviceMain.stop();
    }
}
