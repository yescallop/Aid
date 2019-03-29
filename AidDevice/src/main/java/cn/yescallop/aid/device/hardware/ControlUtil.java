package cn.yescallop.aid.device.hardware;

import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.device.DeviceMain;
import cn.yescallop.aid.device.hardware.servo.SoftPwmServoController;
import cn.yescallop.aid.network.protocol.ControlPacket;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import java.io.IOException;

public class ControlUtil {

    public static final Pin PIN_SERVO_CAMERA = RaspiPin.GPIO_04;
    public static final Pin PIN_SERVO_LOCK = RaspiPin.GPIO_05;
    public static final Pin PIN_BUTTON_UNLOCK = RaspiPin.GPIO_26;
    public static final Pin PIN_BUTTON_LOCK = RaspiPin.GPIO_27;

    private static SoftPwmServoController cameraServo;

    private static int cameraAngle = 0;

    private ControlUtil() {
        //no instance
    }

    public static void registerHardware() {
        new LockController(
                PIN_SERVO_LOCK,
                PIN_BUTTON_UNLOCK,
                PIN_BUTTON_LOCK
        ).register();
    }

    public static void registerCameraServo() {
        cameraServo = new SoftPwmServoController(PIN_SERVO_CAMERA);
        cameraServo.write(0);
    }

    public static void unregisterCameraServo() {
        if (cameraServo != null) {
            cameraServo.unregister();
        }
    }

    public static void processAction(int action) {
        if (action < 4) {
            try {
                DeviceMain.bluetooth.writeAndFlush(action + '0');
                Logger.info("Action " + action + " written");
            } catch (IOException e) {
                Logger.severe("Failed in sending the message");
            }
        } else if (action < 6) {
            rotateCamera(action);
        }
    }

    private static boolean rotateCamera(int action) {
        if (cameraServo == null)
            return false;
        if (action == ControlPacket.ACTION_CAMERA_LEFT) {
            if (cameraAngle >= -120) {
                cameraAngle -= 15;
                cameraServo.write(cameraAngle);
                return true;
            }
        } else if (action == ControlPacket.ACTION_CAMERA_RIGHT) {
            if (cameraAngle <= 120) {
                cameraAngle += 15;
                cameraServo.write(cameraAngle);
                return true;
            }
        }
        return false;
    }
}
