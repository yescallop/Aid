package cn.yescallop.aid.server;

import cn.yescallop.aid.console.CommandHandler;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.server.management.DeviceManager;
import cn.yescallop.aid.server.util.Util;

/**
 * @author Scallop Ye
 */
public class ServerCommandHandler implements CommandHandler {

    @Override
    public void handle(String cmd, String[] args) {
        switch (cmd) {
            case "stop":
                ServerMain.stop();
                break;
            case "list":
                if (DeviceManager.count() == 0) {
                    Logger.info("No device is connected at present.");
                } else {
                    Util.logDeviceList(DeviceManager.deviceArray());
                }
                break;
            default:
                Logger.info("Invalid command");
        }
    }

    @Override
    public void userInterrupt() {
        ServerMain.stop();
    }
}
