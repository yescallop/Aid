package cn.yescallop.aid.video.ffmpeg.util;

import static org.bytedeco.javacpp.avformat.AVInputFormat;
import static org.bytedeco.javacpp.avformat.av_find_input_format;

/**
 * @author Scallop Ye
 */
public interface InputFormats {
    AVInputFormat DSHOW = av_find_input_format("dshow");
    AVInputFormat VIDEO4LINUX2 = av_find_input_format("video4linux2");
}
