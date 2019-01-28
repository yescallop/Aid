package cn.yescallop.aid.client.menu;

import cn.yescallop.aid.client.controller.*;
import com.jfoenix.controls.JFXListView;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Magical Sheep
 */
@ViewController(value = "/SideMenu.fxml",title = "AidClient")
public class SideMenuController {

    @FXMLViewFlowContext
    private ViewFlowContext context;
    @FXML
    @ActionTrigger("首页")
    private Label home;
    @FXML
    @ActionTrigger("服务器运行状态")
    private Label serverStatus;
    @FXML
    @ActionTrigger("设备运行状态")
    private Label deviceStatus;
    @FXML
    @ActionTrigger("设备录像")
    private Label video;
    @FXML
    @ActionTrigger("设置")
    private Label setting;
    @FXML
    private JFXListView<Label> sideList;

    @PostConstruct
    public void init() {
        Objects.requireNonNull(context, "context");
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");
        sideList.propagateMouseEventsToParent();
        sideList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> new Thread(()-> Platform.runLater(()->{
            if (newVal != null) {
                try {
                    contentFlowHandler.handle(newVal.getId());
                } catch (VetoException exc) {
                    exc.printStackTrace();
                } catch (FlowException exc) {
                    exc.printStackTrace();
                }
            }
        })).start());
        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        bindNodeToController(home, ConsolePageController.class, contentFlow, contentFlowHandler);
        bindNodeToController(deviceStatus, DeviceStatusPageController.class, contentFlow, contentFlowHandler);
        bindNodeToController(video, VideoPageController.class, contentFlow, contentFlowHandler);
        bindNodeToController(serverStatus, ServerStatusPageController.class, contentFlow, contentFlowHandler);
        bindNodeToController(setting, SettingPageController.class, contentFlow, contentFlowHandler);
    }

    private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) {
        flow.withGlobalLink(node.getId(), controllerClass);
    }
}
