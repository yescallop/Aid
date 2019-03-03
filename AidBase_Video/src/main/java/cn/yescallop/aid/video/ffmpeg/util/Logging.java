package cn.yescallop.aid.video.ffmpeg.util;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;

import static org.bytedeco.javacpp.avutil.*;

/**
 * @author Scallop Ye
 * We can't have more than one FunctionPointer.
 * See https://github.com/bytedeco/javacpp-presets/issues/683
 */
public class Logging {

    private static LogCallback defaultCallback = DefaultLogCallback.INSTANCE;
    private static LogCallback callback = DefaultLogCallback.INSTANCE;

    private static final Callback_Pointer_int_BytePointer_Pointer CALLBACK_POINTER = new Callback_Pointer_int_BytePointer_Pointer() {
        @Override
        public synchronized void call(Pointer avcl, int level, BytePointer fmt, Pointer vl) {
            callback.call(avcl, level, fmt, vl);
        }
    };

    public static LogCallback getCallback() {
        return callback;
    }

    public static void setCallback(LogCallback callback) {
        Logging.callback = callback;
    }

    public static LogCallback getDefaultCallback() {
        return defaultCallback;
    }

    public static void resetDefaultCallback() {
        callback = defaultCallback;
    }

    public static void init() {
        defaultCallback = DefaultLogCallback.INSTANCE;
        callback = DefaultLogCallback.INSTANCE;
        av_log_set_callback(CALLBACK_POINTER);
    }

    /**
     * Sets the log callback in avutil
     * Remember to call this function before registering a callback
     */
    public static void init(LogCallback def) {
        defaultCallback = def;
        callback = def;
        av_log_set_callback(CALLBACK_POINTER);
    }
}
