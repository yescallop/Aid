package cn.yescallop.aid.video.device;

import org.bytedeco.javacpp.avdevice;

/**
 * @author Scallop Ye
 */
public class Devices {

    static {
        avdevice.avdevice_register_all();
    }

    private Devices() {
        //no instance
    }

    public static DshowDevice[] getDshowDeviceList() {
        return DshowDeviceListHelper.getDshowDeviceList();
    }

}
