package cn.yescallop.aid.client.api;

import cn.yescallop.aid.client.Device;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * @author Magical Sheep
 */
public class Factory {
    private static UIHandler currentPage = null;
    private static String consoleInfo;

    private static ObservableList<Device> onlineDeviceList;

    private Factory() {
    }

    static {
        consoleInfo = "";
        onlineDeviceList = FXCollections.observableArrayList();
    }

    /**
     * 更新在线设备列表
     *
     * @param list 在线设备名单
     */
    public static void updateDeviceList(List<Device> list) {
        onlineDeviceList.clear();
        onlineDeviceList.addAll(list);
        Platform.runLater(() -> currentPage.sync());
    }

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
        consoleInfo = consoleInfo + msg + "\n";
        Platform.runLater(() -> currentPage.sync());
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
     * 获取控制台消息缓存区的内容
     *
     * @return String
     */
    public static String getConsoleInfo() {
        return consoleInfo;
    }

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
        consoleInfo = "";
    }

    public static ObservableList<Device> getOnlineDeviceList() {
        return onlineDeviceList;
    }

}
