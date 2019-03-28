package cn.yescallop.aid.client.ui.controller;

import cn.yescallop.aid.client.api.Factory;
import cn.yescallop.aid.network.protocol.ControlPacket;
import io.netty.channel.Channel;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;


public class MonitorController {

    @FXML
    private StackPane root;
    @FXML
    private ImageView screen;

    public int deviceId;

    @FXML
    public void initialize() {
        root.setOnKeyPressed(event -> {
            Channel channel = Factory.Network.getDeviceChannelById(deviceId);
            if (channel == null || !channel.isActive())
                return;

            int action;
            switch (event.getCode()) {
                case W:
                    action = ControlPacket.ACTION_START;
                    break;
                case A:
                    action = ControlPacket.ACTION_LEFT;
                    break;
                case S:
                    action = ControlPacket.ACTION_STOP;
                    break;
                case D:
                    action = ControlPacket.ACTION_RIGHT;
                    break;
                case RIGHT:
                    action = ControlPacket.ACTION_CAMERA_RIGHT;
                    break;
                case LEFT:
                    action = ControlPacket.ACTION_CAMERA_LEFT;
                    break;
                default:
                    return;
            }
            channel.writeAndFlush(ControlPacket.byAction(action));
        });
    }

    public ImageView getScreen() {
        return screen;
    }
}
