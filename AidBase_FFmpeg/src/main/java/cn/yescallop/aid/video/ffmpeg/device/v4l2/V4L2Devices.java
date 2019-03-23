package cn.yescallop.aid.video.ffmpeg.device.v4l2;

import cn.yescallop.aid.video.ffmpeg.util.InputFormats;
import org.bytedeco.javacpp.avformat;

import static org.bytedeco.javacpp.avformat.avformat_open_input;

public class V4L2Devices {

    private V4L2Devices() {
        //no instance
    }

    public static int openInput(avformat.AVFormatContext ctx, int id) {
        return avformat_open_input(ctx, "/dev/video" + id, InputFormats.VIDEO4LINUX2, null);
    }
}
