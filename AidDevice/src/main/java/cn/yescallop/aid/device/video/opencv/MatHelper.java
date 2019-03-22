package cn.yescallop.aid.device.video.opencv;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core.Mat;

import static org.bytedeco.javacpp.avutil.*;
import static org.bytedeco.javacpp.swscale.*;
import static org.bytedeco.javacpp.opencv_core.CV_8UC3;

public class MatHelper {

    private static SwsContext swsContext;
    private static AVFrame swsFrame;
    private static Mat mat;

    private MatHelper() {
        //no instance
    }

    public static void init(avcodec.AVCodecContext decoder) {
        int width = decoder.width();
        int height = decoder.height();

        int size = av_image_get_buffer_size(AV_PIX_FMT_RGB24, width, height, 1);
        BytePointer outBuffer = new BytePointer(av_malloc(size));
        swsFrame = av_frame_alloc();
        swsFrame.format(AV_PIX_FMT_RGB24);
        swsFrame.width(width);
        swsFrame.height(height);
        av_image_fill_arrays(swsFrame.data(), swsFrame.linesize(), outBuffer, AV_PIX_FMT_RGB24, width, height, 1);

        swsContext = sws_getContext(width, height, decoder.pix_fmt(), width, height, AV_PIX_FMT_RGB24, SWS_BILINEAR, null, null, (DoublePointer) null);

        mat = new Mat(height, width, CV_8UC3, outBuffer);
    }

    public static Mat get() {
        return mat;
    }

    public static void convert(AVFrame frame) {
        sws_scale(swsContext, frame.data(), frame.linesize(), 0, frame.height(), swsFrame.data(), swsFrame.linesize());
    }
}
