package cn.yescallop.aid.video.ffmpeg.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;

import static org.bytedeco.javacpp.avutil.*;
import static org.bytedeco.javacpp.swscale.*;

public class FXImageHelper {

    private int height;
    private int width;

    private SwsContext swsContext;
    private AVFrame swsFrame;

    public FXImageHelper(AVFrame firstFrame) {
        width = firstFrame.width();
        height = firstFrame.height();

        BytePointer outBuffer = new BytePointer(av_malloc(av_image_get_buffer_size(AV_PIX_FMT_RGB24, width, height, 1)));
        swsFrame = av_frame_alloc();
        swsFrame.format(AV_PIX_FMT_RGB24);
        swsFrame.width(width);
        swsFrame.height(height);
        av_image_fill_arrays(swsFrame.data(), swsFrame.linesize(), outBuffer, AV_PIX_FMT_RGB24, width, height, 1);

        swsContext = sws_getContext(width, height, firstFrame.format(), width, height, AV_PIX_FMT_RGB24, SWS_BILINEAR, null, null, (DoublePointer) null);
    }

    public Image convertFromAVFrame(AVFrame frame) {
        sws_scale(swsContext, frame.data(), frame.linesize(), 0, frame.height(), swsFrame.data(), swsFrame.linesize());

        byte[] data = new byte[height * width * 3];
        swsFrame.data(0).get(data);

        WritableImage wi = new WritableImage(width, height);
        PixelWriter pw = wi.getPixelWriter();
        pw.setPixels(0, 0, width, height, PixelFormat.getByteRgbInstance(), data, 0, width * 3);

        return wi;
    }

    public void free() {
        sws_freeContext(swsContext);
        av_frame_free(swsFrame);
    }
}
