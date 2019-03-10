package cn.yescallop.aid.client;

import cn.yescallop.aid.console.CommandHandler;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.network.protocol.DeviceListPacket;
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

                if (ClientConsoleMain.deviceInfos == null || ClientConsoleMain.deviceInfos.length == 0) {
                    Logger.warning("No device available, fetch a list first");
                    break;
                }

                DeviceListPacket.DeviceInfo info = null;
                for (DeviceListPacket.DeviceInfo one : ClientConsoleMain.deviceInfos) {
                    if (one.id == id)
                        info = one;
                }
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
