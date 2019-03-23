package cn.yescallop.aid.client.api;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Magical Sheep
 */
public abstract class UIHandler {

    @FXML
    private StackPane root;

    private Set<Integer> warningDialogShown = new HashSet<>();

    public abstract void resize();

    public abstract void release();

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

    public void warningDialog(int deviceId) {
        if (warningDialogShown.contains(deviceId))
            return;
        JFXButton ok = new JFXButton("确定");
        JFXButton cancel = new JFXButton("取消");
        ok.setPrefSize(70, 35);
        cancel.setPrefSize(70, 35);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("警告"));
        content.setBody(new Text("设备 " + deviceId + " 检测到变动，是否立即查看视频？"));
        content.setActions(cancel, ok);
        JFXDialog dialog = new JFXDialog(root, content, JFXDialog.DialogTransition.BOTTOM);
        dialog.show();
        ok.setOnAction(event -> {
            new Thread(() -> Factory.Network.connect(deviceId)).start();
            dialog.close();
        });
        cancel.setOnAction(event -> dialog.close());
        dialog.setOnDialogClosed(event -> warningDialogShown.remove(deviceId));
        warningDialogShown.add(deviceId);
    }
}
