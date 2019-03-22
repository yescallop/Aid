package cn.yescallop.aid.client;

import cn.yescallop.aid.client.util.Util;
import cn.yescallop.aid.console.CommandHandler;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.protocol.DeviceListPacket;

import java.util.Map;

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
                Util.logDeviceList();
                break;
            case "connect":
                if (args.length != 1) {
                    Logger.info("No device specified");
                    break;
                }
                int id;
                try {
                    id = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    Logger.info("Device ID must be an integer");
                    break;
                }

                Map<Integer, DeviceListPacket.DeviceInfo> deviceList = ClientConsoleMain.deviceList();
                if (deviceList.isEmpty()) {
                    Logger.warning("No device is available");
                    break;
                }

                DeviceListPacket.DeviceInfo info = deviceList.get(id);
                if (info == null) {
                    Logger.info("Device ID matching " + id + " not found");
                    break;
                }
                ClientConsoleMain.tryConnect(info);
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
