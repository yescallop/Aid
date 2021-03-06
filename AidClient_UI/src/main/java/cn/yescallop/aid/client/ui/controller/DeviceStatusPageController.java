package cn.yescallop.aid.client.ui.controller;

import cn.yescallop.aid.client.api.Factory;
import cn.yescallop.aid.client.api.UIHandler;
import cn.yescallop.aid.client.network.DeviceInfo;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.ViewController;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javax.annotation.PostConstruct;
import java.util.function.Function;

/**
 * @author Magical Sheep
 */
@ViewController(value = "/page/DeviceStatusPage.fxml", title = "DeviceStatus")
public class DeviceStatusPageController extends UIHandler {

    @FXML
    private JFXTreeTableView<DeviceInfo> onlineDeviceList;
    @FXML
    private JFXTreeTableColumn<DeviceInfo, String> nameColumn;
    @FXML
    private JFXTreeTableColumn<DeviceInfo, String> idColumn;
    @FXML
    private JFXTreeTableColumn<DeviceInfo, String> registerTimeColumn;
    @FXML
    private ImageView screen;
    @FXML
    private BorderPane container;

    @PostConstruct
    public void init() {
        Factory.UIData.regPage(this);

        AnchorPane.setLeftAnchor(container, Factory.UIData.LRSpacing);
        AnchorPane.setRightAnchor(container, Factory.UIData.LRSpacing);
        AnchorPane.setTopAnchor(container, Factory.UIData.TBSpacing);
        AnchorPane.setBottomAnchor(container, Factory.UIData.TBSpacing);

        setupCellValueFactory(nameColumn, DeviceInfo::nameProperty);
        setupCellValueFactory(idColumn, d -> d.idProperty().asString());
        setupCellValueFactory(registerTimeColumn, d -> d.registerTimeProperty().asString());

        onlineDeviceList.setRoot(new RecursiveTreeItem<>(Factory.UIData.getOnlineDeviceList(), RecursiveTreeObject::getChildren));

        onlineDeviceList.setShowRoot(false);

        /*
          选中设备的事件将在这里进行处理
         */
        onlineDeviceList.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                new Thread(() -> Factory.Network.connect(onlineDeviceList.getSelectionModel().getSelectedItem().getValue().getId())).start();
//                showDialog("Test", onlineDeviceList.getSelectionModel().getSelectedItem().toString());
            }
        });

    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<DeviceInfo, T> column, Function<DeviceInfo, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<DeviceInfo, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    @Override
    public void resize() {

    }

    @Override
    public void release() {

    }
}