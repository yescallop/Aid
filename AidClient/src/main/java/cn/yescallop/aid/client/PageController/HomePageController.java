package cn.yescallop.aid.client.PageController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import io.datafx.controller.ViewController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import javax.annotation.PostConstruct;

@ViewController(value = "/page/HomePage.fxml",title = "Home")
public class HomePageController {

    @FXML
    private StackPane root;
    @FXML
    private JFXButton test1;

    @PostConstruct
    public void init() {
        test1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> showDialog("test","Hello World"));
    }

    private void showDialog(String heading, String body) {
        JFXButton ok = new JFXButton("确定");
        ok.setPrefSize(70,35);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(heading));
        content.setBody(new Text(body));
        content.setActions(ok);
        JFXDialog dialog= new JFXDialog(root,content, JFXDialog.DialogTransition.BOTTOM);
        dialog.show();
        ok.setOnAction(event -> dialog.close());
    }
}
