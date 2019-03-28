package cn.yescallop.aid.device.hardware.bluetooth;

import cn.yescallop.aid.console.Logger;
import purejavacomm.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

/**
 * @author Magical Sheep
 */
public class BluetoothHandler {

    private SerialPort serialPort;//串口对象
    private OutputStream outputStream;
    private InputStream inputStream;

    private long lastResponse = -1;

    private static final int TIMEOUT_MILLIS = 2000;

    /**
     * @param appname  串口名
     * @param timeout  超时时间
     */
    public BluetoothHandler(String appname, int timeout) throws NoSuchPortException, PortInUseException, TooManyListenersException, IOException {
        CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(appname);
        serialPort = (SerialPort) commPortIdentifier.open(appname, timeout);
        serialPort.notifyOnDataAvailable(true);
        serialPort.addEventListener(new EventListener());
        outputStream = serialPort.getOutputStream();
        inputStream = serialPort.getInputStream();
    }

    public void writeAndFlush(int b) throws IOException {
        outputStream.write(b);
        outputStream.flush();
    }

    public boolean checkTimeout() {
        return lastResponse != -1 && (System.currentTimeMillis() - lastResponse) >= TIMEOUT_MILLIS;
    }

    private class EventListener implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                try {
                    inputStream.read();
                    Logger.info("HEARTBEAT");
                    lastResponse = System.currentTimeMillis();
                } catch (Exception e) {
                    Logger.logException(e, "Bluetooth exception");
                }
            }
        }
    }
}
