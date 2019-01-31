package cn.yescallop.aid.video.util;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;

import static org.bytedeco.javacpp.avutil.*;

/**
 * @author Scallop Ye
 */
public class Logging {

    private static LogCallback callback = (level, msg) -> System.out.print(msg);

    private static final Callback_Pointer_int_BytePointer_Pointer CALLBACK_POINTER = new Callback_Pointer_int_BytePointer_Pointer() {
        @Override
        public void call(Pointer avcl, int level, BytePointer fmt, Pointer vl) {
            int[] print_prefix = {1};
            byte[] line = new byte[1024];
            int len = av_log_format_line2(avcl, level, fmt, vl, line, line.length, print_prefix);
            String msg = new String(line, 0, len);
            Logging.callback.call(level, msg);
        }
    };

    public static LogCallback getCallback() {
        return callback;
    }

    public static void registerCallback(LogCallback callback) {
        Logging.callback = callback;
        registerCallback();
    }

    public static void registerCallback() {
        av_log_set_callback(CALLBACK_POINTER);
    }
}
