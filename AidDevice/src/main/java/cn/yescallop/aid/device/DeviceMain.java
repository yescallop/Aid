package cn.yescallop.aid.device;

import cn.yescallop.aid.console.CommandReader;
import cn.yescallop.aid.console.Logger;
import cn.yescallop.aid.device.hardware.ControlUtil;
import cn.yescallop.aid.device.hardware.bluetooth.BluetoothHandler;
import cn.yescallop.aid.device.network.DeviceClientHandler;
import cn.yescallop.aid.device.network.DeviceServerHandler;
import cn.yescallop.aid.device.video.DeviceFrameHandler;
import cn.yescallop.aid.device.video.DeviceLogCallback;
import cn.yescallop.aid.network.Network;
import cn.yescallop.aid.network.util.NetUtil;
import cn.yescallop.aid.video.ffmpeg.FFmpegException;
import cn.yescallop.aid.video.ffmpeg.FrameGrabber;
import cn.yescallop.aid.video.ffmpeg.device.dshow.DshowDeviceInfo;
import cn.yescallop.aid.video.ffmpeg.device.dshow.DshowDevices;
import cn.yescallop.aid.video.ffmpeg.device.dshow.DshowException;
import cn.yescallop.aid.video.ffmpeg.device.v4l2.V4L2Devices;
import cn.yescallop.aid.video.ffmpeg.util.Logging;
import io.netty.channel.Channel;
import org.bytedeco.javacpp.*;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.SocketException;
import java.util.Map;

import static org.bytedeco.javacpp.avformat.avformat_alloc_context;

/**
 * @author Scallop Ye
 */
public class DeviceMain {

    public static final String SERVER_HOST = "192.168.0.104";
    public static final int SERVER_PORT = 9000;

    public static final String HOST = "0.0.0.0"; //TODO: Move these arguments to a configuration file
    public static final int PORT = 9001;

    public static final String ARDUINO_ADDRESS = "98:D3:32:11:3C:82";

    public static final int ID = 1;
    public static final String NAME = "测试设备";

    public static Channel clientChannel;
    private static Channel serverChannel;
    private static Map<Inet4Address, byte[]> addresses;
    private static boolean stopping = false;

    public static BluetoothHandler bluetooth;

    private static final long RECONNECTING_DELAY_MILLIS = 5000; //TODO: Move this to a configuration file

    static {
        try {
            addresses = NetUtil.getLocalAddressesWithMAC();
        } catch (SocketException e) {
            System.out.println("Unable to get network interface info");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Map<Inet4Address, byte[]> localAddresses() {
        return addresses;
    }

    public static void main(String[] args) {
        try {
            new CommandReader(new DeviceCommandHandler(), "> ").start();
        } catch (IOException e) {
            Logger.logException(e, "Error while initiating logger");
            System.exit(1);
        }

        try {
//            BluetoothConnector bc = new BluetoothConnector(ARDUINO_ADDRESS);
//            bc.start();
            bluetooth = new BluetoothHandler("/dev/rfcomm0", 2000);
        } catch (Exception e) {
            Logger.logException(e, "Error while initiating bluetooth");
            System.exit(1);
        }

        try {
            initVideo();
        } catch (FFmpegException e) {
            Logger.logException(e, "Error while initiating video");
            System.exit(1);
        }

        try {
            clientChannel = Network.startClient(SERVER_HOST, SERVER_PORT, new DeviceClientHandler());
            Logger.info("Connected to " + clientChannel.remoteAddress());

            serverChannel = Network.startServer(HOST, PORT, new DeviceServerHandler());
            Logger.info("Server started on " + HOST + ":" + PORT);
        } catch (Exception e) {
            Logger.logException(e, "Error while connecting to server");
            System.exit(1);
        }

        ControlUtil.registerHardware();
    }

    private static void initVideo() throws FFmpegException {
        Loader.load(opencv_core.class);
        Loader.load(opencv_imgproc.class);

        Logging.init(DeviceLogCallback.INSTANCE);
        avdevice.avdevice_register_all();

        avformat.AVFormatContext fmtCtx = avformat_alloc_context();
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            DshowDeviceInfo[] devices;
            try {
                devices = DshowDevices.listDevices();
            } catch (DshowException e) {
                Logger.logException(e);
                return;
            }

            DshowDeviceInfo in = devices[0];
            Logger.info(String.format("Input device: %s (%s)\n", in.friendlyName(), in.uniqueName()));

            if (DshowDevices.openInput(fmtCtx, in) == 0) {
                Logger.info("Successfully opened input");
            } else {
                Logger.severe("Could not open device input");
                System.exit(1);
                return;
            }
        } else {
            if (V4L2Devices.openInput(fmtCtx, 0) == 0) {
                Logger.info("Successfully opened input");
            } else {
                Logger.severe("Could not open device input");
                System.exit(1);
                return;
            }
        }

        FrameGrabber fg = new FrameGrabber(fmtCtx, new DeviceFrameHandler());
        fg.start();
    }

    private static Runnable RECONNECTING_RUNNABLE = () -> {
        while (true) {
            Logger.info("Attempting reconnecting to server...");
            try {
                clientChannel = Network.startClient(SERVER_HOST, SERVER_PORT, new DeviceClientHandler());
            } catch (Exception e) {
                try {
                    Thread.sleep(RECONNECTING_DELAY_MILLIS);
                } catch (InterruptedException e1) {
                    return;
                }
                continue;
            }
            Logger.info("Reconnected to server");
            break;
        }
    };

    public static void attemptReconnecting() {
        new Thread(RECONNECTING_RUNNABLE, "Re-connector").start();
    }

    public static void stop() {
        stopping = true;
        if (clientChannel != null) {
            Logger.info("Closing client channel...");
            try {
                clientChannel.close().sync();
            } catch (Exception e) {
                //ignored
            }
        }
        if (serverChannel != null) {
            Logger.info("Closing server channel...");
            try {
                serverChannel.close().sync();
            } catch (Exception e) {
                //ignored
            }
        }
        Logger.info("Device stopped");
        System.exit(0);
    }

    public static boolean isStopping() {
        return stopping;
    }
}
