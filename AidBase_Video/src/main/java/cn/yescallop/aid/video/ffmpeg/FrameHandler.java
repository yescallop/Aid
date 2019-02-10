package cn.yescallop.aid.video.ffmpeg;

import org.bytedeco.javacpp.avutil;

/**
 * @author Scallop Ye
 */
public interface FrameHandler {

    void frameGrabbed(avutil.AVFrame frame);

    void eofReached();

    void exceptionCaught(Throwable cause);
}
