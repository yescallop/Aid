package cn.yescallop.aid.client.api;

/**
 * @author Magical Sheep
 */
public interface UIHandler {
    void resize();

    void release();

    void showDialog(String heading, String body);
}
