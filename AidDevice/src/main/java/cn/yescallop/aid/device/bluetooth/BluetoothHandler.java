package cn.yescallop.aid.device.bluetooth;

import cn.yescallop.aid.console.Logger;
import purejavacomm.*;

import java.io.*;
import java.util.TooManyListenersException;

/**
 * @author Magical Sheep
 */
public class BluetoothHandler {

    private SerialPort serialPort;//串口对象
    private OutputStream outputStream;

    /**
     * @param appname  串口名
     * @param timeout  超时时间
     * @param baudrate 波特率
     */
    public BluetoothHandler(String appname, int timeout, int baudrate) throws NoSuchPortException, PortInUseException, TooManyListenersException, IOException {
        CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(appname);
        serialPort = (SerialPort) commPortIdentifier.open(appname, timeout);
        serialPort.notifyOnDataAvailable(true);
        serialPort.addEventListener(new EventListener());
        outputStream = serialPort.getOutputStream();
    }

    public void write(String msg) throws IOException {
        outputStream.write(msg.getBytes());
    }

    private class EventListener implements SerialPortEventListener {
        private InputStream inputStream;
        private BufferedReader bufferedReader;
        private String msg;

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                try {
                    inputStream = serialPort.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    while ((msg = bufferedReader.readLine()) != null) {
                        Logger.info(msg);
                    }
                } catch (Exception e) {
                    Logger.severe("从串口读取数据异常！错误：" + e.getMessage());
                }
            }
        }
    }
}
