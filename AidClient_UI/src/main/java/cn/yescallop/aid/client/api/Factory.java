package cn.yescallop.aid.client.api;

import cn.yescallop.aid.client.network.ClientHandler;
import cn.yescallop.aid.client.network.DeviceInfo;
import cn.yescallop.aid.network.protocol.DeviceListPacket;
import cn.yescallop.aid.network.protocol.RequestPacket;
import io.netty.channel.Channel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Magical Sheep
 */
public class Factory {
    private static UIHandler currentPage = null;
    private static StringProperty consoleInfo;

    private static ObservableList<DeviceInfo> onlineDeviceList;

    private static Channel channel;

    private static boolean connectStatus = false;
    private static boolean stopping = false;

    private static Stage stage;

    private Factory() {
    }

    static {
        consoleInfo = new SimpleStringProperty("");
        onlineDeviceList = FXCollections.observableArrayList();
    }

    public static class UIData {

        /**
         * 注册页面
         *
         * @param page 当前页面
         */
        public static void regPage(UIHandler page) {
            currentPage = page;
        }

        /**
         * 清除控制台页面内容
         */
        public static void clearConsoleInfo() {
            consoleInfo.setValue("");
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

        public static void setStage(Stage stage1){
            stage = stage1;
        }

        public static Stage getStage() {
            return stage;
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
            Platform.runLater(() -> {
                consoleInfo.setValue(consoleInfo.getValue() + msg + "\n");
            });
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
         * @param deviceInfos 在线设备名单
         */
        public static void updateDeviceList(DeviceListPacket.DeviceInfo[] deviceInfos) {
            onlineDeviceList.clear();
            List<DeviceInfo> list = new ArrayList<>();
            for (DeviceListPacket.DeviceInfo deviceInfo : deviceInfos) {
                list.add(new DeviceInfo(deviceInfo));
            }
            onlineDeviceList.addAll(list);
        }
    }

    public static class Network {

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
