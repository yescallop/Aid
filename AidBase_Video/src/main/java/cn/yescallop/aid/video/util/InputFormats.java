package cn.yescallop.aid.video.util;

import static org.bytedeco.javacpp.avformat.av_find_input_format;
import static org.bytedeco.javacpp.avformat.AVInputFormat;

/**
 * @author Scallop Ye
 */
public interface InputFormats {
    AVInputFormat DSHOW = av_find_input_format("dshow");
    AVInputFormat VIDEO4LINUX2 = av_find_input_format("video4linux2");
}
