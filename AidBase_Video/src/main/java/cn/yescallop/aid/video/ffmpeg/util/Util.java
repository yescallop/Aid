package cn.yescallop.aid.video.ffmpeg.util;

import static org.bytedeco.javacpp.avformat.*;
import static org.bytedeco.javacpp.avutil.*;

/**
 * @author Scallop Ye
 */
public class Util {

    private Util() {
        //no instance
    }

    public static AVStream findVideoStream(AVFormatContext ctx) {
        int nb = ctx.nb_streams();
        for (int i = 0; i < nb; i++) {
            AVStream stream = ctx.streams(i);
            if (stream.codecpar().codec_type() == AVMEDIA_TYPE_VIDEO)
                return stream;
        }
        return null;
    }
}
