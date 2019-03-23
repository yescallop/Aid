package cn.yescallop.aid.client.ui.controller;

import cn.yescallop.aid.client.api.Factory;
import cn.yescallop.aid.client.api.UIHandler;
import io.datafx.controller.ViewController;

import javax.annotation.PostConstruct;

/**
 * @author Magical Sheep
 */
@ViewController(value = "/page/ServerStatusPage.fxml", title = "ServerStatus")
public class ServerStatusPageController extends UIHandler {


    @PostConstruct
    public void init() {
        Factory.UIData.regPage(this);
    }

    @Override
    public void resize() {

    }

    @Override
    public void release() {

    }
}
