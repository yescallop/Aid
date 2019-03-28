package cn.yescallop.aid.device.hardware;

import cn.yescallop.aid.device.hardware.servo.ServoController;
import cn.yescallop.aid.device.hardware.servo.SoftPwmServoController;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class LockController {

    private static final int ANGLE_LOCKED = -15;
    private static final int ANGLE_UNLOCKED = 120;

    private ServoController servo;
    private GpioPinDigitalInput unlockButton;
    private GpioPinDigitalInput lockButton;

    public LockController(Pin servoPin, Pin unlockButtonPin, Pin lockButtonPin) {
        this.servo = new SoftPwmServoController(servoPin, -40, 120);
        GpioController gpio = GpioFactory.getInstance();
        unlockButton = gpio.provisionDigitalInputPin(unlockButtonPin, PinPullResistance.PULL_UP);
        lockButton = gpio.provisionDigitalInputPin(lockButtonPin, PinPullResistance.PULL_UP);
    }


    public LockController register() {
        unlockButton.addListener((GpioPinListenerDigital) event -> servo.write(ANGLE_UNLOCKED));
        lockButton.addListener((GpioPinListenerDigital) event -> {
            if (event.getState().isLow()) {
                servo.write(ANGLE_LOCKED);
            } else {
                servo.write(ANGLE_UNLOCKED);
            }
        });
        return this;
    }
}
