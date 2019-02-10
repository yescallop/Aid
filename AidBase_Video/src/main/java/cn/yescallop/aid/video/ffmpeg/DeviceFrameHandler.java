package cn.yescallop.aid.video.ffmpeg;

import org.bytedeco.javacpp.avutil;

/**
 * @author Scallop Ye
 */
public class DeviceFrameHandler implements FrameHandler {

    private int frameCount = 0;
    private long lastTime = -1;

    @Override
    public void frameGrabbed(avutil.AVFrame frame) {
        frameCount++;
        long curTime = System.currentTimeMillis();
        if (lastTime != -1) {
            long dur = curTime - lastTime;
            if (dur >= 1000) {
                float fps = frameCount / (dur / 1000f);
                System.out.println("FPS: " + String.format("%.1f", fps));
                frameCount = 0;
                lastTime = curTime;
            }
        } else lastTime = curTime;

        //TODO: Process frames
    }

    @Override
    public void eofReached() {

    }

    @Override
    public void exceptionCaught(Throwable cause) {
        cause.printStackTrace();
    }
}
