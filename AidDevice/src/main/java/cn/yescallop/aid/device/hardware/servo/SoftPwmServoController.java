package cn.yescallop.aid.device.hardware.servo;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;

public class SoftPwmServoController implements ServoController {

    private static int RANGE = 200;

    private GpioPinPwmOutput pwm;
    private int minAngle;
    private int maxAngle;

    private static int degreeToPwmValue(int degree) {
        return Math.round((1.5f + degree / 135f * 1.1f) * RANGE / 20f);
    }

    public SoftPwmServoController(Pin pin) {
        this(pin, -135, 135);
    }

    public SoftPwmServoController(Pin pin, int minAngle, int maxAngle) {
        if (minAngle < -135 || maxAngle > 135 || maxAngle <= minAngle)
            throw new IllegalArgumentException("minAngle: " + minAngle + ", maxAngle: " + maxAngle);
        pwm = GpioFactory.getInstance().provisionSoftPwmOutputPin(pin, 0);
        pwm.setPwmRange(RANGE);

        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }

    public boolean write(int degree) {
        if (degree < minAngle || degree > maxAngle)
            return false;
        int v = degreeToPwmValue(degree);

        pwm.setPwm(v);
        return true;
    }
}
