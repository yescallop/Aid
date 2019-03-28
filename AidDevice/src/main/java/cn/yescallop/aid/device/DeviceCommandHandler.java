package cn.yescallop.aid.device;

import cn.yescallop.aid.console.CommandHandler;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.device.hardware.ControlUtil;

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
            case "write":
                char c = args[0].charAt(0);
                if (args[0].length() != 1 || c < '0' || c > '5') {
                    Logger.warning("Value is expected to be between 0 and 5");
                    break;
                }
                int action = c - '0';
                ControlUtil.processAction(action);
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
