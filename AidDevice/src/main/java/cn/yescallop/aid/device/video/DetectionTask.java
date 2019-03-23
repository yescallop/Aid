package cn.yescallop.aid.device.video;

import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.device.DeviceMain;
import cn.yescallop.aid.device.video.opencv.DetectionHelper;
import cn.yescallop.aid.device.video.opencv.MatHelper;
import cn.yescallop.aid.network.protocol.EventPacket;
import org.bytedeco.javacpp.opencv_core.Mat;

import static org.bytedeco.javacpp.opencv_core.CV_8UC3;

public class DetectionTask extends Thread {

    private final DeviceFrameHandler handler;
    private final Mat originMat;
    private final Mat mat;

    private static final EventPacket CAMERA_DETECTED_EVENT = new EventPacket();

    static {
        CAMERA_DETECTED_EVENT.event = EventPacket.EVENT_CAMERA_DETECTED;
    }

    public DetectionTask(DeviceFrameHandler handler) {
        this.handler = handler;
        this.originMat = MatHelper.get();
        this.mat = new Mat(handler.height, handler.width, CV_8UC3);
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                detect();
            }
        } catch (Exception e) {
            handler.exceptionCaught(e);
        } finally {
            free();
        }
    }

    private void detect() throws InterruptedException {
        synchronized (handler) {
            handler.wait();
            originMat.copyTo(mat);
        }
        if (DetectionHelper.detect(mat)) {
            Logger.warning("MOVEMENT DETECTED");
            DeviceMain.clientChannel.writeAndFlush(CAMERA_DETECTED_EVENT);
        }
    }

    private void free() {
        mat.release();
    }
}
