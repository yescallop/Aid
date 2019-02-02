package cn.yescallop.aid.client.ui.controller;

import cn.yescallop.aid.client.api.Factory;
import cn.yescallop.aid.client.api.UIHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import io.datafx.controller.ViewController;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.annotation.PostConstruct;

/**
 * @author Magical Sheep
 */
@ViewController(value = "/page/ConsolePage.fxml", title = "Console")
public class ConsolePageController implements UIHandler {

    @FXML
    private StackPane root;
    @FXML
    private JFXTextArea console;
    @FXML
    private JFXButton connect;

    @PostConstruct
    public void init() {
        Factory.UIData.regPage(this);
        console.textProperty().bind(Factory.UIData.getConsoleInfo());
        connect.disableProperty().bind(new SimpleBooleanProperty(Factory.Network.isConnected()));
        connect.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> new Thread(Factory.Network::start).start());
    }

    @Override
    public void showDialog(String heading, String body) {
        JFXButton ok = new JFXButton("确定");
        ok.setPrefSize(70, 35);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(heading));
        content.setBody(new Text(body));
        content.setActions(ok);
        JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.BOTTOM);
        dialog.show();
        ok.setOnAction(event -> dialog.close());
    }

}
