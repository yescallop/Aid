package cn.yescallop.aid.video.device;

import cn.yescallop.aid.video.device.dshow.DshowDevice;
import cn.yescallop.aid.video.device.dshow.DshowDeviceListHelper;
import cn.yescallop.aid.video.device.dshow.DshowException;
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

    public synchronized static DshowDevice[] getDshowDeviceList() throws DshowException {
        return DshowDeviceListHelper.getDshowDeviceList();
    }

}
