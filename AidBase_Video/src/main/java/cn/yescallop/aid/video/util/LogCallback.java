package cn.yescallop.aid.video.util;

/**
 * @author Scallop Ye
 */
@FunctionalInterface
public interface LogCallback {

    void call(int level, String msg);
}
