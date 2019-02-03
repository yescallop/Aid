package cn.yescallop.aid.video.util;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;

/**
 * @author Scallop Ye
 */
@FunctionalInterface
public interface LogCallback {

    /**
     * @param avcl A double pointer to AVClass
     * @param level The log level
     * @param fmt The format of the message
     * @param vl a va_list
     * The callback must be thread safe, even if the application does not use
     * threads itself as some codecs are multithreaded.
     */
    void call(Pointer avcl, int level, BytePointer fmt, Pointer vl);
}
