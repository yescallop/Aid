package cn.yescallop.aid.video;

import cn.yescallop.aid.video.device.dshow.DshowDeviceInfo;
import cn.yescallop.aid.video.device.dshow.DshowDevices;
import cn.yescallop.aid.video.device.dshow.DshowException;
import cn.yescallop.aid.video.util.Logging;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.avdevice;

import static org.bytedeco.javacpp.avformat.*;

/**
 * @author Scallop Ye
 * Test class for AidBase_Video
 */
public class Main {

    public static void main(String[] args) {
        Logging.registerCallback();
        avdevice.avdevice_register_all();

        DshowDeviceInfo[] devices;
        try {
            devices = DshowDevices.listDevices();
        } catch (DshowException e) {
            e.printStackTrace();
            return;
        }

        DshowDeviceInfo in = devices[0];
        System.out.printf("Input device: %s (%s)\n", in.friendlyName(), in.uniqueName());

        AVFormatContext ctx = avformat_alloc_context();

        if (DshowDevices.openInput(ctx, in) == 0) {
            System.out.println("Successfully opened input");
        }

        int i = avformat_find_stream_info(ctx, (PointerPointer<Pointer>) null);
        //TODO: Find out why this native method leads to exit code -1073741819 (0xC0000005)
        System.out.println(i);

        avformat_close_input(ctx);
    }
}
