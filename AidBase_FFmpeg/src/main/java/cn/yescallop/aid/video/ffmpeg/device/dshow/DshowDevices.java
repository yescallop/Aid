package cn.yescallop.aid.video.ffmpeg.device.dshow;

import cn.yescallop.aid.video.ffmpeg.util.InputFormats;

import static org.bytedeco.javacpp.avformat.AVFormatContext;
import static org.bytedeco.javacpp.avformat.avformat_open_input;

/**
 * @author Scallop Ye
 */
public class DshowDevices {

    private DshowDevices() {
        //no instance
    }


    public synchronized static DshowDeviceInfo[] listDevices() throws DshowException {
        return DshowDeviceListHelper.listDevices();
    }

    public static int openInput(AVFormatContext ctx, DshowDeviceInfo in) {
        return avformat_open_input(ctx, in.type() + "=" + in.uniqueName(), InputFormats.DSHOW, null);
    }

    public static int openMultipleInput(AVFormatContext ctx, DshowDeviceInfo in1, DshowDeviceInfo in2) {
        if (in1.isAudioOnly() == in2.isAudioOnly())
            throw new IllegalArgumentException("two inputs must be of different types");
        return avformat_open_input(ctx, in1.type() + "=" + in1.uniqueName() + ":" +
                in2.type() + "=" + in2.uniqueName(), InputFormats.DSHOW, null);
    }
}
