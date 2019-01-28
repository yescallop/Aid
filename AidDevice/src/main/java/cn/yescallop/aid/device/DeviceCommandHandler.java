package cn.yescallop.aid.device;

import cn.yescallop.aid.console.CommandHandler;
import cn.yescallop.aid.console.Logger;

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
                Logger.info("Invalid command");
        }
    }

    @Override
    public void userInterrupt() {
        DeviceMain.stop();
    }
}
