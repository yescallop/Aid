package cn.yescallop.aid.client.controller;

import cn.yescallop.aid.client.ClientHandler;
import cn.yescallop.aid.client.api.Factory;
import cn.yescallop.aid.client.api.UIHandler;
import cn.yescallop.aid.network.Network;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import io.datafx.controller.ViewController;
import io.netty.channel.Channel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

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
        Factory.regPage(this);
        sync();
        connect.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> new Thread(this::start).start());
    }

    @Override
    public void sync() {
        console.setText(Factory.getConsoleInfo());
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

    /**
     * 连接服务器
     */
    private void start() {
        try {
            Channel channel = Network.startClient("127.0.0.1", 9000, new ClientHandler());
            Factory.println("Connected to " + channel.remoteAddress());
            Platform.runLater(() -> connect.setDisable(true));
            channel.closeFuture().sync();
        } catch (Exception e) {
            Factory.println(e.getMessage());
            Platform.runLater(() -> connect.setDisable(false));
        }
    }
}
