package cn.yescallop.aid.video;

import cn.yescallop.aid.video.ffmpeg.DeviceFrameHandler;
import cn.yescallop.aid.video.ffmpeg.FrameGrabber;
import cn.yescallop.aid.video.ffmpeg.device.dshow.DshowDeviceInfo;
import cn.yescallop.aid.video.ffmpeg.device.dshow.DshowDevices;
import cn.yescallop.aid.video.ffmpeg.device.dshow.DshowException;
import cn.yescallop.aid.video.ffmpeg.util.Logging;
import org.bytedeco.javacpp.avdevice;

import static org.bytedeco.javacpp.avformat.*;

/**
 * @author Scallop Ye
 * Test class for AidBase_Video
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Logging.init();
        avdevice.avdevice_register_all();

        DshowDeviceInfo[] devices;
        try {
            devices = DshowDevices.listDevices();
        } catch (DshowException e) {
            e.printStackTrace();
            return;
        }

        DshowDeviceInfo in = devices[1];
        System.out.printf("Input device: %s (%s)\n", in.friendlyName(), in.uniqueName());

        AVFormatContext fmtCtx = avformat_alloc_context();

        if (DshowDevices.openInput(fmtCtx, in) == 0) {
            System.out.println("Successfully opened input");
        } else {
            System.err.println("Could not open device input");
            System.exit(1);
            return;
        }

        FrameGrabber fg = new FrameGrabber(fmtCtx, new DeviceFrameHandler());
        fg.start();
    }
}
