package cn.yescallop.aid.client.controller;

import cn.yescallop.aid.client.Device;
import cn.yescallop.aid.client.api.Factory;
import cn.yescallop.aid.client.api.UIHandler;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.ViewController;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import javax.annotation.PostConstruct;
import java.util.function.Function;

/**
 * @author Magical Sheep
 */
@ViewController(value = "/page/DeviceStatusPage.fxml", title = "DeviceStatus")
public class DeviceStatusPageController implements UIHandler {

    @FXML
    private StackPane root;
    @FXML
    private JFXTreeTableView<Device> onlineDeviceList;
    @FXML
    private JFXTreeTableColumn<Device, String> deviceNumColumn;
    @FXML
    private JFXTreeTableColumn<Device, String> regTimeColumn;
    @FXML
    private JFXTreeTableColumn<Device, String> loginTimeColumn;
    @FXML
    private ImageView screen;

    @PostConstruct
    public void init() {
        setupCellValueFactory(deviceNumColumn,Device::deviceNumProperty);
        setupCellValueFactory(regTimeColumn,Device::regTimeProperty);
        setupCellValueFactory(loginTimeColumn,Device::loginTimeProperty);

        onlineDeviceList.setRoot(new RecursiveTreeItem<>(Factory.getOnlineDeviceList(), RecursiveTreeObject::getChildren));
        onlineDeviceList.setShowRoot(false);

        /*
          选中设备的事件将在这里进行处理
         */
        onlineDeviceList.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                showDialog("Test",onlineDeviceList.getSelectionModel().getSelectedItem().toString());
            }
        });

    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<Device, T> column, Function<Device, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Device, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    @Override
    public void sync() {
        onlineDeviceList.setRoot(new RecursiveTreeItem<>(Factory.getOnlineDeviceList(), RecursiveTreeObject::getChildren));
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