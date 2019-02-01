package cn.yescallop.aid.video.util;

/**
 * @author Scallop Ye
 */
@FunctionalInterface
public interface LogCallback {

    /**
     * The callback must be thread safe, even if the application does not use
     * threads itself as some codecs are multithreaded.
     */
    void call(int level, String msg);
}
