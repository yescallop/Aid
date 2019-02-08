package cn.yescallop.aid.video;

import cn.yescallop.aid.video.ffmpeg.FFmpegException;
import cn.yescallop.aid.video.ffmpeg.device.dshow.DshowDeviceInfo;
import cn.yescallop.aid.video.ffmpeg.device.dshow.DshowDevices;
import cn.yescallop.aid.video.ffmpeg.device.dshow.DshowException;
import cn.yescallop.aid.video.ffmpeg.util.DefaultLogCallback;
import cn.yescallop.aid.video.ffmpeg.util.Logging;
import cn.yescallop.aid.video.ffmpeg.util.Util;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.avdevice;
import org.bytedeco.javacpp.avutil;

import static org.bytedeco.javacpp.avcodec.*;
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

        DshowDeviceInfo in = devices[0];
        System.out.printf("Input device: %s (%s)\n", in.friendlyName(), in.uniqueName());

        AVFormatContext fmtCtx = avformat_alloc_context();

        if (DshowDevices.openInput(fmtCtx, in) == 0) {
            System.out.println("Successfully opened input");
        } else {
            System.err.println("Could not open device input");
            System.exit(1);
            return;
        }

        if (avformat_find_stream_info(fmtCtx, (PointerPointer) null) != 0)
            throw new FFmpegException("Could not find stream information");

        AVStream stream = Util.findVideoStream(fmtCtx);
        if (stream == null)
            throw new FFmpegException("Could not find video stream");

        AVCodec codec = avcodec_find_decoder(stream.codecpar().codec_id());
        if (codec == null)
            throw new FFmpegException("Codec not found");

        AVCodecContext codecCtx = avcodec_alloc_context3(codec);
        if (codecCtx == null)
            throw new FFmpegException("Could not open codec");

        //TODO: More operations

        avformat_close_input(fmtCtx);
        avcodec_free_context(codecCtx);
    }
}
