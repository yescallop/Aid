package cn.yescallop.aid.console;

/**
 * @author Scallop Ye
 */
public interface CommandHandler {

    void handle(String cmd, String[] args);

    void userInterrupt();
}
