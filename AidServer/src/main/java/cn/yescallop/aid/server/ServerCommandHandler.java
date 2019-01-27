package cn.yescallop.aid.server;

import cn.yescallop.aid.console.CommandHandler;
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
                    System.out.println("No device is connected at present.");
                } else {
                    String msg = Util.createMessageForDeviceList(DeviceManager.deviceArray());
                    System.out.println(msg);
                }
                break;
            default:
                System.out.println("Invalid command");
        }
    }

    @Override
    public void userInterrupt() {
        ServerMain.stop();
    }
}
