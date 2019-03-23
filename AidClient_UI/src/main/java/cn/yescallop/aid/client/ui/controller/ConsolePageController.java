package cn.yescallop.aid.client.ui.controller;

import cn.yescallop.aid.client.api.Factory;
import cn.yescallop.aid.client.api.UIHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import io.datafx.controller.ViewController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javax.annotation.PostConstruct;

/**
 * @author Magical Sheep
 */
@ViewController(value = "/page/ConsolePage.fxml", title = "Console")
public class ConsolePageController extends UIHandler {

    @FXML
    private JFXTextArea console;
    @FXML
    private JFXButton connect;
    @FXML
    private BorderPane container;

    @PostConstruct
    public void init() {
        Factory.UIData.regPage(this);
        AnchorPane.setLeftAnchor(container, Factory.UIData.LRSpacing);
        AnchorPane.setRightAnchor(container, Factory.UIData.LRSpacing);
        AnchorPane.setTopAnchor(container, Factory.UIData.TBSpacing);
        AnchorPane.setBottomAnchor(container, Factory.UIData.TBSpacing);

        AnchorPane.setLeftAnchor(console, Factory.UIData.LRSpacing - 15);
        AnchorPane.setRightAnchor(console, Factory.UIData.LRSpacing - 15);
        AnchorPane.setTopAnchor(console, Factory.UIData.TBSpacing - 15);
        AnchorPane.setBottomAnchor(console, Factory.UIData.TBSpacing - 15);

        container.setMaxWidth(Factory.UIData.getScreenWidth());
        container.setMaxHeight(Factory.UIData.getScreenHeight());

        console.textProperty().bind(Factory.UIData.getConsoleInfo());
        connect.disableProperty().bind(new SimpleBooleanProperty(Factory.Network.isConnected()));
        connect.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> new Thread(Factory.Network::start).start());
    }

    @Override
    public void resize() {

    }

    @Override
    public void release() {

    }

}
