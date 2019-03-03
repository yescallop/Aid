package cn.yescallop.aid.device.video;

import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.video.ffmpeg.util.LogCallback;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;

import static org.bytedeco.javacpp.avutil.*;

public class DeviceLogCallback implements LogCallback {

    public static final DeviceLogCallback INSTANCE;

    public static int logLevel = AV_LOG_INFO;

    static {
        INSTANCE = new DeviceLogCallback();
    }

    private DeviceLogCallback() {
    }

    @Override
    public synchronized void call(Pointer avcl, int level, BytePointer fmt, Pointer vl) {
        if (level < 0 || level > logLevel)
            return;
        level &= 0xff;

        int[] print_prefix = {1};
        byte[] line = new byte[1024];
        int len = av_log_format_line2(avcl, level, fmt, vl, line, line.length, print_prefix);
        String msg = new String(line, 0, len - 1);

        if (level >= AV_LOG_INFO) {
            Logger.info(msg);
        } else if (level >= AV_LOG_WARNING) {
            Logger.warning(msg);
        } else {
            Logger.severe(msg);
        }
    }
}
