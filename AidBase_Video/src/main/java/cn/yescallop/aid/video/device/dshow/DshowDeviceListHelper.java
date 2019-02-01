package cn.yescallop.aid.video.device.dshow;

import cn.yescallop.aid.video.util.Logging;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;

import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.*;

/**
 * @author Scallop Ye
 */
public class DshowDeviceListHelper {

    private static final String VIDEO_HEADER = "DirectShow video devices (some may be both video and audio devices)";
    private static final String AUDIO_HEADER = "DirectShow audio devices";
    private static final String VIDEO_NONE = "Could not enumerate video devices (or none found).";
    private static final String AUDIO_NONE = "Could not enumerate audio only devices (or none found).";

    private static final String NAME_PREFIX = " \"";
    private static final String ALTERNATIVE_NAME_PREFIX = "    Alternative name \"";

    private static List<DshowDevice> list;
    private static Boolean audioOnly;
    private static String name;
    private static String errMsg;

    private DshowDeviceListHelper() {
        //no instance
    }

    private static final Callback_Pointer_int_BytePointer_Pointer CALLBACK_POINTER = new Callback_Pointer_int_BytePointer_Pointer() {
        @Override
        public void call(Pointer avcl, int level, BytePointer fmt, Pointer vl) {
            PointerPointer<AVClass> pp = new PointerPointer<>(avcl);
            AVClass avc = pp.get(AVClass.class);
            String ctxName = avc.item_name().call(avcl).getString();
            int[] print_prefix = {0}; //not printing the prefix
            byte[] line = new byte[1024];
            if (!ctxName.equals("dshow")) { //callback normally if not logged by dshow
                print_prefix[0] = 1; //printing the prefix
                int len = av_log_format_line2(avcl, level, fmt, vl, line, line.length, print_prefix);
                String msg = new String(line, 0, len);
                Logging.getCallback().call(level, msg);
                return;
            }

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
            else if (msg.startsWith(NAME_PREFIX)) { //name
                name = msg.substring(NAME_PREFIX.length(), msg.length() - 1);
            } else if (msg.startsWith("    Alternative name \"")) { //alternative name
                String alternativeName = msg.substring(ALTERNATIVE_NAME_PREFIX.length(), msg.length() - 1);
                list.add(new DshowDevice(name, alternativeName, audioOnly));
            }
        }
    };

    public synchronized static DshowDevice[] getDshowDeviceList() throws DshowException {
        list = new ArrayList<>();
        audioOnly = null;
        name = null;
        errMsg = null;

        AVFormatContext ctx = avformat_alloc_context();
        AVDictionary options = new AVDictionary();
        av_dict_set(options, "list_devices", "true", 0);
        AVInputFormat fmt = av_find_input_format("dshow");
        av_log_set_callback(CALLBACK_POINTER);
        avformat_open_input(ctx, "dummy", fmt, options);
        avformat_close_input(ctx);
        Logging.registerCallback();

        if (errMsg != null)
            throw new DshowException(errMsg);

        DshowDevice[] res = new DshowDevice[list.size()];
        list.toArray(res);

        list = null;
        audioOnly = null;
        name = null;
        errMsg = null;

        return res;
    }
}
