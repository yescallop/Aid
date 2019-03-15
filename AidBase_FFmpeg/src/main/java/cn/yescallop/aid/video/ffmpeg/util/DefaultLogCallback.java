package cn.yescallop.aid.video.ffmpeg.util;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;

import static org.bytedeco.javacpp.avutil.AV_LOG_INFO;
import static org.bytedeco.javacpp.avutil.av_log_format_line2;

/**
 * @author Scallop Ye
 * See libavutil/log.c
 */
public class DefaultLogCallback implements LogCallback {

    public static final DefaultLogCallback INSTANCE;

    public static int logLevel = AV_LOG_INFO;

    static {
        INSTANCE = new DefaultLogCallback();
    }

    private DefaultLogCallback() {
    }

    @Override
    public synchronized void call(Pointer avcl, int level, BytePointer fmt, Pointer vl) {
        if (level < 0 || level > logLevel)
            return;
        level &= 0xff;

        int[] print_prefix = {1};
        byte[] line = new byte[1024];
        int len = av_log_format_line2(avcl, level, fmt, vl, line, line.length, print_prefix);
        String msg = new String(line, 0, len);

        if (level >= AV_LOG_INFO) {
            System.out.print(msg);
        } else {
            System.err.print(msg);
        }
    }
}
