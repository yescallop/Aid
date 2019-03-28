package cn.yescallop.aid.device.hardware.servo;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.wiringpi.Gpio;

public class PwmServoController implements ServoController {

    private static int CLOCK = 192;
    private static int RANGE = 2000;

    private GpioPinPwmOutput pwm;
    private int minAngle;
    private int maxAngle;

    private static int degreeToPwmValue(int degree) {
        return Math.round((1.5f + degree / 135f * 1.1f) * RANGE / 20f);
    }

    public PwmServoController(Pin pin) {
        this(pin, -135, 135);
    }

    public PwmServoController(Pin pin, int minAngle, int maxAngle) {
        if (minAngle < -135 || maxAngle > 135 || maxAngle <= minAngle)
            throw new IllegalArgumentException("minAngle: " + minAngle + ", maxAngle: " + maxAngle);
        pwm = GpioFactory.getInstance().provisionPwmOutputPin(pin, 0);

        Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
        Gpio.pwmSetClock(CLOCK);
        Gpio.pwmSetRange(RANGE);

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
