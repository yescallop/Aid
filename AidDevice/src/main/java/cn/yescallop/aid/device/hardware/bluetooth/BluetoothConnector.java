package cn.yescallop.aid.device.hardware.bluetooth;

import cn.yescallop.aid.console.Logger;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;

import java.io.IOException;
import java.util.TooManyListenersException;

public class BluetoothConnector extends Thread {

    private Process rfcommProc;
    private BluetoothHandler handler;

    public BluetoothConnector(String address) throws IOException, TooManyListenersException, NoSuchPortException, PortInUseException {
        handler = new BluetoothHandler("/dev/rfcomm0", 2000);
    }

    public BluetoothHandler handler() {
        return handler;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            if (handler.checkTimeout()) {
                Logger.severe("TIMEOUT");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
