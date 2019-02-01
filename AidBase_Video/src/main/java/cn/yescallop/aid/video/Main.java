package cn.yescallop.aid.video;

import cn.yescallop.aid.video.device.Devices;
import cn.yescallop.aid.video.util.Logging;

import java.util.Arrays;

/**
 * @author Scallop Ye
 */
public class Main {

    public static void main(String[] args) throws Exception{
        Logging.registerCallback();
        System.out.println(Arrays.toString(Devices.getDshowDeviceList()));
    }
}
