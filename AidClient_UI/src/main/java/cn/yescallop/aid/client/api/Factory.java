package cn.yescallop.aid.client.api;

import javafx.application.Platform;

/**
 * @author Magical Sheep
 */
public class Factory {
    private static UIHandler currentPage = null;
    private static String consoleInfo;

    private Factory() {
    }

    static {
        consoleInfo = "";
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
     * 获取当前页面的Controller对象
     *
     * @return UIHandler
     */
    public static UIHandler getCurrentPage() {
        return currentPage;
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
     * 向控制台页面输出字符串
     *
     * @param msg 输出内容
     */
    public static void println(String msg) {
        consoleInfo = consoleInfo + msg;
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
     * 清除控制台页面内容
     */
    public static void clearConsoleInfo() {
        consoleInfo = "";
    }
}
