package cn.yescallop.aid.video.ffmpeg;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avutil;

/**
 * @author Scallop Ye
 */
public interface FrameHandler {

    void frameGrabbed(avutil.AVFrame frame) throws FFmpegException;

    void eofReached();

    void exceptionCaught(Throwable cause);

    void init(avcodec.AVCodecContext decoder) throws FFmpegException;

    void close();
}
