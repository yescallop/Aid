package cn.yescallop.aid.device.video.opencv;

import cn.yescallop.aid.console.Logger;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_imgproc;

import java.nio.ByteBuffer;

import static org.opencv.core.CvType.CV_8UC1;

public class DetectionHelper {

    private DetectionHelper() {
        //no instance
    }

    private static final Size SIZE_21 = new Size(21, 21);

    private static boolean firstFrame = true;
    private static Mat frame;
    private static Mat lastImage;
    private static Mat curImage;
    private static Mat moveObject;
    private static ByteBuffer bufferOfMoveObject;
    private static int size;

    private static int threshold = 20000;

    public static void init(int height, int width) {
        lastImage = new Mat(height, width, CV_8UC1);
        curImage = new Mat(height, width, CV_8UC1);
        moveObject = new Mat(height, width, CV_8UC1);
        frame = MatHelper.get();

        size = height * width;

        bufferOfMoveObject = moveObject.data().position(0).limit(size).asByteBuffer();
    }

    public static int threshold() {
        return threshold;
    }

    public static void threshold(int threshold) {
        DetectionHelper.threshold = threshold;
    }

    public static boolean detect() {
        if (firstFrame) {
            opencv_imgproc.cvtColor(frame, lastImage, opencv_imgproc.COLOR_RGB2GRAY);
            opencv_imgproc.GaussianBlur(lastImage, lastImage, SIZE_21, 0);
            firstFrame = false;
            return false;
        }

        opencv_imgproc.cvtColor(frame, curImage, opencv_imgproc.COLOR_RGB2GRAY);
        opencv_imgproc.GaussianBlur(curImage, curImage, SIZE_21, 0);
        opencv_core.absdiff(curImage, lastImage, moveObject);
        opencv_imgproc.threshold(moveObject, moveObject, 15, 255, opencv_imgproc.THRESH_BINARY);// 差分图像二值化

        Mat tmp = lastImage;
        lastImage = curImage;
        curImage = tmp;

        return checkCount();
    }

    private static boolean checkCount() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (bufferOfMoveObject.get(i) == (byte) 0xff)
                count++;
        }

        if (count >= threshold) {
            Logger.info(String.valueOf(count));
            return true;
        } else {
            return false;
        }
    }
}
