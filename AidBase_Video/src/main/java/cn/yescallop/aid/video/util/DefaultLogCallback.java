package cn.yescallop.aid.video.util;

import java.util.logging.Level;

import static org.bytedeco.javacpp.avutil.*;

/**
 * @author Scallop Ye
 */
public class DefaultLogCallback implements LogCallback {

    public static final DefaultLogCallback INSTANCE;

    static {
        INSTANCE = new DefaultLogCallback();
    }

    private DefaultLogCallback() {
    }

    @Override
    public synchronized void call(int level, String msg) {
        if (level >= 0)
            level &= 0xff;
        else return;

        if (level > AV_LOG_INFO)
            return;

        if (level == AV_LOG_INFO) {
            System.out.println(msg);
        } else {
            System.err.println(msg);
        }
    }
}
