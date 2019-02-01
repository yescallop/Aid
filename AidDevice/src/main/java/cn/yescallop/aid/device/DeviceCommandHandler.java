package cn.yescallop.aid.device;

import cn.yescallop.aid.console.CommandHandler;
import cn.yescallop.aid.console.Logger;

import java.io.IOException;

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
                try{
                    DeviceMain.bluetooth.write(args[0]);
                }catch (IOException e){
                    Logger.severe("Failed in sending the message");
                }
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
