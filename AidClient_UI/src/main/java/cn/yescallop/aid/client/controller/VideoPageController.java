package cn.yescallop.aid.client.controller;

import cn.yescallop.aid.client.api.UIHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.File;

/**
 * @author Magical Sheep
 */
@ViewController(value = "/page/VideoPage.fxml", title = "Video")
public class VideoPageController implements UIHandler {
    @FXML
    private StackPane root;
    @FXML
    private ImageView screen;

    @PostConstruct
    public void init() {
    }

    @Override
    public void sync() {

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
