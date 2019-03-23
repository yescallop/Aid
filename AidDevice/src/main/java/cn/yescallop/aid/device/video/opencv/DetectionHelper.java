package cn.yescallop.aid.device.video.opencv;

import cn.yescallop.aid.console.Logger;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgproc;

import java.nio.ByteBuffer;

import static org.opencv.core.CvType.CV_8UC1;

//import org.bytedeco.javacpp.avutil;
//import org.bytedeco.javacpp.opencv_core.Size;

public class DetectionHelper {

    private DetectionHelper() {
        //no instance
    }

//    private static final Size SIZE_21 = new Size(21, 21);

    private static boolean firstFrame = true;
    private static Mat lastImage;
    private static Mat curImage;
    private static Mat moveObject;
    private static ByteBuffer bufferOfMoveObject;
    private static int size;

    private static int threshold = 40000;

    private static int times = 0;
    private static long lastTime = 0;

    public static void init(int height, int width) {
        lastImage = new Mat(height, width, CV_8UC1);
        curImage = new Mat(height, width, CV_8UC1);
        moveObject = new Mat(height, width, CV_8UC1);

        size = height * width;

        bufferOfMoveObject = moveObject.data().position(0).limit(size).asByteBuffer();
    }

    public static int threshold() {
        return threshold;
    }

    public static void threshold(int threshold) {
        DetectionHelper.threshold = threshold;
    }

    public static boolean detect(Mat frame) {
        if (firstFrame) {
            opencv_imgproc.cvtColor(frame, lastImage, opencv_imgproc.COLOR_RGB2GRAY);
//            opencv_imgproc.GaussianBlur(lastImage, lastImage, SIZE_21, 0);
            firstFrame = false;
            return false;
        }

        opencv_imgproc.cvtColor(frame, curImage, opencv_imgproc.COLOR_RGB2GRAY);
//        opencv_imgproc.GaussianBlur(curImage, curImage, SIZE_21, 0);
        opencv_core.absdiff(curImage, lastImage, moveObject);

        Mat tmp = lastImage;
        lastImage = curImage;
        curImage = tmp;

        return checkCount();
    }

    private static boolean checkCount() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            byte v = bufferOfMoveObject.get(i);
            if ((v & (byte) 0b11110000) != 0)
                count++;
        }

        if (count >= threshold) {
            Logger.info(String.valueOf(count));

            long curTime = System.currentTimeMillis();
            if ((curTime - lastTime) >= 1000) {
                times = 0;
            }
            lastTime = curTime;

            times++;
        }
        if (times >= 5) {
            times = 0;
            return true;
        }
        return false;
    }
}
