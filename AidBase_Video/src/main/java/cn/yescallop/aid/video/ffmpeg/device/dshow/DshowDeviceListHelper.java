package cn.yescallop.aid.video.ffmpeg.device.dshow;

import cn.yescallop.aid.video.ffmpeg.util.DefaultLogCallback;
import cn.yescallop.aid.video.ffmpeg.util.LogCallback;
import cn.yescallop.aid.video.ffmpeg.util.Logging;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;

import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.*;

/**
 * @author Scallop Ye
 * See libavdevice/dshow.c
 */
class DshowDeviceListHelper {

    private static final String VIDEO_HEADER = "DirectShow video devices (some may be both video and audio devices)";
    private static final String AUDIO_HEADER = "DirectShow audio devices";
    private static final String VIDEO_NONE = "Could not enumerate video devices (or none found).";
    private static final String AUDIO_NONE = "Could not enumerate audio only devices (or none found).";

    private static final String FRIENDLY_NAME_PREFIX = " \"";
    private static final String UNIQUE_NAME_PREFIX = "    Alternative name \"";

    private static List<DshowDeviceInfo> list;
    private static boolean audioOnly;
    private static String friendlyName;
    private static String errMsg;

    private DshowDeviceListHelper() {
        //no instance
    }

    private static final LogCallback LOG_CALLBACK = new LogCallback() {
        @Override
        public void call(Pointer avcl, int level, BytePointer fmt, Pointer vl) {
            PointerPointer<AVClass> pp = new PointerPointer<>(avcl);
            AVClass avc = pp.get(AVClass.class);
            String ctxName = avc.item_name().call(avcl).getString();

            if (!ctxName.equals("dshow")) { //callback by default if not logged by dshow
                DefaultLogCallback.INSTANCE.call(avcl, level, fmt, vl);
                return;
            }

            int[] print_prefix = {0}; //not printing the prefix
            byte[] line = new byte[1024];

            int len = av_log_format_line2(avcl, level, fmt, vl, line, line.length, print_prefix);
            String msg = new String(line, 0, len - 1); //skip a \n

            if (level != AV_LOG_INFO) { //error occurred
                if (!msg.equals(VIDEO_NONE) && !msg.equals(AUDIO_NONE)) {
                    errMsg = msg;
                }
                return;
            }

            if (msg.equals(VIDEO_HEADER))
                audioOnly = false;
            else if (msg.equals(AUDIO_HEADER))
                audioOnly = true;
            else if (msg.startsWith(FRIENDLY_NAME_PREFIX)) { //name
                friendlyName = msg.substring(FRIENDLY_NAME_PREFIX.length(), msg.length() - 1);
            } else if (msg.startsWith(UNIQUE_NAME_PREFIX)) { //alternative name
                String uniqueName = msg.substring(UNIQUE_NAME_PREFIX.length(), msg.length() - 1);
                list.add(new DshowDeviceInfo(friendlyName, uniqueName, audioOnly));
            }
        }
    };

    synchronized static DshowDeviceInfo[] listDevices() throws DshowException {
        list = new ArrayList<>();
        friendlyName = null;
        errMsg = null;

        AVFormatContext ctx = avformat_alloc_context();
        AVDictionary options = new AVDictionary();
        av_dict_set(options, "list_devices", "true", 0);
        AVInputFormat fmt = av_find_input_format("dshow");
        Logging.setCallback(LOG_CALLBACK);
        avformat_open_input(ctx, "dummy", fmt, options);
        avformat_close_input(ctx);
        Logging.setCallback(DefaultLogCallback.INSTANCE);

        if (errMsg != null)
            throw new DshowException(errMsg);

        DshowDeviceInfo[] res = new DshowDeviceInfo[list.size()];
        list.toArray(res);

        list = null;
        friendlyName = null;
        errMsg = null;

        return res;
    }
}
