package cn.yescallop.aid.video;

import cn.yescallop.aid.video.device.Devices;
import cn.yescallop.aid.video.device.dshow.DshowDevice;
import cn.yescallop.aid.video.device.dshow.DshowException;
import cn.yescallop.aid.video.util.Logging;

import java.util.Arrays;

/**
 * @author Scallop Ye
 */
public class Main {

    public static void main(String[] args) {
        Logging.registerCallback();
        try {
            DshowDevice[] devices = Devices.getDshowDeviceList();
            System.out.println(Arrays.toString(devices));
        } catch (DshowException e) {
            e.printStackTrace();
        }
    }
}
