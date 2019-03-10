package cn.yescallop.aid.client.api;

import cn.yescallop.aid.client.network.ClientHandler;
import cn.yescallop.aid.client.network.DeviceInfo;
import cn.yescallop.aid.client.ui.frame.Frame;
import cn.yescallop.aid.network.protocol.DeviceListPacket;
import cn.yescallop.aid.network.protocol.RequestPacket;
import io.netty.channel.Channel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Magical Sheep
 */
public class Factory {

    private static Stage stage;
    private static UIHandler currentPage = null;
    private static StringProperty consoleInfo;
    protected static Map<Integer, DeviceListPacket.DeviceInfo> deviceListMap = new LinkedHashMap<>();
    private static ObservableList<DeviceInfo> onlineDeviceList;

    private Factory() {
    }

    static {
        consoleInfo = new SimpleStringProperty("");
        onlineDeviceList = FXCollections.observableArrayList();
    }

    public static class UIData {

        public final static double LRSpacing = 30; // 控件左右间距
        public final static double TBSpacing = 20; // 控件上下间距

        /**
         * 注册页面
         *
         * @param page 当前页面
         */
        public static void regPage(UIHandler page) {
            if (currentPage != null) currentPage.release();
            currentPage = page;
        }

        /**
         * 清除控制台页面内容
         */
        public static void clearConsoleInfo() {
            consoleInfo.setValue("");
        }

        public static void setStage(Stage stage1){
            stage = stage1;
        }

        public static ObservableList<DeviceInfo> getOnlineDeviceList() {
            return onlineDeviceList;
        }

        /**
         * 获取控制台消息缓存区的内容
         *
         * @return String
         */
        public static StringProperty getConsoleInfo() {
            return consoleInfo;
        }

        public static Stage getStage() {
            return stage;
        }

        /**
         * 计算当前窗口大小下的控件宽度
         *
         * @return 控件宽度
         */
        public static double getScreenWidth() {
            return stage.getWidth() - (2 * LRSpacing);
        }

        /**
         * 计算当前窗口大小下的控件高度
         *
         * @return 控件高度
         */
        public static double getScreenHeight() {
            if (stage.isFullScreen()) {
                return stage.getHeight() - Frame.getToolbarHeight() - (2 * TBSpacing);
            } else {
                return stage.getHeight() - Frame.getToolbarHeight() - (2 * TBSpacing) - 32;
            }
        }
    }

    public static class UI {

        /**
         * 获取当前页面的Controller对象
         *
         * @return UIHandler
         */
        public static UIHandler getCurrentPage() {
            return currentPage;
        }

        /**
         * 向控制台页面输出字符串
         *
         * @param msg 输出内容
         */
        public static void println(String msg) {
            Platform.runLater(() -> consoleInfo.setValue(consoleInfo.getValue() + msg + "\n"));
        }

        /**
         * 向当前页面弹出Dialog
         *
         * @param heading 弹窗标题
         * @param body    弹窗内容
         */
        public static void showDialog(String heading, String body) {
            Platform.runLater(() -> currentPage.showDialog(heading, body));
        }

        /**
         * 更新在线设备列表
         *
         * @param type 设备列表类型
         * @param list 在线设备名单
         */
        public static void updateDeviceList(int type, DeviceListPacket.DeviceInfo[] list) {
            if (type == DeviceListPacket.TYPE_FULL) {
                deviceListMap.clear();
                for (DeviceListPacket.DeviceInfo deviceInfo : list) {
                    deviceListMap.put(deviceInfo.id, deviceInfo);
                }
            } else if (type == DeviceListPacket.TYPE_ADD) {
                for (DeviceListPacket.DeviceInfo deviceInfo : list) {
                    deviceListMap.put(deviceInfo.id, deviceInfo);
                }
            } else if (type == DeviceListPacket.TYPE_REMOVE) {
                for (DeviceListPacket.DeviceInfo deviceInfo : list) {
                    deviceListMap.remove(deviceInfo.id);
                }
            }

            onlineDeviceList.clear();
            for (DeviceListPacket.DeviceInfo deviceInfo : deviceListMap.values()) {
                onlineDeviceList.add(new DeviceInfo(deviceInfo));
            }
        }
    }

    public static class Network {

        private static boolean connectStatus = false;
        private static boolean stopping = false;
        private static Channel channel;

        /**
         * 连接服务器
         */
        public static void start() {
            try {
                channel = cn.yescallop.aid.network.Network.startClient("127.0.0.1", 9000, new ClientHandler());
                Factory.UI.println("Connected to " + channel.remoteAddress());
                connectStatus = true;
            } catch (Exception e) {
                Factory.UI.println(e.getMessage());
                connectStatus = false;
            }
        }

        /**
         * 断开连接
         */
        public static void stop() {
            stopping = true;
            if (channel != null) {
                Factory.UI.println("Closing client channel...");
                connectStatus = false;
                onlineDeviceList.clear();
                try {
                    channel.close().sync();
                } catch (Exception e) {
                    //ignored
                }
            }
            stopping = false;
        }

        /**
         * 请求在线设备名单
         */
        public static void list() {
            if (isConnected()) {
                RequestPacket listRequest = new RequestPacket();
                listRequest.type = RequestPacket.TYPE_DEVICE_LIST;
                channel.writeAndFlush(listRequest);
            }
        }

        public static boolean isStopping() {
            return stopping;
        }

        public static boolean isConnected() {
            return connectStatus;
        }

    }

}
